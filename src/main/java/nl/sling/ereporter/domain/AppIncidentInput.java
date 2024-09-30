package nl.sling.ereporter.domain;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Record representing a parsed line from an input file.
 *
 * @author Pavel Kutlunin
 */
public record AppIncidentInput(
    String appName,
    LocalDateTime startTime,
    LocalDateTime endTime,
    IncidentSeverity severity
) {

  /**
   * Calculate the duration of the incident.
   * @return Duration of the incident.
   */
  public Duration incidentDuration() {
    return Duration.between(startTime, endTime);
  }

  /**
   * Get rating based on the severity.
   * @return rating of the incident.
   */
  public int rating() {
    return severity.getRatingWeight();
  }

}
