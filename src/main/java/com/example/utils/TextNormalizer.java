package com.example.utils;

import java.text.Normalizer;

public class TextNormalizer {

    private TextNormalizer() {
    }

    public static String normalize(String text) {

        if (text == null) {
            return "";
        }

        String trimmed = text.trim().toLowerCase().replaceAll("\\s+", " ");

        String withoutAccents = Normalizer.normalize(trimmed, Normalizer.Form.NFD);

        return withoutAccents.replaceAll("\\p{M}", "");
    }
}