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
class DailyStatisticsTest {

  DailyStatistics dailyStatistics = new DailyStatistics(LocalDate.now());

  @Test
  void oneRecordPerApp() {
    AppIncidentInput input1 = new AppIncidentInput("app",
        LocalDateTime.now().minusMinutes(10), LocalDateTime.now(), IncidentSeverity.CRITICAL);
    AppIncidentInput input2 = new AppIncidentInput("app",
        LocalDateTime.now().minusMinutes(10), LocalDateTime.now(), IncidentSeverity.MAJOR);
    dailyStatistics.add(input1);
    dailyStatistics.add(input2);

    List<AppIncidentStatistics> statistics = dailyStatistics.getStatistics();

    assertNotNull(statistics);
    assertEquals(1, statistics.size());
  }

  @Test
  void eachAppHasOneRecord() {
    AppIncidentInput input1 = new AppIncidentInput("app1",
        LocalDateTime.now().minusMinutes(10), LocalDateTime.now(), IncidentSeverity.CRITICAL);
    AppIncidentInput input2 = new AppIncidentInput("app2",
        LocalDateTime.now().minusMinutes(10), LocalDateTime.now(), IncidentSeverity.CRITICAL);
    dailyStatistics.add(input1);
    dailyStatistics.add(input2);

    List<AppIncidentStatistics> statistics = dailyStatistics.getStatistics();

    assertNotNull(statistics);
    assertEquals(2, statistics.size());
  }

}
