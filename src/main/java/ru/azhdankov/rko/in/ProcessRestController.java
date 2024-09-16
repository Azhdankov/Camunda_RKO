package ru.azhdankov.rko.in;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.camunda.zeebe.client.ZeebeClient;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.azhdankov.rko.data.InitiateProcessVariables;
import ru.azhdankov.rko.data.ScorringResults;
import ru.azhdankov.rko.out.ZeebeClientController;


@RestController
@RequestMapping
@RequiredArgsConstructor
public class ProcessRestController {

    private final ZeebeClient zeebeClient;

    private final ZeebeClientController zeebeClientController;

    @PostMapping("/start_process")
    public void startProcess(@RequestBody InitiateProcessVariables variables) {
        zeebeClient.newCreateInstanceCommand()
                .bpmnProcessId("RKO_process")
                .latestVersion()
                .variables(variables)
                .send();
    }

    @PostMapping("/publish_ScorringResults_Message")
    public void publishMessage(@RequestBody String variables, @RequestParam String corrKey) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ScorringResults scorringResults = mapper.readValue(variables, ScorringResults.class);
        zeebeClientController.publishScorringResultsMessage(scorringResults, corrKey);
    }

}
