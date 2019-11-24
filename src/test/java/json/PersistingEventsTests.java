package json;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import se.gewalli.Collections;
import se.gewalli.commands.Command;
import se.gewalli.json.AppendToFile;
import xmlimport.GetCommands;

public class PersistingEventsTests {
    static GetCommands c=new GetCommands();
    Collection<Command> cs = c.Get();
    ExecutorService pool = Executors.newFixedThreadPool(1);

    @Test
    public void read_items_persisted_in_separate_batches() {
        var db="./tmp/Json_CustomerDataTests_1.db";
        try {
            var appendToFile=new AppendToFile(db, pool, ex-> System.err.println(ex.toString()));
            var batches = Collections.batchesOf(cs, 3);
            assertTrue(batches.size()>=2);
            var ap =
                    CompletableFuture.allOf(batches.stream()
                    .map(appendToFile::batch).toArray(CompletableFuture[]::new));
            ap.join();
            var read = appendToFile.readAll().join();
            read.match(cs1->assertEquals(cs.size(), cs1.size()),
                    err-> Assertions.fail(err.name()));
        }finally {
            new File(db).delete();
        }
    }
    @Test
    public void read_items_persisted_in_single_batch(){
        var db = "./tmp/Json_CustomerDataTests_2.db";
        try {
            var appendToFile = new AppendToFile(db, pool, ex -> System.err.println(ex.toString()));
            appendToFile.batch(cs).join();
            var read = appendToFile.readAll().join();
            read.match(cs1 -> assertEquals(cs1.size(), cs.size()),
                    err -> Assertions.fail(err.name()));
        }finally {
            new File(db).delete();
        }
    }
    @Test
    public void read_items(){
        var db = "./tmp/Json_CustomerDataTests_3.db";
        try {
            var appendToFile = new AppendToFile(db, pool, ex -> System.err.println(ex.toString()));
            appendToFile.batch(cs).join();
            var read = appendToFile.readAll().join();
            read.match(cs1 -> {
                        assertArrayEquals(cs1.stream().map(c->c.getType()).toArray(),
                                cs.stream().map(c->c.getType()).toArray());
                        assertArrayEquals(cs1.stream().map(c->c.id).toArray(),
                                cs.stream().map(c->c.id).toArray());
                    },
                    err -> Assertions.fail(err.name()));
        }finally {
            new File(db).delete();
        }
    }
}
