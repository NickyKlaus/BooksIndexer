package com.home.textextractor.impl;

import com.home.textextractor.DocumentTextExtractor;
import org.apache.pdfbox.io.RandomAccessBufferedFileInputStream;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.IOException;
import java.nio.file.Paths;

import static com.home.model.FileType.pdf;
import static org.apache.commons.io.FilenameUtils.getExtension;

public class PdfTextExtractor implements DocumentTextExtractor {
    @Override
    public String extract(String fullyQualifiedSourceFilename, int startPage, int endPage) {
        assert pdf.toString().equalsIgnoreCase(getExtension(fullyQualifiedSourceFilename));
        assert (startPage > 0 && endPage > 0 && startPage <= endPage);

        PDFTextStripper stripper;
        PDDocument document;

        try (
                var stream = new RandomAccessBufferedFileInputStream(Paths.get(fullyQualifiedSourceFilename).toFile())
        ) {
            var parser = new PDFParser(stream);
            parser.parse();

            try (var cosDocument = parser.getDocument()) {
                stripper = new PDFTextStripper();
                document = new PDDocument(cosDocument);
                stripper.setStartPage(startPage);
                stripper.setEndPage(endPage);
                return stripper.getText(document);
            }
        } catch (IOException e) {
            return "";
        }
    }
}
