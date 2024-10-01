package nl.sling.ereporter.processor;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.sling.ereporter.configuration.IncidentProcessingConfiguration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;

/**
 * @author Pavel Kutlunin
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class JobScheduler {

  private final IncidentProcessingConfiguration processingConfiguration;
  private final IncidentProcessor incidentProcessor;
  private final CsvWriter csvWriter;

  @Scheduled(cron = "${app-incident.processing-cron}", zone = "${app-incident.processing-cron-zone}")
  public void processInputFile() {
    Path inputPath = Path.of(processingConfiguration.getDataFolder(), processingConfiguration.getInputFileName());
    Path outputPath = Path.of(processingConfiguration.getDataFolder(), processingConfiguration.getOutputFileName());
    ParsedStatistics statistics = new ParsedStatistics();
    try {
      log.info("Starting processing");
      process(inputPath, outputPath, statistics);
      log.info("Processing has ended");
    } catch (IOException e) {
      log.error("An error occurred during processing of app incidents", e);
    }
  }

  private void process(Path inputPath, Path outputPath, ParsedStatistics statistics) throws IOException {
    try (InputStream inputStream = new FileInputStream(inputPath.toFile())) {
      incidentProcessor.process(inputStream, statistics);
    }
    log.debug("The input file {} has been processed", inputPath);
    try (OutputStream outputStream = new FileOutputStream(outputPath.toFile())) {
      csvWriter.write(statistics.getStatisticsForAllDays(), outputStream);
    }
    log.debug("The output file {} has been written", outputPath);
  }


}
