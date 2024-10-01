package nl.sling.ereporter.processor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.sling.ereporter.domain.AppIncidentInput;
import nl.sling.ereporter.domain.AppIncidentStatistics;
import nl.sling.ereporter.parser.IncidentParser;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Objects;

/**
 * Processor of incidents that accepts the InputStream which represents a CSV format.
 * It also accepts a ParsedStatistics object where the statistics will be collected.
 * The processor attempts to process records in parallel and uses a default thread pool for that.
 *
 * @author Pavel Kutlunin
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class IncidentProcessor {

  private final IncidentParser incidentParser;

  /**
   * Process input stream and aggregated the statistics into a given object.
   *
   * @param inputStream      stream to process. The stream is expected to be a set of lines
   *                         in CSV format. The first line must be a header, and it will be ignored.
   * @param parsedStatistics object to aggregate statistics into.
   * @return unsorted list of aggregated statistics per app. It may contain multiple items per app name
   * if the processed stream mixes records for different dates.
   * @throws IOException if there is a problem reading the given input stream.
   */
  public List<AppIncidentStatistics> process(InputStream inputStream, ParsedStatistics parsedStatistics)
      throws IOException {

    try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
      reader.lines()
          .skip(1)
          .parallel()
          .filter(line -> !line.isBlank())
          .map(this::parseAndLogErrors)
          .filter(Objects::nonNull)
          .forEach(parsedStatistics::add);
    }
    return parsedStatistics.getStatisticsForAllDays();
  }

  private AppIncidentInput parseAndLogErrors(String line) {
    try {
      return incidentParser.parse(line);
    } catch (Exception e) {
      log.warn("A line from CSV could not be parsed: {}: {}",
          line.substring(0, Math.min(line.length(), 50)),
          e.getMessage()
      );
      return null;
    }
  }

}
