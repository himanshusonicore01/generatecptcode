package com.calculator.cptcode.service;

import org.springframework.stereotype.Service;

@Service
public class PatientType {

    public static String getPatientType(String text) {

        String patientType;

        if (isEstablishedPatient(text)) {
            patientType = "Established";
        } else {
            patientType = "New";
        }
        return patientType;
    }

    public static boolean isEstablishedPatient(String text) {
        text = text.toLowerCase();

        // Exclude new patient indicators
        if (text.contains("first visit") ||
                text.contains("new patient") ||
                text.contains("initial consult") ||
                text.contains("initial evaluation") ||
                text.contains("first time seeing") ||
                text.contains("initial history") ||
                text.contains("intake form")) {
            return false;
        }

        // Positive indicators of established patient
        boolean hasChartNumber = text.contains("chart:") || text.contains("chart number");
        boolean hasPastMedicalHistory = text.contains("medical hx") ||
                text.contains("past medical history") ||
                text.contains("surgical history") ||
                text.contains("past surgical history") ||
                text.contains("family history");
        boolean hasMedicationHistory = text.contains("current medication") || text.contains("medications & allergies");
        boolean hasVitals = text.contains("vitals") ||
                text.contains("temperature") ||
                text.contains("blood pressure") ||
                text.contains("oxygen saturation");
        boolean hasProviderFollowUp = text.contains("follow-up") || text.contains("return visit") || text.contains("last visit") || text.contains("seen previously");
        boolean hasPastVisitClues = text.contains("reviewed") || text.contains("history") || text.contains("bmi");


        // Return true if any two of these positive indicators are present
        int score = 0;
        if (hasChartNumber) score++;
        if (hasPastMedicalHistory) score++;
        if (hasMedicationHistory) score++;
        if (hasVitals) score++;
        if (hasProviderFollowUp) score++;
        if (hasPastVisitClues) score++;

        return score >= 2;
    }

}

