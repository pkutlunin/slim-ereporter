package nl.sling.ereporter.parser;

import nl.sling.ereporter.domain.AppIncidentInput;
import nl.sling.ereporter.domain.IncidentSeverity;
import org.springframework.stereotype.Component;

/**
 * Parse a single line from an input CSV file into a record while converting
 * all fields to the correct data types.
 *
 * @author Pavel Kutlunin
 */
@Component
public class IncidentParser {

  public AppIncidentInput parse(String line) {
    CsvLineParser csvLineParser = new CsvLineParser(line);
    String appName = csvLineParser.getNextValue().trim();
    String startTime = csvLineParser.getNextValue().trim();
    String endTime = csvLineParser.getNextValue().trim();
    String severity = csvLineParser.getNextValue().trim();

    int severityNumeric = Integer.parseInt(severity);

    return new AppIncidentInput(
      appName,
      DateParser.parseDateTime(startTime),
      DateParser.parseDateTime(endTime),
        IncidentSeverity.getBySeverity(severityNumeric)
    );
  }

}
