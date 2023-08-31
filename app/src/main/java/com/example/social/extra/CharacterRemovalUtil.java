package com.example.social.extra;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CharacterRemovalUtil {

    public static String removeCharacters(String input) {
        // Define the regex pattern
        String pattern = "[.#$\\[\\]]";

        // Create a Pattern object
        Pattern regex = Pattern.compile(pattern);

        // Use Matcher to find and replace characters
        Matcher matcher = regex.matcher(input);
        return matcher.replaceAll("");
    }
}
