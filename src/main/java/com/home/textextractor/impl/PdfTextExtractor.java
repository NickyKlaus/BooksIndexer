package com.home.textextractor.impl;

import com.home.textextractor.DocumentTextExtractor;
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.io.RandomAccessBufferedFileInputStream;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static com.home.model.FileType.pdf;
import static org.apache.commons.io.FilenameUtils.getExtension;

public class PdfTextExtractor implements DocumentTextExtractor {
    @Override
    public String extract(String fullyQualifiedSourceFilename, int startPage, int endPage) {
        assert pdf.toString().equalsIgnoreCase(getExtension(fullyQualifiedSourceFilename));
        assert (startPage > 0 && endPage > 0 && startPage <= endPage);

        PDFTextStripper stripper;
        PDDocument document;
        File file = new File(fullyQualifiedSourceFilename);

        try (RandomAccessBufferedFileInputStream stream = new
                RandomAccessBufferedFileInputStream(new FileInputStream(file))) {
            PDFParser parser = new PDFParser(stream);
            try (COSDocument cosDocument = parser.getDocument()) {
                parser.parse();
                stripper = new PDFTextStripper();
                document = new PDDocument(cosDocument);
                stripper.setStartPage(startPage);
                stripper.setEndPage(endPage);
                return stripper.getText(document);
            } catch (IOException ie) {
                ie.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
