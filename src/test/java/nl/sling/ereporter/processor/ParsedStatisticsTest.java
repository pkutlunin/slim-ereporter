package nl.sling.ereporter.processor;

import nl.sling.ereporter.domain.AppIncidentInput;
import nl.sling.ereporter.domain.AppIncidentStatistics;
import nl.sling.ereporter.domain.IncidentSeverity;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Pavel Kutlunin
 */
class ParsedStatisticsTest {

  ParsedStatistics parsedStatistics = new ParsedStatistics();

  @Test
  void noRecords() {
    List<AppIncidentStatistics> statistics = parsedStatistics.getStatisticsForAllDays();

    assertNotNull(statistics);
    assertTrue(statistics.isEmpty());
  }

  @Test
  void noRecordsForTheDate() {
    AppIncidentInput input = new AppIncidentInput("app",
        LocalDateTime.now().minusMinutes(10), LocalDateTime.now(), IncidentSeverity.CRITICAL);
    parsedStatistics.add(input);

    List<AppIncidentStatistics> statistics = parsedStatistics.getStatisticsForDate(LocalDate.parse("2000-01-01"));

    assertNotNull(statistics);
    assertTrue(statistics.isEmpty());
  }

  @Test
  void recordsForTheSpecificDate() {
    AppIncidentInput input = new AppIncidentInput("app",
        LocalDateTime.parse("2010-02-02T00:00:00"), LocalDateTime.now(), IncidentSeverity.CRITICAL);
    parsedStatistics.add(input);

    List<AppIncidentStatistics> statistics = parsedStatistics.getStatisticsForDate(LocalDate.parse("2010-02-02"));

    assertEquals(1, statistics.size());
  }

  @Test
  void recordsPerAppPerDayEndUpInOneStatistics() {
    String appName = "appName";
    AppIncidentInput input1 = new AppIncidentInput(appName,
        LocalDateTime.parse("2010-03-03T00:00:00"), LocalDateTime.now(), IncidentSeverity.CRITICAL);
    AppIncidentInput input2 = new AppIncidentInput(appName,
        LocalDateTime.parse("2010-03-03T01:00:00"), LocalDateTime.now(), IncidentSeverity.CRITICAL);
    parsedStatistics.add(input1);
    parsedStatistics.add(input2);

    List<AppIncidentStatistics> statistics = parsedStatistics.getStatisticsForAllDays();

    assertEquals(1, statistics.size());
  }


  @Test
  void getAllRecords() {
    AppIncidentInput input1 = new AppIncidentInput("app1",
        LocalDateTime.parse("2010-01-01T01:01:01"), LocalDateTime.now(), IncidentSeverity.CRITICAL);
    AppIncidentInput input2 = new AppIncidentInput("app2",
        LocalDateTime.parse("2010-02-02T02:02:02"), LocalDateTime.now(), IncidentSeverity.CRITICAL);

    parsedStatistics.add(input1);
    parsedStatistics.add(input2);

    List<AppIncidentStatistics> statistics = parsedStatistics.getStatisticsForAllDays();

    assertEquals(2, statistics.size());
  }

}
