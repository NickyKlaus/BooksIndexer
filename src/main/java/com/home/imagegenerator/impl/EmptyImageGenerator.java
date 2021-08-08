package com.home.imagegenerator.impl;

import com.home.imagegenerator.ImageGenerator;

public class EmptyImageGenerator implements ImageGenerator {

    @Override
    public byte[] generate(final String fullyQualifiedSourceFilename, final String targetImageFormat) {
        return new byte[0];
    }
}
