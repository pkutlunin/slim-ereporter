package nl.sling.ereporter.parser;

import lombok.RequiredArgsConstructor;

/**
 * Each line in input.csv file looks quite long so it would be inefficient use
 * of memory to use String.split() method to get first few values of a line.
 * Luckily, all necessary values are in the beginning of each line and can be lazily parsed.
 * To use this class, create an instance of it by providing the whole line to be parsed.
 * Then it can be used to retrieve all values from a CSV file
 *
 * @author Pavel Kutlunin
 */
@RequiredArgsConstructor
public class CsvLineParser {

  private static final Character CSV_SEPARATOR = ',';

  /**
   * Original line.
   */
  private final String line;

  private int currentPosition;

  /**
   * Parse the next value from the line.
   *
   * @return untrimmed value. It could be an empty String but never null.
   * @throws IllegalStateException when the end of line is reached.
   */
  public String getNextValue() {
    if (currentPosition >= line.length()) {
      throw new IllegalStateException("The line has already been parsed to its end");
    }
    StringBuilder value = new StringBuilder();
    for (; currentPosition < line.length(); currentPosition++) {
      char currentChar = line.charAt(currentPosition);
      if (currentChar == CSV_SEPARATOR) {
        skipComma();
        break;
      } else {
        value.append(currentChar);
      }
    }
    return value.toString();
  }

  private void skipComma() {
    currentPosition++;
  }

}
