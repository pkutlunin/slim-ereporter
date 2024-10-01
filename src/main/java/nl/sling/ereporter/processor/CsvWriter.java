package nl.sling.ereporter.processor;

import nl.sling.ereporter.domain.AppIncidentStatistics;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.List;

/**
 * Write statistics into the output stream in the CSV format.
 *
 * @author Pavel Kutlunin
 */
@Component
public class CsvWriter {

  private static final String HEADER = "Asset Name,Total Incidents,Total Downtime,Rating";

  /**
   * Write statistics into the output stream in the CSV format.
   * The first line is a constant header.
   *
   * @param statistics data to write to the output stream.
   * @throws IOException in case the data cannot be written to the given output stream.
   */
  public void write(List<AppIncidentStatistics> statistics, OutputStream outputStream) throws IOException {
    try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream))) {
      writer.write(HEADER);
      writer.newLine();
      for (AppIncidentStatistics appIncidentStatistics : statistics) {
        writer.write(formatLine(appIncidentStatistics));
        writer.newLine();
      }
      writer.flush();
    }
  }

  private String formatLine(AppIncidentStatistics statistics) {
    return "%s,%d,%.6f,%d".formatted(
        statistics.appName(),
        statistics.totalNumberOfIncidents(),
        statistics.totalDownTimePercentage(),
        statistics.rating()
    );
  }

}
