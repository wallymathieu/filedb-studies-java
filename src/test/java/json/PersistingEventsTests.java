package json;

import io.atlassian.fugue.Either;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import se.gewalli.Collections;
import se.gewalli.FailureReason;
import se.gewalli.commands.Command;
import se.gewalli.json.AppendToFile;
import xmlimport.GetCommands;

import java.io.File;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PersistingEventsTests {
    static GetCommands c = new GetCommands();
    Collection<Command> cs = c.Get();
    ExecutorService pool = Executors.newFixedThreadPool(1);

    @Test
    public void read_items_persisted_in_separate_batches() {
        String db = "./tmp/Json_CustomerDataTests_1.db";
        try {
            AppendToFile appendToFile = new AppendToFile(db, pool, ex -> System.err.println(ex.toString()));
            Collection<Collection<Command>> batches = Collections.batchesOf(cs, 3);
            assertTrue(batches.size() >= 2);
            CompletableFuture<Void> ap =
                    CompletableFuture.allOf(batches.stream()
                            .map(appendToFile::batch).toArray(CompletableFuture[]::new));
            ap.join();
            Either<FailureReason, Collection<Command>> read = appendToFile.readAll().join();
            assertCount(read);
        } finally {
            new File(db).delete();
        }
    }

    private void assertCount(Either<FailureReason, Collection<Command>> read) {
        read.fold(err -> {
                    Assertions.fail(err.name());
                    return 1;
                },
                cs1 -> {
                    assertEquals(cs.size(), cs1.size());
                    return 2;
                });
    }

    @Test
    public void read_items_persisted_in_single_batch() {
        String db = "./tmp/Json_CustomerDataTests_2.db";
        try {
            AppendToFile appendToFile = new AppendToFile(db, pool, ex -> System.err.println(ex.toString()));
            appendToFile.batch(cs).join();
            Either<FailureReason, Collection<Command>> read = appendToFile.readAll().join();
            assertCount(read);
        } finally {
            new File(db).delete();
        }
    }

    @Test
    public void read_items() {
        String db = "./tmp/Json_CustomerDataTests_3.db";
        try {
            AppendToFile appendToFile = new AppendToFile(db, pool, ex -> System.err.println(ex.toString()));
            appendToFile.batch(cs).join();
            Either<FailureReason, Collection<Command>> read = appendToFile.readAll().join();
            read.fold(err -> {
                Assertions.fail(err.name());
                return 1;
            }, cs1 -> {
                assertArrayEquals(cs1.stream().map(c -> c.getType()).toArray(),
                        cs.stream().map(c -> c.getType()).toArray());
                assertArrayEquals(cs1.stream().map(c -> c.id).toArray(),
                        cs.stream().map(c -> c.id).toArray());
                return 2;
            });
        } finally {
            new File(db).delete();
        }
    }
}
