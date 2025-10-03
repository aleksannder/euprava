package com.github.aleksannder.zavodzastatistiku.service.csv;

import com.github.aleksannder.zavodzastatistiku.model.enums.Region;
import com.github.aleksannder.zavodzastatistiku.model.gdp.GdpStat;
import com.github.aleksannder.zavodzastatistiku.model.population.PopulationStat;
import com.github.aleksannder.zavodzastatistiku.model.traffic.TrafficStat;
import com.github.aleksannder.zavodzastatistiku.model.wage.WageStat;
import com.github.aleksannder.zavodzastatistiku.repository.gdp.GdpStatRepository;
import com.github.aleksannder.zavodzastatistiku.repository.population.PopulationStatRepository;
import com.github.aleksannder.zavodzastatistiku.repository.traffic.TrafficStatRepository;
import com.github.aleksannder.zavodzastatistiku.repository.wage.WageStatRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class CsvService {

    private final TrafficStatRepository trafficStatRepository;
    private final GdpStatRepository gdpStatRepository;
    private final WageStatRepository wageStatRepository;
    private final PopulationStatRepository populationStatRepository;

    private static final Map<String, List<String>> EXPECTED_HEADERS = Map.of(
            "GDP", List.of("region", "year", "gdp_billion", "growth_percent", "cpi_percent"),
            "POP", List.of("region", "year", "population", "average_age", "birth_rate", "mortality_rate"),
            "TRAFFIC", List.of("region", "year", "registered_vehicles", "traffic_accidents", "fatalities"),
            "WAGE", List.of("region", "year", "average_wage", "growth_percent")
    );

    private static final String[] GDP_HEADERS = {
            "region", "year", "gdp_billion", "growth_percent", "cpi_percent"
    };

    private static final String[] POPULATION_HEADERS = {
            "region", "year", "population", "average_age", "birth_rate", "mortality_rate"
    };

    private static final String[] TRAFFIC_HEADERS = {
            "region", "year", "registered_vehicles", "traffic_accidents", "fatalities"
    };

    private static final String[]  WAGE_HEADERS = {
            "region", "year", "average_wage", "growth_percent"
    };


    public void importCsv(String domain, MultipartFile file) {
        if (!EXPECTED_HEADERS.containsKey(domain)) {
            log.error("[ImportExportCsvService] Invalid domain: " + domain);
            throw new IllegalArgumentException("Invalid domain: " + domain);
        }

        try (Reader reader = new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8)) {
            CSVParser parser = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(reader);

            List<String> headers = new ArrayList<>(parser.getHeaderMap().keySet());
            if (!headers.equals(EXPECTED_HEADERS.get(domain))) {
                throw new InvalidCsvHeaderException("[ImportExportCsvService] Invalid headers: " + EXPECTED_HEADERS.get(domain));
            }

            for (CSVRecord record : parser) {
                mapToEntity(domain, record);
            }

        } catch (Exception ex) {
            throw new RuntimeException("An error occurred while importing CSV file: " + file.getOriginalFilename(), ex);
        }
    }

    public ResponseEntity<Resource> exportCsv(String domain) {
        Resource result = switch (domain) {
            case "GDP" -> exportGdp();
            case "POP" -> exportPopulation();
            case "TRAFFIC" -> exportTraffic();
            case "WAGE" -> exportWage();
            default -> throw new IllegalArgumentException("Invalid domain: " + domain);
        };

        return ResponseEntity.ok(result);
    }

    private void mapToEntity(String domain, CSVRecord record) {
        Map<String, String> row = record.toMap();
        switch (domain) {
            case "GDP":
                mapToGdp(row);
                break;
            case "POP":
                mapToPopulation(row);
                break;
            case "TRAFFIC":
                mapToTraffic(row);
                break;
            case "WAGE":
                mapToWage(row);
                break;
        }
    }

    private void mapToWage(Map<String, String> row) {
        WageStat wageStat = WageStat.builder()
                .region(Region.valueOf(row.get("region")))
                .year(Integer.parseInt(row.get("year")))
                .averageWage(Double.parseDouble(row.get("average_wage")))
                .growthPercent(Double.parseDouble(row.get("growth_percent")))
                .build();

        wageStatRepository.save(wageStat);
    }

    private void mapToTraffic(Map<String, String> row) {
        TrafficStat trafficStat = TrafficStat.builder()
                .region(Region.valueOf(row.get("region")))
                .year(Integer.parseInt(row.get("year")))
                .registeredVehicles(Long.parseLong(row.get("registered_vehicles")))
                .trafficAccidents(Long.parseLong(row.get("traffic_accidents")))
                .fatalities(Long.parseLong(row.get("fatalities")))
                .build();

        trafficStatRepository.save(trafficStat);
    }

    private void mapToPopulation(Map<String, String> row) {
        PopulationStat populationStat = PopulationStat.builder()
                .region(Region.valueOf(row.get("region")))
                .year(Integer.parseInt(row.get("year")))
                .population(Long.parseLong(row.get("population")))
                .averageAge(Double.parseDouble(row.get("average_age")))
                .birthRate(Double.parseDouble(row.get("birth_rate")))
                .mortalityRate(Double.parseDouble(row.get("mortality_rate")))
                .build();

        populationStatRepository.save(populationStat);
    }

    private void mapToGdp(Map<String, String> row) {
        GdpStat gdpStat = GdpStat.builder()
                .region(Region.valueOf(row.get("region")))
                .year(Integer.parseInt(row.get("year")))
                .gdpBillion(Double.parseDouble(row.get("gdp_billion")))
                .growthPercent(Double.parseDouble(row.get("growth_percent")))
                .cpiPercent(Double.parseDouble(row.get("cpi_percent")))
                .build();

        gdpStatRepository.save(gdpStat);
    }

    private Resource exportGdp() {
        try {
            List<GdpStat> gdpStats = gdpStatRepository.findAll();

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            OutputStreamWriter writer = new OutputStreamWriter(out, StandardCharsets.UTF_8);

            CSVPrinter printer = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader(GDP_HEADERS));

            for (GdpStat gdpStat : gdpStats) {
                printer.printRecord(gdpStat.getRegion(), gdpStat.getYear(), gdpStat.getGdpBillion(), gdpStat.getGrowthPercent(), gdpStat.getCpiPercent());
            }

            printer.flush();

            return new ByteArrayResource(out.toByteArray());
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private Resource exportPopulation() {
        try {
            List<PopulationStat> populationStats = populationStatRepository.findAll();

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            OutputStreamWriter writer = new OutputStreamWriter(out, StandardCharsets.UTF_8);

            CSVPrinter printer = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader(POPULATION_HEADERS));

            for (PopulationStat popStat : populationStats) {
                printer.printRecord(popStat.getRegion(), popStat.getYear(), popStat.getPopulation(), popStat.getAverageAge(), popStat.getBirthRate(), popStat.getMortalityRate());
            }

            printer.flush();

            return new ByteArrayResource(out.toByteArray());
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private Resource exportWage() {
        try {
            List<WageStat> wageStats = wageStatRepository.findAll();

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            OutputStreamWriter writer = new OutputStreamWriter(out, StandardCharsets.UTF_8);

            CSVPrinter printer = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader(WAGE_HEADERS));

            for (WageStat wageStat : wageStats) {
                printer.printRecord(wageStat.getRegion(), wageStat.getYear(), wageStat.getAverageWage(), wageStat.getGrowthPercent());
            }

            printer.flush();

            return new ByteArrayResource(out.toByteArray());
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private Resource exportTraffic() {
        try {
            List<TrafficStat> trafficStats = trafficStatRepository.findAll();

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            OutputStreamWriter writer = new OutputStreamWriter(out, StandardCharsets.UTF_8);

            CSVPrinter printer = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader(TRAFFIC_HEADERS));

            for (TrafficStat trafficStat : trafficStats) {
                printer.printRecord(trafficStat.getRegion(), trafficStat.getYear(), trafficStat.getRegisteredVehicles(), trafficStat.getTrafficAccidents(), trafficStat.getFatalities());
            }

            printer.flush();

            return new ByteArrayResource(out.toByteArray());
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
