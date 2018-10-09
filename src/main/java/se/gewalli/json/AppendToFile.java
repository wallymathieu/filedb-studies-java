package se.gewalli.json;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import se.gewalli.AppendBatch;
import se.gewalli.FailureReason;
import se.gewalli.commands.Command;
import se.gewalli.results.Result;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;

public class AppendToFile implements AppendBatch {
    private final String fileName;
    private final ExecutorService executorService;
    private final Consumer<Exception> logger;
    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new ParameterNamesModule())
            .registerModule(new Jdk8Module())
            .registerModule(new JavaTimeModule());


    public AppendToFile(String fileName, ExecutorService executorService, Consumer<Exception> logger) {
        this.executorService = executorService;
        this.logger = logger;
        this.fileName = fileName;
    }

    @Override
    public CompletableFuture<Result<Integer, FailureReason>> batch(Collection<Command> commands) {
        return CompletableFuture.supplyAsync(() -> {
            try (FileWriter fw = new FileWriter(fileName, true);
                 BufferedWriter bw = new BufferedWriter(fw)) {
                bw.write(objectMapper.writeValueAsString(commands));
                bw.newLine();
                return Result.ok(commands.size());
            } catch (IOException e) {
                logger.accept(e);
                return Result.error(FailureReason.IOException);
            }
        }, executorService);
    }

    @Override
    public CompletableFuture<Result<Collection<Command>, FailureReason>> readAll() {
        return CompletableFuture.supplyAsync(() -> {
            try (BufferedReader r = Files.newBufferedReader(Paths.get(fileName))) {
                List<Command> commands = new ArrayList<>();

                r.lines()
                        .filter(Objects::nonNull)
                        .map(this::parse)
                        .filter(Objects::nonNull)
                        .forEach(commands::addAll);

                return Result.ok(commands);
            } catch (IOException e) {
                logger.accept(e);
                return Result.error(FailureReason.IOException);
            }
        }, executorService);
    }

    private Collection<Command> parse(String line) {
        try {
            return objectMapper.readValue(line, new TypeReference<Collection<Command>>() {
            });
        } catch (IOException ex) {//NOTE: We assume low probability of this happening
            logger.accept(ex);
            throw new RuntimeException(ex);
        }
    }
}
