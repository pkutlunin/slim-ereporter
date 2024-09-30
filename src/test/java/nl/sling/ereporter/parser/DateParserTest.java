package nl.sling.ereporter.parser;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Pavel Kutlunin
 */
class DateParserTest {

  @Test
  void parseFromDatasetWithoutSeconds() {
    LocalDateTime result = DateParser.parseDateTime("4/1/2019 8:10");

    assertEquals(1, result.getDayOfMonth());
    assertEquals(4, result.getMonthValue());
    assertEquals(2019, result.getYear());
    assertEquals(8, result.getHour());
    assertEquals(10, result.getMinute());
    assertEquals(0, result.getSecond());
  }

  @Test
  void parseFromDatasetWithSeconds() {
    LocalDateTime result = DateParser.parseDateTime("4/2/2019 10:31:55");

    assertEquals(2, result.getDayOfMonth());
    assertEquals(4, result.getMonthValue());
    assertEquals(2019, result.getYear());
    assertEquals(10, result.getHour());
    assertEquals(31, result.getMinute());
    assertEquals(55, result.getSecond());
  }

  @Test
  void parseFromRequirementsWithoutSeconds() {
    LocalDateTime result = DateParser.parseDateTime("2019-05-01 13:11");

    assertEquals(1, result.getDayOfMonth());
    assertEquals(5, result.getMonthValue());
    assertEquals(2019, result.getYear());
    assertEquals(13, result.getHour());
    assertEquals(11, result.getMinute());
    assertEquals(0, result.getSecond());
  }

  @Test
  void parseFromRequirementsWithSeconds() {
    LocalDateTime result = DateParser.parseDateTime("2019-05-01 13:05:20");

    assertEquals(1, result.getDayOfMonth());
    assertEquals(5, result.getMonthValue());
    assertEquals(2019, result.getYear());
    assertEquals(13, result.getHour());
    assertEquals(5, result.getMinute());
    assertEquals(20, result.getSecond());
  }

}
