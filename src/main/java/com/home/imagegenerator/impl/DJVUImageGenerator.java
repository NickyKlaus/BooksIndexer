package com.home.imagegenerator.impl;

import com.home.imagegenerator.ImageGenerator;

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

public class DJVUImageGenerator implements ImageGenerator {

    private static ProcessBuilder processBuilder() {
        return new ProcessBuilder().redirectErrorStream(true);
    }

    private static Supplier<Process> startImageGeneration(final String generationCommand) {
        return () -> {
            try {
                return processBuilder()
                        .command("zsh", "-c", generationCommand)
                        .start();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        };
    }

    private static final BiFunction<Process, Throwable, byte[]> retrieveGeneratedImageData = (process, throwable) -> {
        if (throwable == null && process != null) {
            try {
                return process.getInputStream().readAllBytes();
            } catch (IOException e) {
                e.printStackTrace();
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
                .handleAsync(retrieveGeneratedImageData);
    }

    @Override
    public byte[] generate(final String fullyQualifiedSourceFilename, final String targetImageFormat) {
        assert djvu.toString().equalsIgnoreCase(getExtension(fullyQualifiedSourceFilename)) ||
                djv.toString().equalsIgnoreCase(getExtension(fullyQualifiedSourceFilename));

        try {
            return createImageFromFirstDjvuPage(fullyQualifiedSourceFilename, targetImageFormat)
                    .get(60L, SECONDS);
        } catch (ExecutionException | InterruptedException | TimeoutException ioe) {
            ioe.printStackTrace();
        }
        return new byte[0];
    }
}
