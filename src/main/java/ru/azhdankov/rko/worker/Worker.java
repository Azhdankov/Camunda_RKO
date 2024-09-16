package ru.azhdankov.rko.worker;

import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.azhdankov.rko.out.ZeebeClientController;

import java.util.Date;

@Component
@AllArgsConstructor
public class Worker {

    private final ZeebeClientController zeebeClientController;
    @JobWorker
    public void sendClientInfoToExtSys(ActivatedJob job) {
        String corrKey = (String) job.getVariable("corrKey");
        System.out.println("sendClientInfoToExtSys: " + new Date() + " " + corrKey);
    }

    @JobWorker
    public void updateClientInfoInExtSys(ActivatedJob job) {
        System.out.println("updateClientInfoInExtSys: " + new Date() + " " + job.getVariable("corrKey"));
    }

    @JobWorker
    public void activateClient() {
        System.out.println("activateClient: " + new Date());
    }
}