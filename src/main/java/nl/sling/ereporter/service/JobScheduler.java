package nl.sling.ereporter.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


/**
 * Class to schedule the incident processing.
 *
 * @author Pavel Kutlunin
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class JobScheduler {

  private final ProcessingService processingService;

  @Scheduled(cron = "${app-incident.processing-cron}", zone = "${app-incident.processing-cron-zone}")
  public void processInputFile() {
    log.info("Starting scheduled processing");
    processingService.process();
  }

}
