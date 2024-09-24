package ru.azhdankov.rko.worker;

import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.azhdankov.rko.out.ZeebeClientController;

import java.time.LocalDate;

@Component
@AllArgsConstructor
public class Worker {

    private final ZeebeClientController zeebeClientController;

    @JobWorker
    public void sendClientInfoToExtSys(ActivatedJob job) {
        String corrKey = (String) job.getVariable("corrKey");
        System.out.println("sendClientInfoToExtSys: " + LocalDate.now() + " with variables " + job.getVariables());
    }

    @JobWorker
    public void updateClientInfoInExtSys(ActivatedJob job) {
        System.out.println("updateClientInfoInExtSys: " + LocalDate.now() + " with variables " + job.getVariables());
    }

    @JobWorker
    public void activateClient(ActivatedJob job) {
        System.out.println("activateClient: " + LocalDate.now()+ " with variables " + job.getVariables());
    }

    @JobWorker(type = "connectInternetBank")
    public void connectInternetBank(ActivatedJob job) {
        System.out.println("connect-internetbank: " + LocalDate.now() + " with variables " + job.getVariables());
    }
}