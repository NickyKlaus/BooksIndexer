package com.home.imagegenerator;

@FunctionalInterface
public interface ImageGenerator {
    float TARGET_IMAGE_DPI = 72f;

    byte[] generate(final String fullyQualifiedSourceFilename, final String targetImageFormat);
}
