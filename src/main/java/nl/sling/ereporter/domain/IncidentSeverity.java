package nl.sling.ereporter.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

/**
 * Severity of an incident.
 *
 * @author Pavel Kutlunin
 */
@RequiredArgsConstructor
@Getter
public enum IncidentSeverity {

  CRITICAL(1, 30),
  MAJOR(2, 10),
  MINOR(3, 10)
  ;


  /**
   * Numerical value of the criticality of an incident.
   */
  private final int severity;

  /**
   * Weight of the severity which affect the rating of the app.
   */
  private final int ratingWeight;

  /**
   * Return enum value based on a given severity.
   *
   * @param value severity. Examples: 1, 2, 3.
   * @return IncidentSeverity that corresponds to the given severity.
   * @throws IllegalArgumentException when the severity is unknown.
   */
  public static IncidentSeverity getBySeverity(int value) {
    return Arrays.stream(values())
        .filter(val -> val.severity == value)
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException(
            "Severity %d is not recognized".formatted(value))
        );
  }

}
