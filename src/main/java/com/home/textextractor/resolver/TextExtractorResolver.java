package com.home.textextractor.resolver;

import com.home.model.FileType;
import com.home.textextractor.DocumentTextExtractor;
import com.home.textextractor.impl.DjvuTextExtractor;
import com.home.textextractor.impl.EmptyTextExtractor;
import com.home.textextractor.impl.PdfTextExtractor;
import lombok.experimental.UtilityClass;

@UtilityClass
public class TextExtractorResolver {
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
