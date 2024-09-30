package nl.sling.ereporter.domain;

import java.time.LocalDateTime;

/**
 * Record representing a parsed line from an input file.
 *
 * @author Pavel Kutlunin
 */
public record AppIncidentInput(
    String appName,
    LocalDateTime startTime,
    LocalDateTime endTime,
    IncidentSeverity severity
) {

}
