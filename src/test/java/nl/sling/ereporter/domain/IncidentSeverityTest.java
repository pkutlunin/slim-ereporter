package nl.sling.ereporter.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Pavel Kutlunin
 */
class IncidentSeverityTest {


  static Stream<Arguments> severityWithType() {
    return Stream.<Arguments>builder()
        .add(Arguments.of(1, IncidentSeverity.CRITICAL))
        .add(Arguments.of(2, IncidentSeverity.MAJOR))
        .add(Arguments.of(3, IncidentSeverity.MINOR))
        .build();
  }

  @ParameterizedTest
  @MethodSource(value = "severityWithType")
  void findBySeverity(int severity, IncidentSeverity expectedIncidentSeverity) {
    IncidentSeverity result = IncidentSeverity.getBySeverity(severity);

    assertEquals(expectedIncidentSeverity, result);
  }

  @Test
  void unknownSeverity() {
    int unknownSeverity = 999;

    assertThrows(IllegalArgumentException.class, () ->
      IncidentSeverity.getBySeverity(unknownSeverity)
    );
  }

}
