package com.calculator.cptcode.service;

import com.calculator.cptcode.entity.NhsIllnessesCategorized;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MdmLevelCalculator {

    public static String calculateFinalMdmLevel(
            List<NhsIllnessesCategorized> problemAddressed,
            List<NhsIllnessesCategorized> dataReviewed,
            List<NhsIllnessesCategorized> riskOfComplication
    ) {
        String paLevel = getHighestLevel(problemAddressed);
        String drLevel = getHighestLevel(dataReviewed);
        String rcLevel = getHighestLevel(riskOfComplication);

        String finalMdmLevel = determineFinalMdmLevel(paLevel, drLevel, rcLevel);

//        return String.format(
//                "{\n" +
//                        "  \"problemAddressedLevel\": \"%s\",\n" +
//                        "  \"dataReviewedLevel\": \"%s\",\n" +
//                        "  \"riskOfComplicationLevel\": \"%s\",\n" +
//                        "  \"finalMDMLevel\": \"%s\"\n" +
//                        "}", paLevel, drLevel, rcLevel, finalMdmLevel
//        );
        return finalMdmLevel;
    }

    private static String getHighestLevel(List<NhsIllnessesCategorized> list) {
        if (list == null || list.isEmpty()) return ""; // Default if no data

        Map<String, Integer> priority = Map.of(
                "Minimal", 1,
                "Low", 2,
                "Moderate", 3,
                "High", 4
        );

        return list.stream()
                .map(NhsIllnessesCategorized::getStatus)
                .filter(priority::containsKey)
                .max(Comparator.comparingInt(priority::get))
                .orElse("Minimal");
    }

    private static String determineFinalMdmLevel(String pa, String dr, String rc) {
        Map<String, Integer> priority = Map.of(
                "Minimal", 1,
                "Low", 2,
                "Moderate", 3,
                "High", 4
        );

        List<String> levels = new ArrayList<>();

        if (!pa.isEmpty() && !dr.isEmpty() && !rc.isEmpty()) {
            levels = List.of(pa, dr, rc);
        } else if (!pa.isEmpty() && !dr.isEmpty()) {
            levels = List.of(pa, dr);
        } else if (!pa.isEmpty() && !rc.isEmpty()) {
            levels = List.of(pa, rc);
        } else if (!dr.isEmpty() && !rc.isEmpty()) {
            levels = List.of(dr, rc);
        } else if (!pa.isEmpty()) {
            levels = List.of(pa);
        } else if (!dr.isEmpty()) {
            levels = List.of(dr);
        } else if (!rc.isEmpty()) {
            levels = List.of(rc);
        } else {
            levels = List.of(); // All are empty
        }


        // Count frequencies of each level
        Map<String, Long> countMap = levels.stream()
                .collect(Collectors.groupingBy(s -> s, Collectors.counting()));

        // Iterate from highest to lowest and return the first level that occurs at least twice
        return List.of("High", "Moderate", "Low", "Minimal").stream()
                .filter(level -> countMap.getOrDefault(level, 0L) >= 2)
                .findFirst()
                .orElse(
                        // If no level appears at least twice, return the second-highest level
                        levels.stream()
                                .max(Comparator.comparingInt(priority::get))
                                .orElse("Minimal")
                );
    }
}
