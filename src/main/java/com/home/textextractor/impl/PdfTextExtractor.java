package com.home.textextractor.impl;

import com.home.textextractor.DocumentTextExtractor;
import org.apache.pdfbox.io.RandomAccessBufferedFileInputStream;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Paths;

import static com.home.model.FileType.pdf;
import static org.apache.commons.io.FilenameUtils.getExtension;

public class PdfTextExtractor implements DocumentTextExtractor {
    private static final Logger LOG = LoggerFactory.getLogger(PdfTextExtractor.class);

    @Override
    public String extract(String fullyQualifiedSourceFilename, int startPage, int endPage) {
        assert pdf.toString().equalsIgnoreCase(getExtension(fullyQualifiedSourceFilename));
        assert (startPage > 0 && endPage > 0 && startPage <= endPage);

        Throwable primaryException = null;
        try (
                var stream = new RandomAccessBufferedFileInputStream(Paths.get(fullyQualifiedSourceFilename).toFile())
        ) {
            var parser = new PDFParser(stream);
            parser.parse();
            PDDocument document = null;
            PDFTextStripper stripper;

            try (var cosDocument = parser.getDocument()) {
                stripper = new PDFTextStripper();
                document = new PDDocument(cosDocument);
                stripper.setStartPage(startPage);
                stripper.setEndPage(endPage);
                return stripper.getText(document);
            } catch (IOException ioe) {
                primaryException = ioe;
                LOG.error("Error: ", primaryException);
            } finally {
                if (document != null) {
                    try {
                        document.close();
                    } catch (IOException ex) {
                        if (primaryException != null) {
                            primaryException.addSuppressed(ex);
                            LOG.error("Error: ", primaryException);
                        } else {
                            LOG.error("Error: ", ex);
                        }
                    }
                }
            }
        } catch (IOException e) {
            LOG.error("Error: ", e);
        }
        return "";
    }
}
