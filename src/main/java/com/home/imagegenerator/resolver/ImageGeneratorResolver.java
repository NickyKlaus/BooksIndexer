package com.home.imagegenerator.resolver;

import com.home.imagegenerator.ImageGenerator;
import com.home.imagegenerator.impl.DJVUImageGenerator;
import com.home.imagegenerator.impl.EmptyImageGenerator;
import com.home.imagegenerator.impl.PDFImageGenerator;
import com.home.model.FileType;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ImageGeneratorResolver {
    public static ImageGenerator resolveBySourceFileType(final String type) {
        switch (FileType.valueOf(type.toLowerCase())) {
            case pdf:
                return new PDFImageGenerator();
            case djvu:
            case djv:
                return new DJVUImageGenerator();
            default:
                return new EmptyImageGenerator();
        }
    }
}
