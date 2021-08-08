package com.home.imagegenerator.impl;

import com.home.imagegenerator.ImageGenerator;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.imageio.ImageIO;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static com.home.model.FileType.pdf;
import static java.nio.file.Files.readAllBytes;
import static java.nio.file.Path.of;
import static org.apache.commons.io.FilenameUtils.getExtension;
import static org.apache.pdfbox.pdmodel.PDDocument.load;

public class PdfImageGenerator implements ImageGenerator {

    @Override
    public byte[] generate(final String fullyQualifiedSourceFilename, final String targetImageFormat) {
        assert pdf.toString().equalsIgnoreCase(getExtension(fullyQualifiedSourceFilename));

        try (ByteArrayOutputStream imageOutputStream = new ByteArrayOutputStream();
             PDDocument document = load(readAllBytes(of(fullyQualifiedSourceFilename)))) {
            ImageIO.write(
                    new PDFRenderer(document)
                            .renderImageWithDPI(0, TARGET_IMAGE_DPI, ImageType.RGB),
                    targetImageFormat,
                    imageOutputStream);
            return imageOutputStream.toByteArray();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return new byte[0];
    }
}
