package nl.sling.ereporter.parser;

import nl.sling.ereporter.domain.AppIncidentInput;
import org.junit.jupiter.api.Test;

import java.time.format.DateTimeParseException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Pavel Kutlunin
 */
class IncidentParserTest {

  private final IncidentParser incidentParser = new IncidentParser();

  @Test
  void parseRecord() {
    AppIncidentInput result = incidentParser.parse("appName,2024-09-01 10:00:00,2024-09-01 12:00:00,1");

    assertNotNull(result);
    assertEquals("appName", result.appName());
    assertNotNull(result.startTime());
    assertNotNull(result.endTime());
    assertNotNull(result.severity());
  }

  @Test
  void appNameIsTrimmed() {
    AppIncidentInput result = incidentParser.parse("  trimmed ,2024-09-01 10:00:00,2024-09-01 12:00:00,1");

    assertEquals("trimmed", result.appName());
  }

  @Test
  void invalidStartDate() {
    String line = "appName,--,2024-09-01 12:00:00,1";
    assertThrows(DateTimeParseException.class, () -> incidentParser.parse(line));
  }

  @Test
  void invalidEndDate() {
    String line = "appName,2024-09-01 10:00:00,--,1";
    assertThrows(DateTimeParseException.class, () -> incidentParser.parse(line));
  }

  @Test
  void invalidSeverity() {
    String line = "appName,2024-09-01 10:00:00,2024-09-01 12:00:00,--";
    assertThrows(IllegalArgumentException.class, () -> incidentParser.parse(line));
  }

}
