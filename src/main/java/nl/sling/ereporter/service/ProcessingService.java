package nl.sling.ereporter.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.sling.ereporter.configuration.IncidentProcessingConfiguration;
import nl.sling.ereporter.processor.CsvWriter;
import nl.sling.ereporter.processor.IncidentProcessor;
import nl.sling.ereporter.processor.ParsedStatistics;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.Optional;

/**
 * Main class to start incident processing.
 *
 * @author Pavel Kutlunin
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ProcessingService {

  private final IncidentProcessingConfiguration processingConfiguration;
  private final IncidentProcessor incidentProcessor;
  private final CsvWriter csvWriter;

  /**
   * Start processing of the incident file.
   */
  public void process() {
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

  @Async
  public void asyncProcess() {
    log.info("Start processing asynchronously");
    process();
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

  /**
   * Get an InputStream which represents the output file with the processed app incident statistics.
   *
   * @return either an InputStream representing a processed report or an empty optional if the file cannot be read.
   */
  public Optional<InputStream> getOutputFile() {
    Path outputPath = Path.of(processingConfiguration.getDataFolder(), processingConfiguration.getOutputFileName());
    try {
      return Optional.of(new FileInputStream(outputPath.toFile()));
    } catch (FileNotFoundException e) {
      return Optional.empty();
    }
  }

}
