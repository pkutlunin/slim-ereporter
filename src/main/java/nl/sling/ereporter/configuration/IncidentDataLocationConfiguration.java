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
public class IncidentDataLocationConfiguration {

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


}
