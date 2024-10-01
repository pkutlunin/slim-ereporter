package nl.sling.ereporter.processor;

import nl.sling.ereporter.domain.AppIncidentStatistics;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Pavel Kutlunin
 */
@SpringBootTest
class CsvWriterTest {

  @Autowired
  CsvWriter csvWriter;

  @Test
  void outputContainsHeader() throws IOException {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    csvWriter.write(List.of(), outputStream);
    String result = outputStream.toString();

    long lineNumer = result.lines().count();
    assertEquals(1, lineNumer);
  }

  @Test
  void outputWithOneLine() throws IOException {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    AppIncidentStatistics statistics = new AppIncidentStatistics("app 1", 10,
        new BigDecimal("10.1234").setScale(4, RoundingMode.UNNECESSARY), 5);

    csvWriter.write(List.of(statistics), outputStream);
    String result = outputStream.toString();

    List<String> lines = result.lines().toList();
    assertEquals(2, lines.size());
    assertEquals("app 1,10,10.123400,5", lines.get(1));
  }

}
