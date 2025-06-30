package com.calculator.cptcode.service;

public class CptCodeResolver {

    public static String getCptCode(String mdmLevel, String patientType) {
        if (mdmLevel == null || patientType == null) return "Invalid input";

        String normalizedMdm = mdmLevel.trim().toLowerCase();
        String normalizedType = patientType.trim().toLowerCase();

        switch (normalizedType) {
            case "new":
                return switch (normalizedMdm) {
                    case "minimal" -> "99202";
                    case "low"     -> "99203";
                    case "moderate"-> "99204";
                    case "high"    -> "99205";
                    default        -> "Unknown MDM Level";
                };
            case "established":
                return switch (normalizedMdm) {
                    case "minimal" -> "99211";
                    case "low"     -> "99212";
                    case "moderate"-> "99214";
                    case "high"    -> "99215";
                    default        -> "Unknown MDM Level";
                };
            default:
                return "Unknown Patient Type";
        }
    }
}
