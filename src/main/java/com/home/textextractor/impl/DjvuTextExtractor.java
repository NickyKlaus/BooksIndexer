package com.home.textextractor.impl;

import com.home.textextractor.DocumentTextExtractor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.function.BiFunction;
import java.util.function.Supplier;

import static com.home.model.FileType.djv;
import static com.home.model.FileType.djvu;
import static java.lang.String.format;
import static java.util.concurrent.CompletableFuture.supplyAsync;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.apache.commons.io.FilenameUtils.getExtension;

public class DjvuTextExtractor implements DocumentTextExtractor {
    private static final Logger LOG = LoggerFactory.getLogger(DjvuTextExtractor.class);

    private static ProcessBuilder processBuilder() {
        return new ProcessBuilder().redirectErrorStream(true);
    }

    private static Supplier<Process> startTextExtraction(final String extractionCommand) {
        return () -> {
            try {
                return processBuilder()
                        .command("zsh", "-c", extractionCommand)
                        .start();
            } catch (IOException e) {
                LOG.error("Error: ", e);
                return null;
            }
        };
    }

    private static final BiFunction<Process, Throwable, byte[]> retrieveExtractedBinaryData = (process, throwable) -> {
        if (throwable == null && process != null) {
            try (
                    var inputStream = process.getInputStream();
                    var data = new BufferedInputStream(inputStream)
            ) {
                return data.readAllBytes();
            } catch (IOException e) {
                LOG.error("Error: ", e);
                return new byte[0];
            }
        }
        return new byte[0];
    };

    private static CompletableFuture<byte[]> extractHiddenTextFromDjvu(
            final String fullyQualifiedSourceFilename,
            final int firstPage,
            final int lastPage
    ) {
        var extractionCommand = format(
                "djvutxt --page=%d-%d \"%s\"",
                firstPage,
                lastPage,
                fullyQualifiedSourceFilename);
        return supplyAsync(startTextExtraction(extractionCommand))
                .handleAsync(retrieveExtractedBinaryData)
                .exceptionally(t -> {
                    LOG.error("Error: ", t);
                    return new byte[0];
                });
    }

    @Override
    public String extract(final String fullyQualifiedSourceFilename, final int startPage, final int endPage) {
        assert djvu.toString().equalsIgnoreCase(getExtension(fullyQualifiedSourceFilename)) ||
                djv.toString().equalsIgnoreCase(getExtension(fullyQualifiedSourceFilename));
        assert (startPage > 0 && endPage > 0 && startPage <= endPage);

        try {
            var binaryData =
                    extractHiddenTextFromDjvu(fullyQualifiedSourceFilename, startPage, endPage)
                            .get(30L, SECONDS);
            return new String(binaryData, DEFAULT_TEXT_CHARSET);
        } catch (ExecutionException | InterruptedException | TimeoutException e) {
            LOG.error("Error: ", e);
            return "";
        }
    }
}
