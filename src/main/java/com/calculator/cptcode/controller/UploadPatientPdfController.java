package com.calculator.cptcode.controller;

import com.calculator.cptcode.entity.NhsIllnessesCategorized;
import com.calculator.cptcode.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@RequestMapping("/use_database")
@RestController
public class UploadPatientPdfController {

    @Autowired
    private IllnessMatcher illnessMatcher;

    @Autowired
    private TestBaseCptCode testBaseCptCode;

    @PostMapping("/pdf_upload")
    public Map<String,Object> fileUpload(@RequestParam("file") MultipartFile file) throws IOException {
        String text = ReadPatientPdf.extractText(file);

        text = text.toLowerCase();

        String patientType = PatientType.getPatientType(text);

        Map<String,Object> matchesRelatedWord = illnessMatcher.matchesRelatedWord(text);

        List<String> illnessMatchKey = (List<String>) matchesRelatedWord.get("joinCategoryStatus");

        List<NhsIllnessesCategorized> illnessWithCategorizedAndStatus = illnessMatcher.illnessCategoryMatches(illnessMatchKey);

        List<NhsIllnessesCategorized> problemAddressed = illnessWithCategorizedAndStatus.stream().filter(e -> e.getCategory().equalsIgnoreCase("Problem Addressed")).collect(Collectors.toList());
        List<NhsIllnessesCategorized> dataReviewed = illnessWithCategorizedAndStatus.stream().filter(e -> e.getCategory().equalsIgnoreCase("Data Reviewed")).collect(Collectors.toList());
        List<NhsIllnessesCategorized> riskOfComplication = illnessWithCategorizedAndStatus.stream().filter(e -> e.getCategory().equalsIgnoreCase("Risk of Complication")).collect(Collectors.toList());

        Map<String,Object> illnessDetails = new HashMap<>();

        illnessDetails.put("problemAddress", problemAddressed);
        illnessDetails.put("dataReviewed", dataReviewed);
        illnessDetails.put("riskOfComplication", riskOfComplication);
        illnessDetails.put("totalRelatedWords", matchesRelatedWord.get("illnessRelatedName"));

        String mdmLevel = MdmLevelCalculator.calculateFinalMdmLevel(problemAddressed, dataReviewed, riskOfComplication);

        String cptCode = CptCodeResolver.getCptCode(mdmLevel, patientType);
        String timeCptCode = TimeBasedCptCode.getTimeBasedCptCode(patientType, text);
        Map<String,Object> testCptCodes = testBaseCptCode.getAllMedicalTest(text);

        Map<String,Object> cptCodes = new HashMap<>();
        cptCodes.put("patient_type", patientType);
        cptCodes.put("mdm_level", mdmLevel);
        cptCodes.put("mdm_based_cpt", cptCode);
        if (timeCptCode != null) {
            cptCodes.put("time_based_cpt", timeCptCode);
        }
        if (testCptCodes != null) {
            cptCodes.put("test_based_cpt", testCptCodes);
        }
        cptCodes.putAll(illnessDetails);
        return cptCodes;
    }
}
