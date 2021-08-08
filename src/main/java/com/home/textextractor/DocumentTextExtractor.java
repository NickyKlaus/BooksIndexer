package com.home.textextractor;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

@FunctionalInterface
public interface DocumentTextExtractor {
    Charset DEFAULT_TEXT_CHARSET = StandardCharsets.UTF_8;

    String extract(final String fullyQualifiedSourceFilename, final int startPage, final int endPage);
}
