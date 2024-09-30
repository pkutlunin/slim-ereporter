package nl.sling.ereporter.parser;

import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;
import java.time.format.DateTimeFormatterBuilder;

/**
 * Although the requirements mention that both start and end dates would contain seconds,
 * this is not confirmed by a sample dataset. That's why it is required to accommodated for both cases
 * when there are specified seconds and when they are omitted.
 *
 * Additionally, there are two different time formats:
 * 1) "2019-05-01 13:05:20" - from requirements;
 * 2) "4/1/2019 8:10" - from an example dataset.
 *
 * For the second case, it is not even clear whether it starts with dd/MM or MM/dd, so assuming month first.
 *
 * This parser will try to parse with the dataformat from an example dataset
 * (which is most likely is the correct format), but if it doesn't work, will try to parse using
 * the format from the requirements.
 *
 * @author Pavel Kutlunin
 */
@UtilityClass
public final class DateParser {

  private static final DateTimeFormatter DATASET_FORMATTER = new DateTimeFormatterBuilder()
      .appendValue(ChronoField.MONTH_OF_YEAR)
      .appendLiteral('/')
      .appendValue(ChronoField.DAY_OF_MONTH)
      .appendLiteral('/')
      .appendValue(ChronoField.YEAR)
      .appendLiteral(' ')
      .appendValue(ChronoField.HOUR_OF_DAY)
      .appendLiteral(':')
      .appendValue(ChronoField.MINUTE_OF_HOUR)
      .optionalStart()
      .appendLiteral(':')
      .appendValue(ChronoField.SECOND_OF_MINUTE)
      .optionalEnd()
      .toFormatter();

  private static final DateTimeFormatter REQUIREMENTS_FORMATTER = new DateTimeFormatterBuilder()
      .appendPattern("yyyy-MM-dd HH:mm")
      .optionalStart()
      .appendLiteral(':')
      .appendValue(ChronoField.SECOND_OF_MINUTE)
      .optionalEnd()
      .toFormatter();

  /**
   * Parse the datetime using a dataformat from the dataset.
   * If that format doesn't work, try the format from requirements.
   * Both formats will take care of optional seconds.
   *
   * @param value String in either "M/d/yyyy HH:mm[:ss]" or "yyyy-MM-dd HH:mm[:ss]" format.
   * @return parsed LocalDateTime
   * @throws DateTimeParseException if neither of the  known formats works.
   */
  public LocalDateTime parseDateTime(String value) {
    try {
      return LocalDateTime.parse(value, DATASET_FORMATTER);
    } catch (DateTimeParseException e) {
      return LocalDateTime.parse(value, REQUIREMENTS_FORMATTER);
    }
  }

}
