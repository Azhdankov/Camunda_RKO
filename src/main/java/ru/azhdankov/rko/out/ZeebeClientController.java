package ru.azhdankov.rko.out;

import io.camunda.zeebe.client.ZeebeClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.azhdankov.rko.data.ScorringResults;

@Component
@RequiredArgsConstructor
public class ZeebeClientController {

  private final ZeebeClient zeebeClient;

  public void publishScorringResultsMessage(
      ScorringResults scorringResults, String correlationKey) {
    zeebeClient
        .newPublishMessageCommand()
        .messageName("Message_ContinueONBProcess")
        .correlationKey(correlationKey)
        .variables(scorringResults)
        .send()
        .join();
  }
}
