package com.home;

import com.home.dbclient.MongoDbClient;
import com.home.mapping.BookMapper;
import com.home.model.Book;
import com.home.model.Cover;
import org.bson.codecs.ValueCodecProvider;
import org.bson.codecs.jsr310.Jsr310CodecProvider;
import org.bson.codecs.pojo.PojoCodecProvider;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.home.model.FileType.contains;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.MINUTES;
import static org.apache.commons.io.FilenameUtils.getExtension;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;

public class BooksIndexer {
    private static final long EXECUTION_TIMEOUT_IN_MINUTES = 30L;
    private static final String ROOT_BOOKS_DIR_PROPERTY = "root_books_dir";
    private static final String DB_HOST_PROPERTY = "host";
    private static final String DB_PORT_PROPERTY = "port";
    private static final Predicate<BasicFileAttributes> isRegularFile = BasicFileAttributes::isRegularFile;
    private static final Predicate<Path> notStartWithDot = path -> !path.getFileName().toString().startsWith(".");
    private static final BiPredicate<Path, BasicFileAttributes> isAvailableBookFile =
            (path, basicFileAttributes) -> isRegularFile.test(basicFileAttributes) && notStartWithDot.test(path) &&
                    contains(getExtension(path.getFileName().toString()));

    private Stream<Path> findAvailableBooks(final Path rootDir) {
        try {
            return Files.find(rootDir, Integer.MAX_VALUE, isAvailableBookFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Stream.empty();
    }

    private void indexBooks(final Path rootPath, final Consumer<? super Book> indexBooksTask) {
        var start = System.currentTimeMillis();
        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        try (var books = findAvailableBooks(rootPath)) {
            for (Path path : books.collect(Collectors.toUnmodifiableList())) {
                executorService.execute(() -> indexBooksTask.accept(BookMapper.toBook(path)));
            }
            executorService.shutdown();
            if (!executorService.awaitTermination(EXECUTION_TIMEOUT_IN_MINUTES, MINUTES)) {
                executorService.shutdown();
            }
        } catch (InterruptedException ie) {
            executorService.shutdownNow();
        }

        var duration = System.currentTimeMillis() - start;
        System.out.printf(
                "Done in %d min, %d sec%n",
                MILLISECONDS.toMinutes(duration),
                MILLISECONDS.toSeconds(duration) -
                        MINUTES.toSeconds(MILLISECONDS.toMinutes(duration))
        );
    }

    public static void main(String[] args) {
        var rootBooksDir =  System.getProperty(ROOT_BOOKS_DIR_PROPERTY);
        assert (rootBooksDir != null && !rootBooksDir.isEmpty());
        var rootBooksPath = Path.of(rootBooksDir);
        assert (rootBooksPath.toFile().exists() && rootBooksPath.toFile().isDirectory());
        var dbHost =  System.getProperty(DB_HOST_PROPERTY);
        assert (dbHost != null && !dbHost.isEmpty());
        var dbPort =  System.getProperty(DB_PORT_PROPERTY);
        assert (dbPort != null && !dbPort.isEmpty());

        try (var client = MongoDbClient.getClient(String.format("mongodb://%s:%s", dbHost,dbPort))) {
            var codecRegistry = fromProviders(
                    PojoCodecProvider.builder().register(Book.class, Cover.class).build(),
                    new Jsr310CodecProvider(),
                    new ValueCodecProvider()
            );
            var collection = client
                    .getDatabase("library")
                    .withCodecRegistry(codecRegistry)
                    .getCollection("books", Book.class);

            new BooksIndexer().indexBooks(rootBooksPath, collection::insertOne);
        }
    }
}
