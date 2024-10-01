package nl.sling.ereporter.processor;

import nl.sling.ereporter.domain.AppIncidentStatistics;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ActiveProfiles;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Pavel Kutlunin
 */
@SpringBootTest
@ActiveProfiles("test")
class IncidentProcessorTest {

  @Autowired
  IncidentProcessor incidentProcessor;

  @Test
  void processExampleDataset() throws IOException {
    ParsedStatistics parsedStatistics = new ParsedStatistics();
    ClassPathResource resource = new ClassPathResource("input/input.csv");

    List<AppIncidentStatistics> statistics = incidentProcessor.process(resource.getInputStream(), parsedStatistics);

    assertNotNull(statistics);
    assertFalse(statistics.isEmpty());
  }

  @Test
  void processErrorDataset() throws IOException {
    ParsedStatistics parsedStatistics = new ParsedStatistics();
    ClassPathResource resource = new ClassPathResource("input/errors.csv");

    List<AppIncidentStatistics> statistics = incidentProcessor.process(resource.getInputStream(), parsedStatistics);

    assertNotNull(statistics);
    assertTrue(statistics.isEmpty());
  }

  @Test
  void firstLineIsHeader() throws IOException {
    ParsedStatistics parsedStatistics = new ParsedStatistics();
    ByteArrayInputStream inputStream = new ByteArrayInputStream("""
        App Name,This is a date column,This is not parsed into a date
        """.getBytes(StandardCharsets.UTF_8));

    List<AppIncidentStatistics> statistics = incidentProcessor.process(inputStream, parsedStatistics);

    assertNotNull(statistics);
    assertTrue(statistics.isEmpty());
  }

}
