package json

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import se.gewalli.Collections.batchesOf
import se.gewalli.FailureReason
import se.gewalli.commands.Command
import se.gewalli.json.AppendToFile
import se.gewalli.kyminon.Result
import xmlimport.GetCommands
import java.io.File
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executors

class PersistingEventsTests {
    var cs = c.Get()
    var pool = Executors.newFixedThreadPool(1)
    @Test
    fun read_items_persisted_in_separate_batches() {
        val db = "./tmp/Json_CustomerDataTests_1.db"
        try {
            val appendToFile = AppendToFile(db, pool) { ex: Exception -> System.err.println(ex.toString()) }
            val batches = batchesOf(cs, 3)
            Assertions.assertTrue(batches.size >= 2)
            val ap = CompletableFuture.allOf(*batches
                .map { commands -> appendToFile.batch(commands) }
                .toTypedArray())
            ap.join()
            val read: Result<Collection<Command>, FailureReason> = appendToFile.readAll().join()
            read.match(
                { cs1: Collection<Command?> -> Assertions.assertEquals(cs.size, cs1.size) }
            ) { err: FailureReason -> Assertions.fail<Any>(err.name) }
        } finally {
            File(db).delete()
        }
    }

    @Test
    fun read_items_persisted_in_single_batch() {
        val db = "./tmp/Json_CustomerDataTests_2.db"
        try {
            val appendToFile = AppendToFile(db, pool) { ex: Exception -> System.err.println(ex.toString()) }
            appendToFile.batch(cs).join()
            val read: Result<Collection<Command>, FailureReason> = appendToFile.readAll().join()
            read.match(
                { cs1: Collection<Command?> -> Assertions.assertEquals(cs1.size, cs.size) }
            ) { err: FailureReason -> Assertions.fail<Any>(err.name) }
        } finally {
            File(db).delete()
        }
    }

    @Test
    fun read_items() {
        val db = "./tmp/Json_CustomerDataTests_3.db"
        try {
            val appendToFile = AppendToFile(db, pool) { ex: Exception -> System.err.println(ex.toString()) }
            appendToFile.batch(cs).join()
            val read = appendToFile.readAll().join()
            read.match(
                { cs1: Collection<Command> ->
                    Assertions.assertArrayEquals(cs1.stream().map { c: Command -> c.type }
                        .toArray(),
                        cs.stream().map { c: Command? -> c!!.type }.toArray())
                    Assertions.assertArrayEquals(cs1.stream().map { c: Command -> c.id }
                        .toArray(),
                        cs.stream().map { c: Command? -> c!!.id }.toArray())
                }
            ) { err: FailureReason -> Assertions.fail<Any>(err.name) }
        } finally {
            File(db).delete()
        }
    }

    companion object {
        var c = GetCommands()
    }
}