package com.calculator.cptcode.service;

import com.calculator.cptcode.entity.MedicalTest;
import com.calculator.cptcode.repository.MedicalTestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class TestBaseCptCode {

    @Autowired
    private MedicalTestRepository medicalTestRepository;

    public Set<String> getAllMedicalTest(String text) {
        List<MedicalTest> medicalTestList = medicalTestRepository.getAllMedicalTest();

        Set<String> testCptCodes = new HashSet<>();

        // 3. Search for related keywords
        List<String> negations = Arrays.asList("no", "not", "denies", "denied", "without", "negative for");
        List<String> alreadyTest = new ArrayList<>();

        // Use more intelligent splitting: split by section headers or newline if followed by ALL CAPS and colon (like "NEUROLOGICAL:")

        // Split by section header (e.g., "NEUROLOGIC:") using regex that also works at beginning of string
        String[] sections = text.split("(?=\\b[A-Z ]+:)");


        for (MedicalTest test : medicalTestList) {
            String[] keywords = test.getRelated_words().split(",");

            for (String keyword : keywords) {
                String keywordTrimmed = keyword.trim().toLowerCase();

                if (!alreadyTest.contains(keywordTrimmed)) {
                    for (String section : sections) {
                        String lowerSection = section.toLowerCase();

                        keywordTrimmed = " "+keywordTrimmed+" ";
                        Pattern pattern = Pattern.compile(Pattern.quote(keywordTrimmed));
                        Matcher matcher = pattern.matcher(lowerSection);

                        if (matcher.find()) {
                            boolean isNegated = false;

                            // Lowercase the section for consistent matching
                            String lowerSection1 = section.toLowerCase();
                            String keywordTrimmed1 = keyword.trim().toLowerCase();

                            // STEP 1: Find where the keyword appears
                            int keywordIndex = lowerSection1.indexOf(keywordTrimmed1);

                            // STEP 2: Only proceed if keyword exists
                            if (keywordIndex != -1) {
                                // Take a substring of 0 to keywordIndex (context before keyword)
                                String contextBeforeKeyword = lowerSection1.substring(0, keywordIndex);

                                // STEP 3: Check for negation in that context
                                for (String neg : negations) {
                                    if (contextBeforeKeyword.matches(".*\\b" + neg + "\\b.*")) {
                                        isNegated = true;
                                        break;
                                    }
                                }
                            }

                            // STEP 4: Only proceed if not negated
                            if (!isNegated) {
                                alreadyTest.add(keywordTrimmed);
                                System.out.println("Match Found:");
                                System.out.println("Test Name: " + test.getName());
                                System.out.println("Test CPT Codes: " + test.getCpt_code());
                                if (test.getCpt_code() != null) {
                                    testCptCodes.add(test.getCpt_code());
                                }
                            }

                            break; // No need to check further sections
                        }
                    }
                }
            }
        }
        return testCptCodes;
    }


}
