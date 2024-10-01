package nl.sling.ereporter.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration of the location with input.csv and output.csv.
 *
 * @author Pavel Kutlunin
 */
@Configuration
@ConfigurationProperties("app-incident")
@Data
public class IncidentProcessingConfiguration {

  /**
   * Folder where the input file for processing is located. The same folder is used for the output file.
   */
  private String dataFolder;

  /**
   * The name of the input file with extension. Default: "input.csv".
   */
  private String inputFileName = "input.csv";

  /**
   * The name of the output file with extension. Default: "output.csv".
   */
  private String outputFileName = "output.csv";

  /**
   * Cron expression when the processing should be run. Example: 0 0 1 * * MON - every Monday at 1 am.
   */
  private String processingCron;

  /**
   * Zone ID which specifies the zone for the cron expression . Default: "UTC".
   */
  private String processingCronZone = "UTC";

}
