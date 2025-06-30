package com.calculator.cptcode.service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TimeBasedCptCode {
    public static String getTimeBasedCptCode(String patientType, String text) {
        text = text.toLowerCase();

        // Look for time format like "time spent: 25 minutes" or "total time: 40 min"
        Pattern pattern = Pattern.compile("(time spent|total time)[^\\d]*(\\d{1,3})\\s*(minutes|min)?");
        Matcher matcher = pattern.matcher(text);

        if (matcher.find()) {
            int minutes = Integer.parseInt(matcher.group(2));

            if (patientType.equals("new")) {
                if (minutes >= 15 && minutes <= 29) return "99202";
                else if (minutes >= 30 && minutes <= 44) return "99203";
                else if (minutes >= 45 && minutes <= 59) return "99204";
                else if (minutes >= 60) return "99205";
            } else { // established
                if (minutes >= 10 && minutes <= 19) return "99212";
                else if (minutes >= 20 && minutes <= 29) return "99213";
                else if (minutes >= 30 && minutes <= 39) return "99214";
                else if (minutes >= 40) return "99215";
            }
        }

        return null; // No valid time found
    }

}

