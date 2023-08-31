package com.example.social.utils;

import java.util.ArrayList;
import java.util.List;

public class StringFormatter {

    public static String formatUserString(String input) {
        if (input != null && input.contains("com")) {
            return input.replace("com", ".com");
        }
        return input;
    }
    public static List<String> formatUserStrings(List<String> inputs) {
        List<String> formattedList = new ArrayList<>();
        for (String input : inputs) {
            String formattedInput = formatUserString(input);
            formattedList.add(formattedInput);
        }
        return formattedList;
    }
}
