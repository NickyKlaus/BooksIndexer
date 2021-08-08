package com.home.textextractor.impl;

import com.home.textextractor.DocumentTextExtractor;

public class EmptyTextExtractor implements DocumentTextExtractor {
    @Override
    public String extract(String fullyQualifiedSourceFilename, int startPage, int endPage) {
        return "";
    }
}
