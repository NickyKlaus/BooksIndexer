package com.home.mapping;

import com.home.imagegenerator.resolver.ImageGeneratorResolver;
import com.home.model.Cover;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import static org.apache.commons.io.FilenameUtils.getExtension;

@Mapper
public interface CoverMapper {

    Cover toCover(final String fullyQualifiedFilename,final String imageFormat);

    @AfterMapping
    default void generateCover(final String fullyQualifiedFilename,
                               final String imageFormat,
                               @MappingTarget final Cover cover) {
        cover.setFormat(imageFormat);
        cover.setBinaryData(generateBookCoverImage(fullyQualifiedFilename, imageFormat));
    }

    private byte[] generateBookCoverImage(final String fullyQualifiedFilename, final String imageFormat) {
        return ImageGeneratorResolver
                .resolveBySourceFileType(getExtension(fullyQualifiedFilename))
                .generate(fullyQualifiedFilename, imageFormat);
    }
}
