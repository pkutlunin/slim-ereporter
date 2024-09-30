package nl.sling.ereporter.domain;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Pavel Kutlunin
 */
class AppIncidentInputTest {

  private static final String APP_NAME = "appName";
  private static final LocalDateTime START = LocalDateTime.now();
  private static final LocalDateTime END = START.plusSeconds(10);
  private static final IncidentSeverity INCIDENT_SEVERITY = IncidentSeverity.CRITICAL;

  @Test
  void startTimeIsNull() {
    AppIncidentInput input = new AppIncidentInput(APP_NAME, null, END, INCIDENT_SEVERITY);

    assertThrows(NullPointerException.class, input::incidentDuration);
  }

  @Test
  void endTimeIsNull() {
    AppIncidentInput input = new AppIncidentInput(APP_NAME, START, null, INCIDENT_SEVERITY);

    assertThrows(NullPointerException.class, input::incidentDuration);
  }

  @Test
  void startAndEndAreNull() {
    AppIncidentInput input = new AppIncidentInput(APP_NAME, null, null, INCIDENT_SEVERITY);

    assertThrows(NullPointerException.class, input::incidentDuration);
  }

  @Test
  void incidentDuration() {
    AppIncidentInput input = new AppIncidentInput(APP_NAME, START, END, INCIDENT_SEVERITY);

    assertEquals(Duration.ofSeconds(10), input.incidentDuration());
  }

  @Test
  void severityIsNull() {
    AppIncidentInput input = new AppIncidentInput(APP_NAME, START, END, null);

    assertThrows(NullPointerException.class, input::rating);
  }

  @Test
  void rating() {
    AppIncidentInput input = new AppIncidentInput(APP_NAME, START, END, IncidentSeverity.CRITICAL);

    assertEquals(30, input.rating());
  }

}
