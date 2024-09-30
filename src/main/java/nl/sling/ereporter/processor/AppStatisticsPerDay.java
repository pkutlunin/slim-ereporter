package nl.sling.ereporter.processor;

import lombok.RequiredArgsConstructor;
import nl.sling.ereporter.domain.AppIncidentInput;
import nl.sling.ereporter.domain.AppIncidentStatistics;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Aggregator to keep the minimal amount of data required for the calculations.
 * So instead of keeping many objects of AppIncidentInput in the JVM heap,
 * it's more memory efficient to preprocess the required data (given such calculations allow this)
 * and dispose the AppIncidentInput object.
 * This means that there will be one object of aggregated statistics per app per day.
 * This class is thread-safe to allow parallel processing (adding only) of the input records.
 *
 * @author Pavel Kutlunin
 */
@RequiredArgsConstructor
public class AppStatisticsPerDay {

  private static final int PERCENTAGE_PRECISION = 6;
  private static final int SECONDS_IN_DAY = 24 * 60 * 60;

  private final String appName;
  private final LocalDate date;


  private final AtomicInteger incidentCount = new AtomicInteger();
  private final AtomicLong downtimeSeconds = new AtomicLong();
  private final AtomicLong rating = new AtomicLong();

  /**
   * Add record to the statistics.
   * @param input parsed CSV input.
   */
  public void add(AppIncidentInput input) {
    incidentCount.incrementAndGet();
    downtimeSeconds.addAndGet(input.incidentDuration().getSeconds());
    rating.addAndGet(input.rating());
  }

  /**
   * Return the current state of aggregate statistics
   * @return Aggregated statistics of incidents for the app and date.
   */
  public AppIncidentStatistics aggregate() {
    return new AppIncidentStatistics(
        appName,
        incidentCount.get(),
        getDowntimePercentage(),
        rating.get()
    );
  }


  private BigDecimal getDowntimePercentage() {
    return new BigDecimal(downtimeSeconds.get())
        .multiply(new BigDecimal(100))
        .divide(new BigDecimal(SECONDS_IN_DAY), PERCENTAGE_PRECISION, RoundingMode.HALF_UP);
  }

}
