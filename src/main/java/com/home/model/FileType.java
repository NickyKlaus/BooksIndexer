package com.home.model;

import java.util.Arrays;

public enum FileType {
    pdf, djvu, djv,;

    public static boolean contains(final String type) {
        return Arrays.stream(FileType.values())
                .map(FileType::toString)
                .anyMatch(type::equalsIgnoreCase);
    }
}