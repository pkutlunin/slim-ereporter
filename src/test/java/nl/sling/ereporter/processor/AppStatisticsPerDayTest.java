package nl.sling.ereporter.processor;

import nl.sling.ereporter.domain.AppIncidentInput;
import nl.sling.ereporter.domain.AppIncidentStatistics;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Pavel Kutlunin
 */
class AppStatisticsPerDayTest {

  private final AppStatisticsPerDay statistics = new AppStatisticsPerDay("appName1", LocalDate.now());

  @Test
  void appNameComesFromTheInstance() {
    AppIncidentInput input = mock(AppIncidentInput.class);
    when(input.appName()).thenReturn("appName2");

    AppIncidentStatistics result = statistics.aggregate();

    assertNotNull(result);
    assertEquals("appName1", result.appName());
  }

  @Test
  void incidentsAreCounted() {
    AppIncidentInput input1 = mock(AppIncidentInput.class);
    AppIncidentInput input2 = mock(AppIncidentInput.class);

    statistics.add(input1);
    statistics.add(input2);
    AppIncidentStatistics result = statistics.aggregate();

    assertEquals(2, result.totalNumberOfIncidents());
  }

  @ParameterizedTest
  @CsvSource({
      "0,0.00",
      "86400,100.00"
  })
  void totalDowntime(int downtimeSeconds, BigDecimal expectedPercentage) {
    AppIncidentInput input = mock(AppIncidentInput.class);
    when(input.incidentDuration()).thenReturn(Duration.ofSeconds(downtimeSeconds));

    statistics.add(input);
    AppIncidentStatistics result = statistics.aggregate();

    assertEquals(expectedPercentage, result.totalDownTimePercentage().setScale(2, RoundingMode.UNNECESSARY));
  }


  @Test
  void totalDowntimeIsCalculated() {
    AppIncidentInput input1 = mock(AppIncidentInput.class);
    AppIncidentInput input2 = mock(AppIncidentInput.class);
    when(input1.incidentDuration()).thenReturn(Duration.ofSeconds(8600));
    when(input2.incidentDuration()).thenReturn(Duration.ofSeconds(40));

    statistics.add(input1);
    statistics.add(input2);
    AppIncidentStatistics result = statistics.aggregate();

    assertEquals(new BigDecimal("10.00"), result.totalDownTimePercentage().setScale(2, RoundingMode.UNNECESSARY));
  }

  @Test
  void ratingIsCorrect() {
    AppIncidentInput input1 = mock(AppIncidentInput.class);
    AppIncidentInput input2 = mock(AppIncidentInput.class);
    when(input1.rating()).thenReturn(1);
    when(input2.rating()).thenReturn(2);

    statistics.add(input1);
    statistics.add(input2);
    AppIncidentStatistics result = statistics.aggregate();

    assertEquals(3, result.rating());
  }

}
