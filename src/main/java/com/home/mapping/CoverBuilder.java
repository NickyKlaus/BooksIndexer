package com.home.mapping;

import com.home.imagegenerator.resolver.ImageGeneratorResolver;
import com.home.model.Cover;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.apache.commons.io.FilenameUtils.getExtension;

public final class CoverBuilder {
    private static final Logger LOG = LoggerFactory.getLogger(CoverBuilder.class);

    private CoverBuilder() {
        LOG.error(getClass() + " is a utility class and cannot be instantiated");
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
