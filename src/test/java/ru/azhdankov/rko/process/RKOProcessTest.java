package ru.azhdankov.rko.process;

import static io.camunda.zeebe.process.test.assertions.BpmnAssert.assertThat;
import static io.camunda.zeebe.protocol.Protocol.USER_TASK_JOB_TYPE;
import static io.camunda.zeebe.spring.test.ZeebeTestThreadSupport.*;

import com.fasterxml.jackson.databind.node.ArrayNode;
import io.camunda.zeebe.client.api.response.ProcessInstanceEvent;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import ru.azhdankov.rko.RKOApplicationTests;
import ru.azhdankov.rko.data.InitiateProcessVariables;
import java.io.InputStream;
import java.time.Duration;
import java.util.concurrent.TimeoutException;

class RKOProcessTest extends RKOApplicationTests {

    @Test
    void processOnbAndInternetBankHappyPath() throws Exception {
        InitiateProcessVariables variables = readFile("processOnbAndInternetBankStart.json", InitiateProcessVariables.class);
        ArrayNode result = objectMapper.readValue("[{\"processType\":\"ONB\",\"clientType\":\"LE\"}]", ArrayNode.class);
        ProcessInstanceEvent processInstanceEvent = zeebe
                .newCreateInstanceCommand()
                .bpmnProcessId("RKO_process")
                .latestVersion()
                .variables(variables)
                .send()
                .join();
        assertThat(processInstanceEvent).isStarted();
        assertThat(processInstanceEvent)
                .hasVariableWithValue("result", result);
        zeebe.newPublishMessageCommand()
                .messageName("Message_ContinueONBProcess")
                .correlationKey(variables.getCorrKey())
                .timeToLive(Duration.ofSeconds(15))
                .send()
                .join();
        completeUserTasks();
        waitForProcessInstanceHasPassedElement(processInstanceEvent,"ConnectAddProduct");
        assertThat(processInstanceEvent).isCompleted();
    }

    @Test
    void processActlHappyPath() throws Exception {
        InitiateProcessVariables variables = readFile("processActlStart.json", InitiateProcessVariables.class);
        ArrayNode result = objectMapper.readValue("[{\"processType\":\"ACTL\",\"clientType\":\"LE\"}]", ArrayNode.class);
        ProcessInstanceEvent processInstanceEvent = zeebe
                .newCreateInstanceCommand()
                .bpmnProcessId("RKO_process")
                .latestVersion()
                .variables(variables)
                .send()
                .join();
        assertThat(processInstanceEvent).isStarted();
        assertThat(processInstanceEvent)
                .hasVariableWithValue("result", result);
        zeebe.newPublishMessageCommand()
                .messageName("Message_ExtSys")
                .correlationKey(variables.getCorrKey())
                .timeToLive(Duration.ofSeconds(15))
                .send()
                .join();
        completeUserTasks();
        waitForProcessInstanceHasPassedElement(processInstanceEvent,"Flow_09ve2vm");
        assertThat(processInstanceEvent).isCompleted();
    }

    public void completeUserTasks()
            throws InterruptedException, TimeoutException {
        engine.waitForIdleState(Duration.ofMinutes(5));

        zeebe.newActivateJobsCommand()
            .jobType(USER_TASK_JOB_TYPE)
            .maxJobsToActivate(1)
            .workerName("waitForUserTaskAndComplete")
            .send()
            .join()
            .getJobs().forEach(job -> zeebe.newCompleteCommand(job.getKey()).send().join());
    }

}
