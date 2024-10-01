package nl.sling.ereporter.processor;

import nl.sling.ereporter.domain.AppIncidentInput;
import nl.sling.ereporter.domain.AppIncidentStatistics;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Parsed statistics that can be retrieved in the output format.
 *
 * @author Pavel Kutlunin
 */
public class ParsedStatistics {

  private final Map<LocalDate, DailyStatistics> date2dayStatistics = new ConcurrentHashMap<>();

  /**
   * Add an input record to the statistics and process the record.
   *
   * @param appIncidentInput Record to be added and processed.
   */
  public void add(AppIncidentInput appIncidentInput) {
    date2dayStatistics.computeIfAbsent(appIncidentInput.startTime().toLocalDate(), DailyStatistics::new)
        .add(appIncidentInput);
  }

  /**
   * Retrieve statistics for the specific date.
   * This might be used if the statistics is built for the inputs from different dates.
   *
   * @param date filter statistics based on the date.
   * @return Return a subset of statistics for the iven date. The list can be empty but never null.
   */
  public List<AppIncidentStatistics> getStatisticsForDate(LocalDate date) {
    DailyStatistics dailyStatistics = date2dayStatistics.get(date);
    if (dailyStatistics == null) {
      return List.of();
    }
    return dailyStatistics.getStatistics();
  }

  /**
   * Get statistics for all processed input records.
   * Each app could potentially occur multiple times representing different days.
   *
   * @return unsorted list of app statistics
   */
  public List<AppIncidentStatistics> getStatisticsForAllDays() {
    return date2dayStatistics.values().stream()
        .flatMap(dailyStatistics -> dailyStatistics.getStatistics().stream())
        .toList();
  }

}
