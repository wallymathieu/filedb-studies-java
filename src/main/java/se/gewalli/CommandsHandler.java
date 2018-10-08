package se.gewalli;

import io.atlassian.fugue.Either;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import se.gewalli.commands.Command;
import se.gewalli.data.Repository;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

public class CommandsHandler {
    @Autowired
    private AppendBatch appendBatch;
    @Autowired
    private Repository repository;
    Logger logger = LoggerFactory.getLogger(CommandsHandler.class);

    public CompletableFuture<Either<FailureReason, Integer>> handle(Command c) {
        ArrayList<Command> l = new ArrayList<>();
        l.add(c);
        c.handle(repository);
        return appendBatch.batch(l);
    }

    @PostConstruct
    public void init() {
        appendBatch.readAll().thenApply(res -> res.bimap(
                err -> {
                    logger.error("Failed to read all", err);
                    return 1;
                },
                collection -> {
                    logger.info("booting up repository information based on stored information");
                    collection.stream().forEach(c -> c.handle(repository));
                    return 0;
                })).join();
    }

}
