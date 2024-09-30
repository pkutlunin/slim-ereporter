package nl.sling.ereporter.domain;

import java.math.BigDecimal;

/**
 * Represents one line in output.csv file.
 *
 * @author Pavel Kutlunin
 */
public record AppIncidentStatistics(
    String appName,
    int totalNumberOfIncidents,
    BigDecimal totalDownTimePercentage,
    long rating
) {

}
