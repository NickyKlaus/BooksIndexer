package com.home.imagegenerator.impl;

import com.home.imagegenerator.ImageGenerator;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.imageio.ImageIO;
import java.io.*;

import static com.home.model.FileType.pdf;
import static org.apache.commons.io.FilenameUtils.getExtension;
import static org.apache.pdfbox.pdmodel.PDDocument.load;

public class PdfImageGenerator implements ImageGenerator {

    @Override
    public byte[] generate(final String fullyQualifiedSourceFilename, final String targetImageFormat) {
        assert pdf.toString().equalsIgnoreCase(getExtension(fullyQualifiedSourceFilename));

        var image = new byte[0];
        PDDocument document = null;
        try (
                var inputStream = new FileInputStream(fullyQualifiedSourceFilename);
                var bufferedInputStream = new BufferedInputStream(inputStream);
                var outputStream = new ByteArrayOutputStream();
                var bufferedOutputStream = new BufferedOutputStream(outputStream)
        ) {
            document = load(bufferedInputStream);
            ImageIO.write(
                    new PDFRenderer(document)
                            .renderImageWithDPI(0, TARGET_IMAGE_DPI, ImageType.RGB),
                    targetImageFormat,
                    bufferedOutputStream);
            bufferedOutputStream.flush();
            image = outputStream.toByteArray();
        } catch (IOException e) {
            return image;
        } finally {
            if (document != null) {
                try {
                    document.close();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        }
        return image;
    }
}
