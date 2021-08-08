package com.home.mapping;

import com.home.model.Book;
import com.home.textextractor.resolver.TextExtractorResolver;
import lombok.experimental.UtilityClass;

import java.nio.file.Path;

import static com.home.model.ImageFormat.jpeg;
import static org.apache.commons.io.FilenameUtils.getExtension;
import static org.apache.commons.io.FilenameUtils.removeExtension;

@UtilityClass
public class BookBuilder {
    public static Book of(final Path path) {
        var filename = path.getFileName().toString();
        return Book.builder()
                .name(removeExtension(filename))
                .format(getExtension(filename))
                .text(TextExtractorResolver
                        .resolveBySourceFileType(getExtension(filename))
                        .extract(path.toString(), 2, 2))
                .cover(CoverBuilder.of(path.toString(), jpeg.toString()))
                .build();
    }
}
