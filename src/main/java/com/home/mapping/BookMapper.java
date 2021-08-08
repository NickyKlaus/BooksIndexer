package com.home.mapping;

import com.home.model.Book;

import java.nio.file.Path;

import static com.home.model.ImageFormat.jpeg;
import static org.apache.commons.io.FilenameUtils.getExtension;
import static org.apache.commons.io.FilenameUtils.removeExtension;

public interface BookMapper {

    static Book toBook(final Path path) {
        var filename = path.getFileName().toString();
        return Book.builder()
                .name(removeExtension(filename))
                .format(getExtension(filename))
                .cover(new com.home.mapping.CoverMapperImpl()
                        .toCover(path.toString(), jpeg.toString()))
                .build();
    }
}
