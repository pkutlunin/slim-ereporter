package nl.sling.ereporter.parser;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Pavel Kutlunin
 */
class CsvLineParserTest {

  private static final String LINE = "first,second, third ";

  @Test
  void parseFirstValue() {
    CsvLineParser lineParser = new CsvLineParser(LINE);

    String result = lineParser.getNextValue();

    assertEquals("first", result);
  }

  @Test
  void parseSecondValue() {
    CsvLineParser lineParser = new CsvLineParser(LINE);

    lineParser.getNextValue();
    String result = lineParser.getNextValue();

    assertEquals("second", result);
  }

  @Test
  void thirdValueIsUntrimmed() {
    CsvLineParser lineParser = new CsvLineParser(LINE);

    lineParser.getNextValue();
    lineParser.getNextValue();
    String result = lineParser.getNextValue();

    assertEquals(" third ", result);
  }

  @Test
  void emptyStringInsteadOfNull() {
    CsvLineParser lineParser = new CsvLineParser(",");

    String result = lineParser.getNextValue();

    assertTrue(result.isEmpty());
  }


  @Test
  void cannotFetchAfterLastValue() {
    CsvLineParser lineParser = new CsvLineParser(LINE);

    lineParser.getNextValue();
    lineParser.getNextValue();
    lineParser.getNextValue();

    assertThrows(IllegalStateException.class, lineParser::getNextValue);
  }

  @Test
  void valueAfterLastCommaDoesNotExist() {
    CsvLineParser lineParser = new CsvLineParser(",");

    lineParser.getNextValue();

    assertThrows(IllegalStateException.class, lineParser::getNextValue);
  }

}
