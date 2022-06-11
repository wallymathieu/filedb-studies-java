package se.gewalli.json

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule
import se.gewalli.AppendBatch
import se.gewalli.FailureReason
import se.gewalli.commands.Command
import se.gewalli.kyminon.Result
import java.io.BufferedWriter
import java.io.FileWriter
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ExecutorService
import java.util.function.Consumer

class AppendToFile(
    private val fileName: String,
    private val executorService: ExecutorService,
    private val logger: Consumer<Exception>
) : AppendBatch {
    private val objectMapper = ObjectMapper()
        .registerModule(ParameterNamesModule())
        .registerModule(Jdk8Module())
        .registerModule(JavaTimeModule())
        .registerModule(KotlinModule())

    override fun batch(commands: Collection<Command>): CompletableFuture<Result<Int, FailureReason>> {
        return CompletableFuture.supplyAsync({
            try {
                FileWriter(fileName, true).use { writer ->
                    BufferedWriter(writer).use { bufferedWriter ->
                        bufferedWriter.write(objectMapper.writeValueAsString(commands))
                        bufferedWriter.newLine()
                        return@supplyAsync Result.ok<Int, FailureReason>(commands.size)
                    }
                }
            } catch (e: IOException) {
                logger.accept(e)
                return@supplyAsync Result.error<Int, FailureReason>(FailureReason.IOException)
            }
        }, executorService)
    }

    override fun readAll(): CompletableFuture<Result<Collection<Command>, FailureReason>> {
        return CompletableFuture.supplyAsync(
            {
                try {
                    Files.newBufferedReader(Paths.get(fileName)).use { reader ->
                        val commands: MutableList<Command> = ArrayList()
                        reader.lines()
                            .filter { obj: String? -> Objects.nonNull(obj) }
                            .map { line: String -> parse(line) }
                            .filter { obj: Collection<Command>? -> Objects.nonNull(obj) }
                            .forEach { c: Collection<Command>? ->
                                commands.addAll(
                                    c!!
                                )
                            }
                        return@supplyAsync Result.ok<Collection<Command>, FailureReason>(commands)
                    }
                } catch (e: IOException) {
                    logger.accept(e)
                    return@supplyAsync Result.error<Collection<Command>, FailureReason>(FailureReason.IOException)
                }
            }, executorService
        )
    }

    private fun parse(line: String): Collection<Command> {
        return try {
            objectMapper.readValue(line)
        } catch (ex: IOException) { //NOTE: We assume low probability of this happening
            logger.accept(ex)
            throw RuntimeException(ex)
        }
    }
}