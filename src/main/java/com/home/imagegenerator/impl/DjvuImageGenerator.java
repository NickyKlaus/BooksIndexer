package com.home.imagegenerator.impl;

import com.home.imagegenerator.ImageGenerator;
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

public class DjvuImageGenerator implements ImageGenerator {
    private static final Logger LOG = LoggerFactory.getLogger(DjvuImageGenerator.class);

    private static ProcessBuilder processBuilder() {
        return new ProcessBuilder().redirectErrorStream(true);
    }

    private static Supplier<Process> startImageGeneration(final String generationCommand) {
        return () -> {
            try {
                return processBuilder()
                        .command("bash", "-c", generationCommand)
                        .start();
            } catch (IOException e) {
                LOG.error("Error: ", e);
                return null;
            }
        };
    }

    private static final BiFunction<Process, Throwable, byte[]> retrieveGeneratedImageData = (process, throwable) -> {
        if (throwable == null && process != null) {
            try (
                    var inputStream = process.getInputStream();
                    var bufferedInputStream = new BufferedInputStream(inputStream)
            ) {
                return bufferedInputStream.readAllBytes();
            } catch (IOException e) {
                LOG.error("Error: ", e);
                return new byte[0];
            }
        }
        return new byte[0];
    };

    private static CompletableFuture<byte[]> createImageFromFirstDjvuPage(
            final String fullyQualifiedSourceFilename,
            final String targetImageFormat
    ) {
        var generationCommand = format(
                "ddjvu -format=pnm -page=1 -mode=color -skip -scale=72 \"%s\" - | convert pnm:fd:0 %s:- ",
                fullyQualifiedSourceFilename,
                targetImageFormat);
        return supplyAsync(startImageGeneration(generationCommand))
                .handleAsync(retrieveGeneratedImageData)
                .exceptionally(t -> {
                    LOG.error("Error: ", t);
                    return new byte[0];
                });
    }

    @Override
    public byte[] generate(final String fullyQualifiedSourceFilename, final String targetImageFormat) {
        assert djvu.toString().equalsIgnoreCase(getExtension(fullyQualifiedSourceFilename)) ||
                djv.toString().equalsIgnoreCase(getExtension(fullyQualifiedSourceFilename));

        try {
            return createImageFromFirstDjvuPage(fullyQualifiedSourceFilename, targetImageFormat)
                    .get(30L, SECONDS);
        } catch (ExecutionException | InterruptedException | TimeoutException e) {
            LOG.error("Error: ", e);
            return new byte[0];
        }
    }
}
