package ru.azhdankov.rko.process;

import static io.camunda.zeebe.process.test.assertions.BpmnAssert.assertThat;
import static io.camunda.zeebe.protocol.Protocol.USER_TASK_JOB_TYPE;
import static io.camunda.zeebe.spring.test.ZeebeTestThreadSupport.*;

import com.fasterxml.jackson.databind.node.ArrayNode;
import java.time.Duration;
import java.util.concurrent.TimeoutException;
import org.junit.jupiter.api.Test;
import ru.azhdankov.rko.RKOApplicationTests;
import ru.azhdankov.rko.data.InitiateProcessVariables;
import ru.azhdankov.rko.data.ScorringResult;

class RKOProcessTest extends RKOApplicationTests {

  @Test
  void processOnbAndInternetBankHappyPath() throws Exception {
    var variables = readFile("processOnbAndInternetBankStart.json", InitiateProcessVariables.class);
    var result =
        objectMapper.readValue(
            "[{\"processType\":\"ONB\",\"clientType\":\"LE\"}]", ArrayNode.class);
    var processInstanceEvent =
        zeebe
            .newCreateInstanceCommand()
            .bpmnProcessId("RKO_process")
            .latestVersion()
            .variables(variables)
            .send()
            .join();
    assertThat(processInstanceEvent).isStarted();
    assertThat(processInstanceEvent).hasVariableWithValue("result", result);
    zeebe
        .newPublishMessageCommand()
        .messageName("Message_ContinueONBProcess")
        .correlationKey(variables.getCorrKey())
        .timeToLive(Duration.ofSeconds(15))
        .variables(new ScorringResult())
        .send()
        .join();
    completeUserTasks();
    waitForProcessInstanceHasPassedElement(processInstanceEvent, "ConnectAddProduct");
    assertThat(processInstanceEvent).isCompleted();
  }

  @Test
  void processActlHappyPath() throws Exception {
    var variables = readFile("processActlStart.json", InitiateProcessVariables.class);
    var result =
        objectMapper.readValue(
            "[{\"processType\":\"ACTL\",\"clientType\":\"LE\"}]", ArrayNode.class);
    var processInstanceEvent =
        zeebe
            .newCreateInstanceCommand()
            .bpmnProcessId("RKO_process")
            .latestVersion()
            .variables(variables)
            .send()
            .join();
    assertThat(processInstanceEvent).isStarted();
    assertThat(processInstanceEvent).hasVariableWithValue("result", result);
    zeebe
        .newPublishMessageCommand()
        .messageName("Message_ExtSys")
        .correlationKey(variables.getCorrKey())
        .timeToLive(Duration.ofSeconds(15))
        .send()
        .join();
    completeUserTasks();
    waitForProcessInstanceHasPassedElement(processInstanceEvent, "Flow_09ve2vm");
    assertThat(processInstanceEvent).isCompleted();
  }

  public void completeUserTasks() throws InterruptedException, TimeoutException {
    engine.waitForIdleState(Duration.ofMinutes(5));

    zeebe
        .newActivateJobsCommand()
        .jobType(USER_TASK_JOB_TYPE)
        .maxJobsToActivate(32)
        .workerName("waitForUserTaskAndComplete")
        .send()
        .join()
        .getJobs()
        .forEach(job -> zeebe.newCompleteCommand(job.getKey()).send().join());
  }
}
