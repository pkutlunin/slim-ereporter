# EReporter / SLIM 

## Prerequisites
- Java 21

## How to run
1. Add `input.csv` file to `mount` folder or adjust `app-incident.data-folder` and `app-incident.input-file-name` in `application.yaml` to point to the desired location
2. ```./mvnw spring-boot:run```
3. Run `POST http://localhost:3026/api/dashboard/trigger` to start the processing
4. Wait until the processing is finished
5. Run `GET http://localhost:3026/api/dashboard` to retrieve the output file

## Big input file assumption
Since it is expected that the input file is too big to fit to laptop, 
we can put a file on a mounted volume for example. This then can be configured via the configuration properties.
