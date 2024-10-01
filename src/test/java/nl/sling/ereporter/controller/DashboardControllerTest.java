package nl.sling.ereporter.controller;

import nl.sling.ereporter.service.ProcessingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Pavel Kutlunin
 */
@WebMvcTest
@ActiveProfiles("test")
class DashboardControllerTest {

  @Autowired
  MockMvc mockMvc;

  @MockBean
  ProcessingService processingService;

  @Test
  void triggerProcessing() throws Exception {
    mockMvc.perform(post("/dashboard/trigger"))
        .andExpect(status().isAccepted());

    verify(processingService).asyncProcess();
  }

  @Test
  void noOutputFile() throws Exception {
    when(processingService.getOutputFile()).thenReturn(Optional.empty());

    mockMvc.perform(get("/dashboard"))
        .andExpect(status().isNotFound());
  }

  @Test
  void downloadOutputFile() throws Exception {
    InputStream inputStream = new ByteArrayInputStream("csv,value,file,content".getBytes(StandardCharsets.UTF_8));
    when(processingService.getOutputFile()).thenReturn(Optional.of(inputStream));

    mockMvc.perform(get("/dashboard"))
        .andExpect(status().isOk())
        .andExpect(header().string("Content-Disposition", containsString("output.csv")));
  }

}
