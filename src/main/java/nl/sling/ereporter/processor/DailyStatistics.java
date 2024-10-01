package nl.sling.ereporter.processor;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import nl.sling.ereporter.domain.AppIncidentInput;
import nl.sling.ereporter.domain.AppIncidentStatistics;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Aggregated statistics for the given day per app.
 *
 * @author Pavel Kutlunin
 */
@RequiredArgsConstructor
class DailyStatistics {

  @Getter
  private final LocalDate date;

  private final Map<String, AppStatisticsPerDay> appName2statisticsPerDay = new ConcurrentHashMap<>();

  /**
   * Add and process an input record.
   *
   * @param appIncidentInput input record to be processed.
   */
  void add(AppIncidentInput appIncidentInput) {
    appName2statisticsPerDay.computeIfAbsent(appIncidentInput.appName(), key -> new AppStatisticsPerDay(key, date))
        .add(appIncidentInput);
  }

  /**
   * Get aggregated statistics. The list will contain one record per app name.
   *
   * @return unsorted list of statistics per app name.
   */
  List<AppIncidentStatistics> getStatistics() {
    return appName2statisticsPerDay.values()
        .stream()
        .map(AppStatisticsPerDay::aggregate)
        .toList();
  }

}
