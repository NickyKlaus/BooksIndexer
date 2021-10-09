package com.home.textextractor.resolver;

import com.home.model.FileType;
import com.home.textextractor.DocumentTextExtractor;
import com.home.textextractor.impl.DjvuTextExtractor;
import com.home.textextractor.impl.EmptyTextExtractor;
import com.home.textextractor.impl.PdfTextExtractor;

public class TextExtractorResolver {
    private TextExtractorResolver() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static DocumentTextExtractor resolveBySourceFileType(final String type) {
        switch (FileType.valueOf(type.toLowerCase())) {
            case pdf:
                return new PdfTextExtractor();
            case djvu:
            case djv:
                return new DjvuTextExtractor();
            default:
                return new EmptyTextExtractor();
        }
    }
}
