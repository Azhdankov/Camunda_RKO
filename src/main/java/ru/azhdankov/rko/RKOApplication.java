package ru.azhdankov.rko;

import io.camunda.zeebe.spring.client.annotation.Deployment;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Deployment(
		resources = {
				"classpath:bpmn/RKO.bpmn",
				"classpath:dmn/GetClientAndProcessTypes.dmn",
				"classpath:form/ScoringChecks.form",
				"classpath:form/CheckClientInExtSys.form"
		})
public class RKOApplication {

	public static void main(String[] args) {
		SpringApplication.run(RKOApplication.class, args);
	}

}
