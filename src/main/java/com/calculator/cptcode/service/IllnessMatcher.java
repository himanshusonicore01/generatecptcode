package com.calculator.cptcode.service;

import com.calculator.cptcode.entity.IllnessRelatedWords;
import com.calculator.cptcode.entity.NhsIllnessesCategorized;
import com.calculator.cptcode.repository.IllnessRelatedWordsRepository;
import com.calculator.cptcode.repository.NhsIllnessesCategorizedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class IllnessMatcher {

    @Autowired
    private IllnessRelatedWordsRepository illnessRelatedWordsRepository;

    @Autowired
    private NhsIllnessesCategorizedRepository nhsIllnessesCategorizedRepository;

    public Map<String, Object> matchesRelatedWord(String text) {

        List<IllnessRelatedWords> illnessData = illnessRelatedWordsRepository.findAllData();

        Map<String,Object> matchRelateData = new HashMap<>();
        List<String> illnessRelatedName = new ArrayList<>();
        List<String> JoinCategoryStatus = new ArrayList<>();

        // 3. Search for related keywords
        List<String> negations = Arrays.asList("no", "not", "denies", "denied", "without", "negative for");
        List<String> alreadyIllness = new ArrayList<>();

        // Use more intelligent splitting: split by section headers or newline if followed by ALL CAPS and colon (like "NEUROLOGICAL:")

        // Split by section header (e.g., "NEUROLOGIC:") using regex that also works at beginning of string
        String[] sections = text.split("(?=\\b[A-Z ]+:)");


        for (IllnessRelatedWords illness : illnessData) {
            String[] keywords = illness.getRelated_words().split(",");

            for (String keyword : keywords) {
                String keywordTrimmed = keyword.trim().toLowerCase();

                if (!alreadyIllness.contains(keywordTrimmed)) {
                    for (String section : sections) {
                        String lowerSection = section.toLowerCase();

                        Pattern pattern = Pattern.compile("\\b" + Pattern.quote(keywordTrimmed) + "\\b");
                        Matcher matcher = pattern.matcher(lowerSection);

                        if (matcher.find()) {
                            // STEP 1: Check if the section contains a negation before the keyword list
                            boolean isNegated = false;

                            // Try to detect negation if it appears before symptom list
                            int keywordIndex = lowerSection.indexOf(keywordTrimmed);
                            int negationIndex = Integer.MAX_VALUE;

                            for (String neg : negations) {
                                int idx = lowerSection.indexOf(neg);
                                if (idx != -1 && idx < keywordIndex && idx < negationIndex) {
                                    negationIndex = idx;
                                    isNegated = true;
                                }
                            }

                            if (!isNegated) {
                                alreadyIllness.add(keywordTrimmed);
                                System.out.println("Match Found:");
                                System.out.println("Illness: " + illness.getIllness());
                                System.out.println("JoinCategoryStatus: " + illness.getJoin_category_status());
                                JoinCategoryStatus.add(illness.getJoin_category_status());
                                illnessRelatedName.add(keywordTrimmed);
                            }

                            break; // No need to check further sections
                        }
                    }
                }
            }
        }



        matchRelateData.put("joinCategoryStatus", JoinCategoryStatus);
        matchRelateData.put("illnessRelatedName", illnessRelatedName);
        return matchRelateData;
    }

    public List<NhsIllnessesCategorized> illnessCategoryMatches(List<String> illnessMatchKey) {

        return nhsIllnessesCategorizedRepository.findByJoinRelatedWord(illnessMatchKey);
    }
}
