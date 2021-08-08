package com.home.mapping;

import com.home.imagegenerator.resolver.ImageGeneratorResolver;
import com.home.model.Cover;
import lombok.experimental.UtilityClass;

import static org.apache.commons.io.FilenameUtils.getExtension;

@UtilityClass
public class CoverBuilder {
    public static Cover of(final String fullyQualifiedFilename, final String imageFormat) {
        if ( fullyQualifiedFilename == null && imageFormat == null ) {
            return null;
        }

        return Cover.builder()
                .format(imageFormat)
                .image(generateBookCoverImage(fullyQualifiedFilename, imageFormat))
                .build();
    }

    private byte[] generateBookCoverImage(final String fullyQualifiedFilename, final String imageFormat) {
        return ImageGeneratorResolver
                .resolveBySourceFileType(getExtension(fullyQualifiedFilename))
                .generate(fullyQualifiedFilename, imageFormat);
    }
}
