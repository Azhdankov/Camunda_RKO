package ru.azhdankov.rko;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import org.apache.commons.io.FileUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.process.test.api.ZeebeTestEngine;
import io.camunda.zeebe.spring.test.ZeebeSpringTest;
import org.camunda.community.process_test_coverage.junit5.platform8.ProcessEngineCoverageExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@ZeebeSpringTest
@ExtendWith(ProcessEngineCoverageExtension.class)
public abstract class RKOApplicationTests {

	protected MockMvc mockMvc;

	@Autowired
	protected ZeebeClient zeebe;

	@Autowired
	protected ZeebeTestEngine engine;

	@Autowired
	protected ObjectMapper objectMapper;

	@BeforeEach
	protected void setUp(WebApplicationContext context) {
		this.mockMvc = MockMvcBuilders
				.webAppContextSetup(context)
				.addFilter((ServletRequest request, ServletResponse response, FilterChain chain)-> {
					response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
					request.setCharacterEncoding(StandardCharsets.UTF_8.toString());
					chain.doFilter(request, response);
				})
				.alwaysDo(print())
				.build();
	}

	protected <T> T readFile(String classPath,Class<T> clazz) throws IOException {
		InputStream file = new ClassPathResource(classPath).getInputStream();
		JsonNode jsonNode = objectMapper.readTree(file);
		return objectMapper.treeToValue(jsonNode, clazz);
	}

	protected String readFile(String classPath) throws IOException {
		ClassPathResource file = new ClassPathResource(classPath);
		return FileUtils.readFileToString(file.getFile(), StandardCharsets.UTF_8.toString());
	}

}
