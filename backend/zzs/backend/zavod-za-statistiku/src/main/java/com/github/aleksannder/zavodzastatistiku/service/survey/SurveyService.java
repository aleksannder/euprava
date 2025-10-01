package com.github.aleksannder.zavodzastatistiku.service.survey;

import com.github.aleksannder.zavodzastatistiku.dto.survey.SurveyDto;
import com.github.aleksannder.zavodzastatistiku.model.gdp.GdpStat;
import com.github.aleksannder.zavodzastatistiku.model.population.PopulationStat;
import com.github.aleksannder.zavodzastatistiku.model.survey.QuestionType;
import com.github.aleksannder.zavodzastatistiku.model.survey.Survey;
import com.github.aleksannder.zavodzastatistiku.model.survey.SurveyQuestion;
import com.github.aleksannder.zavodzastatistiku.model.survey.SurveyResponse;
import com.github.aleksannder.zavodzastatistiku.model.traffic.TrafficStat;
import com.github.aleksannder.zavodzastatistiku.model.wage.WageStat;
import com.github.aleksannder.zavodzastatistiku.repository.gdp.GdpStatRepository;
import com.github.aleksannder.zavodzastatistiku.repository.population.PopulationStatRepository;
import com.github.aleksannder.zavodzastatistiku.repository.survey.SurveyQuestionRepository;
import com.github.aleksannder.zavodzastatistiku.repository.survey.SurveyRepository;
import com.github.aleksannder.zavodzastatistiku.repository.survey.SurveyResponseRepository;
import com.github.aleksannder.zavodzastatistiku.repository.traffic.TrafficStatRepository;
import com.github.aleksannder.zavodzastatistiku.repository.wage.WageStatRepository;
import com.github.aleksannder.zavodzastatistiku.util.SurveyConverter;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SurveyService {

        private static final double NATIONAL_AVG_HH_SIZE = 2.9;
        private static final double BILLION = 1_000_000_000;
        private final SurveyRepository surveyRepository;
        private final SurveyQuestionRepository surveyQuestionRepository;
        private final SurveyResponseRepository surveyResponseRepository;
        private final WageStatRepository wageStatRepository;
        private final TrafficStatRepository trafficStatRepository;
        private final PopulationStatRepository populationStatRepository;
        private final GdpStatRepository gdpStatRepository;

        public List<SurveyDto> getAll() {
            return surveyRepository.findAll().stream().map(SurveyConverter::toDto).collect(Collectors.toList());
        }

        public List<Survey> findAll() {
            return surveyRepository.findAll();
        }

        public Survey createSurvey(Survey survey) {
            List<SurveyQuestion> questions = generateQuestions(survey);

            survey.setQuestions(questions);
            return surveyRepository.save(survey);
        }

        public SurveyDto getById(Long surveyId) {
            Survey s = surveyRepository.findById(surveyId).orElseThrow();
            return SurveyConverter.toDto(s);
        }

        public boolean hasUserResponded(Long surveyId, String userEmail) {
            return surveyResponseRepository.existsBySurveyIdAndUserEmail(surveyId, userEmail);
        }

        public void saveResponses(List<SurveyResponse> responses) {
            surveyResponseRepository.saveAll(responses);
        }

        @Transactional
        public void closeSurvey(Long surveyId) {
            Survey survey = surveyRepository.findById(surveyId).orElseThrow();
            survey.setActive(false);
            surveyRepository.save(survey);

            List<SurveyResponse> responses = surveyResponseRepository.findBySurveyId(surveyId);

            aggregateResponses(survey, responses);
        }

        private List<SurveyQuestion> generateQuestions(Survey survey) {
            List<SurveyQuestion> questions = new ArrayList<>();

            switch(survey.getDomain()) {
                case POPULATION -> {
                    questions.add(SurveyQuestion.builder()
                            .text("Koliko osoba živi u vašem domaćinstvu?")
                            .type(QuestionType.NUMBER)
                            .survey(survey).build());
                    questions.add(SurveyQuestion.builder()
                            .text("Da li planirate proširenje porodice?")
                            .type(QuestionType.YES_NO)
                            .survey(survey).build());
                    questions.add(SurveyQuestion.builder()
                            .text("Koliko imate godina?")
                            .type(QuestionType.NUMBER)
                            .survey(survey).build());
                    questions.add(SurveyQuestion.builder()
                            .text("Da li ste imali smrtnih slučajeva u Vašem domaćinstvu ove godine?")
                            .type(QuestionType.YES_NO)
                            .survey(survey).build());
                }
                case GDP -> {
                    questions.add(SurveyQuestion.builder()
                            .text("Koliki je vaš lični prihod ove godine (u RSD)?")
                            .type(QuestionType.NUMBER)
                            .survey(survey).build());
                    questions.add(SurveyQuestion.builder()
                            .text("Očekujete li bolje ekonomsko stanje sledeće godine?")
                            .type(QuestionType.YES_NO)
                            .survey(survey).build());
                    questions.add(SurveyQuestion.builder()
                            .text("Da li osećate da su cene značajno porasle ove godine?")
                            .type(QuestionType.YES_NO)
                            .survey(survey).build());
                }
                case WAGE -> {
                    questions.add(SurveyQuestion.builder()
                            .text("Kolika je vaša trenutna plata (u RSD)?")
                            .type(QuestionType.NUMBER)
                            .survey(survey).build());
                    questions.add(SurveyQuestion.builder()
                            .text("Očekujete li povišicu sledeće godine?")
                            .type(QuestionType.YES_NO)
                            .survey(survey).build());
                }
                case TRAFFIC -> {
                    questions.add(SurveyQuestion.builder()
                            .text("Koliko registrovanih vozila imate?")
                            .type(QuestionType.NUMBER)
                            .survey(survey).build());
                    questions.add(SurveyQuestion.builder()
                            .text("Da li ste imali udes ove godine?")
                            .type(QuestionType.YES_NO)
                            .survey(survey).build());
                    questions.add(SurveyQuestion.builder()
                            .text("Ako ste imali udes, da li je bilo smrtnih slučajeva?")
                            .type(QuestionType.YES_NO)
                            .survey(survey).build());
                }
            }

            return questions;
        }

        private void aggregateResponses(Survey survey, List<SurveyResponse> responses) {
            switch(survey.getDomain()) {
                case POPULATION -> {
                    aggregatePopulation(responses, survey.getYear());
                }
                case TRAFFIC -> {
                    aggregateTraffic(responses, survey.getYear());
                }
                case GDP -> {
                    aggregateGdp(responses, survey.getYear());
                }
                case WAGE -> {
                    aggregateWage(responses, survey.getYear());
                }
            }
        }

        private void aggregateWage(List<SurveyResponse> responses, int year) {
            var salaries = responses.stream()
                    .filter(r -> r.getQuestion().getText().contains("plata"))
                    .collect(Collectors.groupingBy(SurveyResponse::getRegion,
                            Collectors.mapping(r -> Double.valueOf(r.getAnswer()), Collectors.toList())));

            salaries.forEach((region, values) -> {
                double avg = values.stream().mapToDouble(Double::doubleValue).average().orElse(0);

                long yesCount = responses.stream()
                        .filter(r -> r.getQuestion().getText().contains("povišicu"))
                        .filter(r -> r.getRegion().equals(region))
                        .filter(r -> r.getAnswer().equalsIgnoreCase("yes"))
                        .count();

                double growthExpect = (double) yesCount / (values.isEmpty() ? 1 : values.size()) * 100;

                wageStatRepository.save(new WageStat(null, region, year, avg, growthExpect));
            });
        }

        private void aggregateTraffic(List<SurveyResponse> responses, int year) {
            var vehicles = responses.stream()
                    .filter(r -> r.getQuestion().getText().toLowerCase().contains("vozila"))
                    .collect(Collectors.groupingBy(SurveyResponse::getRegion,
                            Collectors.mapping(r -> Long.valueOf(r.getAnswer()), Collectors.toList())));

            vehicles.forEach((region, values) -> {
                long totalVehicles = values.stream().mapToLong(Long::longValue).sum();

                long accidentCount = responses.stream()
                        .filter(r -> r.getQuestion().getText().toLowerCase().contains("udes"))
                        .filter(r -> r.getRegion().equals(region))
                        .filter(r -> r.getAnswer().equalsIgnoreCase("yes"))
                        .count();

                long fatalities = responses.stream()
                        .filter(r -> r.getQuestion().getText().toLowerCase().contains("smrtnih"))
                        .filter(r -> r.getRegion().equals(region))
                        .filter(r -> r.getAnswer().equalsIgnoreCase("yes"))
                        .count();

                trafficStatRepository.save(new TrafficStat(
                        null, region, year, totalVehicles, accidentCount, fatalities
                ));
            });
        }

        private void aggregatePopulation(List<SurveyResponse> responses, int year) {
            var hhByRegion = responses.stream()
                    .filter(r -> r.getQuestion().getText().toLowerCase(Locale.ROOT).contains("osoba"))
                    .collect(Collectors.groupingBy(
                            SurveyResponse::getRegion,
                            Collectors.mapping(r -> Long.parseLong(r.getAnswer()), Collectors.toList())
                    ));


            hhByRegion.forEach((region, hhSizes) -> {
                int householdsSample = hhSizes.size();
                long sumMembers = hhSizes.stream().mapToLong(Long::longValue).sum();
                double avgHhSize = householdsSample == 0 ? NATIONAL_AVG_HH_SIZE : (double) sumMembers / householdsSample;

                long planYes = responses.stream()
                        .filter(r -> r.getRegion().equals(region))
                        .filter(r -> r.getQuestion().getText().toLowerCase(Locale.ROOT).contains("proširenje"))
                        .filter(r -> "yes".equalsIgnoreCase(r.getAnswer()))
                        .count();

                double estimatedBirthRatePercentage = householdsSample == 0 ? 0.0 : (double) planYes / householdsSample;

                var ages = responses.stream()
                        .filter(r -> r.getRegion().equals(region))
                        .filter(r -> r.getQuestion().getText().toLowerCase(Locale.ROOT).contains("godina"))
                        .map(r -> Double.parseDouble(r.getAnswer()))
                        .toList();

                double avgAge = ages.isEmpty() ? 0.0 : ages.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);

                long mortalityYes = responses.stream()
                        .filter(r -> r.getRegion().equals(region))
                        .filter(r -> r.getQuestion().getText().toLowerCase(Locale.ROOT).contains("smrtni"))
                        .filter(r -> "yes".equalsIgnoreCase(r.getAnswer()))
                        .count();

                long mortalityAnswers = responses.stream()
                        .filter(r -> r.getRegion().equals(region))
                        .filter(r -> r.getQuestion().getText().toLowerCase(Locale.ROOT).contains("smrtni"))
                        .count();

                double estimatedMortalityRatePerCapita = mortalityAnswers == 0 ? 0.0 : (double) mortalityYes / mortalityAnswers;
/*
                Za broj stanovnika po regionu radimo sampling, baziramo se na proslogodisnjoj statistici i primenjujemo vrednosti iz ovogodisnje
                ankete za projekciju broja stanovnika.
                Sto je veci broj zapisa to ce biti precizniji rezultat. https://en.wikipedia.org/wiki/Law_of_large_numbers

                Ovo moze dati relativno nerealne projekcije, ali sve zavisi od broja zapisa. Sto je veci broj odgovora na anketu dobijamo
                realnije odgovore
*/
                Long prevPopulation = populationStatRepository.findByRegionAndYear(region, year - 1)
                        .map(PopulationStat::getPopulation)
                        .orElse(null);

                long estimatedPopulation;
                if (prevPopulation != null) {
                    double householdsPrevYear = prevPopulation / NATIONAL_AVG_HH_SIZE;
                    estimatedPopulation = Math.round(avgHhSize * householdsPrevYear);
                } else {
                    final int EXPANSION_HOUSEHOLDS = 10_000;
                    estimatedPopulation = Math.round(EXPANSION_HOUSEHOLDS * avgHhSize);
                }

                PopulationStat stat = PopulationStat.builder()
                        .region(region)
                        .year(year)
                        .population(estimatedPopulation)
                        .averageAge(avgAge)
                        .birthRate(estimatedBirthRatePercentage)
                        .mortalityRate(estimatedMortalityRatePerCapita)
                        .build();

                populationStatRepository.save(stat);
            });
        }

        /* Slicno kao kod population agregacije koristimo sampling, u slucaju da je broj respondenta na anketi mali
        * gdpInBillion ce biti mali (u odnosu na proslu godinu ispada da nema para), i growth percentage ce biti los i CPI
        * ovde narocito vazi law of large numbers, sto je veci sample to je bolja predikcija gdp-a
        * */
        private void aggregateGdp(List<SurveyResponse> responses, int year) {
            var grouped = responses.stream()
                    .filter(r -> r.getQuestion().getText().toLowerCase().contains("prihod"))
                    .collect(Collectors.groupingBy(SurveyResponse::getRegion,
                            Collectors.mapping(r -> Double.valueOf(r.getAnswer()), Collectors.toList())));

            grouped.forEach((region, values) -> {
                double avgIncome = values.stream().mapToDouble(Double::doubleValue).average().orElse(0);

                var lastYear = gdpStatRepository.findTopByRegionAndYearOrderByYearDesc(region, year - 1);
                double newGdpBillion;
                double realGrowth = 0.0;

                if (lastYear != null && lastYear.getGdpBillion() != null) {
                    double lastYearBillion = lastYear.getGdpBillion();
                    newGdpBillion = avgIncome / BILLION;
                    realGrowth = (newGdpBillion / lastYearBillion - 1) * 100;
                } else {
                    // Fallback
                    newGdpBillion = avgIncome / BILLION;
                }

                long optimisticCount = responses.stream()
                        .filter(r -> r.getQuestion().getText().toLowerCase().contains("ekonomsko stanje"))
                        .filter(r -> r.getRegion().equals(region))
                        .filter(r -> r.getAnswer().equalsIgnoreCase("yes"))
                        .count();
                double optimismRate = (double) optimisticCount / (values.isEmpty() ? 1 : values.size()) * 100;

                double perceptionGrowth = (optimismRate - 50) / 10.0;
                double growthPercent = 0.7 * realGrowth + 0.3 * perceptionGrowth;

                long inflationCount = responses.stream()
                        .filter(r -> r.getQuestion().getText().toLowerCase().contains("cene"))
                        .filter(r -> r.getRegion().equals(region))
                        .filter(r -> r.getAnswer().equalsIgnoreCase("yes"))
                        .count();
                double perceivedCpi = (double) inflationCount / (values.isEmpty() ? 1 : values.size()) * 100;

                double cpiPercent = lastYear != null && lastYear.getCpiPercent() != null
                        ? 0.5 * lastYear.getCpiPercent() + 0.5 * perceivedCpi
                        : perceivedCpi;

                gdpStatRepository.save(new GdpStat(
                        null, region, year, newGdpBillion, growthPercent, cpiPercent
                ));
            });
        }

}
