package ru.azhdankov.rko.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import ru.azhdankov.rko.RkoApplicationTests;

class ControllerTest extends RkoApplicationTests {

  @Test
  void processRestControllerInTest() throws Exception {
    mockMvc
        .perform(
            post("/start_process")
                .contentType(MediaType.APPLICATION_JSON)
                .content(readFile("startProcessPostRequest.json")))
        .andExpect(status().isOk());
  }
}
