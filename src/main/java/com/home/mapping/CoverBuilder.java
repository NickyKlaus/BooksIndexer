package com.home.mapping;

import com.home.imagegenerator.resolver.ImageGeneratorResolver;
import com.home.model.Cover;

import static org.apache.commons.io.FilenameUtils.getExtension;

public final class CoverBuilder {
    private CoverBuilder() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static Cover of(final String fullyQualifiedFilename, final String imageFormat) {
        if ( fullyQualifiedFilename == null || imageFormat == null ) {
            return null;
        }
        return Cover.builder()
                .format(imageFormat)
                .image(generateBookCoverImage(fullyQualifiedFilename, imageFormat))
                .build();
    }

    private static byte[] generateBookCoverImage(final String fullyQualifiedFilename, final String imageFormat) {
        return ImageGeneratorResolver
                .resolveBySourceFileType(getExtension(fullyQualifiedFilename))
                .generate(fullyQualifiedFilename, imageFormat);
    }
}
