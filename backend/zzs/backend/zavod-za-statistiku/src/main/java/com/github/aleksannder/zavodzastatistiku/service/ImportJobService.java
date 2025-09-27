package com.github.aleksannder.zavodzastatistiku.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.aleksannder.zavodzastatistiku.model.DataPoint;
import com.github.aleksannder.zavodzastatistiku.model.Dataset;
import com.github.aleksannder.zavodzastatistiku.model.ImportJob;
import com.github.aleksannder.zavodzastatistiku.repository.DataPointRepository;
import com.github.aleksannder.zavodzastatistiku.repository.DatasetVersionRepository;
import com.github.aleksannder.zavodzastatistiku.repository.ImportJobRepository;
import com.github.aleksannder.zavodzastatistiku.repository.IndicatorRepository;
import jakarta.transaction.Transactional;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ImportJobService {

    private final DatasetVersionRepository datasetVersionRepository;
    private final ImportJobRepository importJobRepository;
    private final IndicatorRepository indicatorRepository;
    private final ObjectMapper objectMapper;
    private final DataPointRepository dataPointRepository;

    @Transactional
    public ImportJob importCsvWithJob(Long datasetVersionId, Long indicatorId, MultipartFile file) {
        Dataset dataset = datasetVersionRepository.getReferenceById(datasetVersionId).getDataset();

        ImportJob job = ImportJob.builder()
                .dataset(dataset)
                .sourceType("CSV")
                .status("IN_PROGRESS")
                .build();
        job = importJobRepository.save(job);

        try (Reader reader = new InputStreamReader(file.getInputStream())) {
            CSVParser csvParser = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(reader);

            for (CSVRecord record : csvParser) {
                Map<String, Object> dims = new HashMap<>();
                Map<String, Object> measures = new HashMap<>();

                for (String header : csvParser.getHeaderMap().keySet()) {
                    String value = record.get(header);
                    if (header.startsWith("dim_")) {
                        dims.put(header.substring(4), value);
                    } else if (header.startsWith("measure_")) {
                        measures.put(header.substring(8), Double.parseDouble(value));
                    }
                }

                DataPoint dp = DataPoint.builder()
                        .indicator(indicatorRepository.getReferenceById(indicatorId))
                        .datasetVersion(datasetVersionRepository.getReferenceById(datasetVersionId))
                        .dims(objectMapper.writeValueAsString(dims))
                        .measures(objectMapper.writeValueAsString(measures))
                        .build();

                dataPointRepository.save(dp);
            }

            job.setStatus("COMPLETED");
            job.setFinishedAt(Instant.now());
            return importJobRepository.save(job);

        } catch (Exception e) {
            job.setStatus("FAILED");
            job.setFinishedAt(Instant.now());
            job.setErrorReportPath("error-" + job.getId() + ".log");
            importJobRepository.save(job);

            throw new RuntimeException("CSV import failed", e);
        }
    }

    public byte[] exportCsv(Long datasetVersionId) {
        List<DataPoint> dataPoints = dataPointRepository.findByDatasetVersionId(datasetVersionId);

        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             CSVPrinter printer = new CSVPrinter(new PrintWriter(out), CSVFormat.DEFAULT.withHeader("id","dims","measures"))) {

            for (DataPoint dp : dataPoints) {
                printer.printRecord(
                        dp.getId(),
                        dp.getDims(),
                        dp.getMeasures()
                );
            }
            printer.flush();
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("CSV export failed", e);
        }
    }

}
