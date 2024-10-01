package nl.sling.ereporter.controller;

import lombok.RequiredArgsConstructor;
import nl.sling.ereporter.service.ProcessingService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.io.InputStream;
import java.util.Optional;

/**
 * @author Pavel Kutlunin
 */
@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {

  private final ProcessingService processingService;

  @PostMapping("/trigger")
  @ResponseStatus(HttpStatus.ACCEPTED)
  public void triggerProcessing() {
    processingService.asyncProcess();
  }

  @GetMapping
  @SuppressWarnings("rawtypes")
  public ResponseEntity download() {
    Optional<InputStream> outputFile = processingService.getOutputFile();
    if (outputFile.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body("Output file was not found. Maybe the input file hasn't been processed yet?");
    }
    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=output.csv")
        .contentType(MediaType.APPLICATION_OCTET_STREAM)
        .body(new InputStreamResource(outputFile.get()));
  }

}
