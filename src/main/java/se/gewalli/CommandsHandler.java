package se.gewalli;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import se.gewalli.commands.Command;
import se.gewalli.data.EntityNotFound;
import se.gewalli.data.Repository;
import se.gewalli.results.Result;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

public class CommandsHandler {
    @Autowired
    private AppendBatch appendBatch;
    @Autowired
    private Repository repository;
    Logger logger = LoggerFactory.getLogger(CommandsHandler.class);

    public CompletableFuture<Result<Integer, FailureReason>> handle(Command c) {
        ArrayList<Command> l = new ArrayList<>();
        l.add(c);
        try {
            c.run(repository);
        } catch (EntityNotFound entityNotFound) {
            logger.error("entity not found", entityNotFound);
        }
        return appendBatch.batch(l);
    }

    @PostConstruct
    public void init() {
        appendBatch.readAll().thenApply(res -> res.fold(collection -> {
                    logger.info("booting up repository information based on stored information");
                    for (Command command : collection) {
                        try {
                            command.run(repository);
                        } catch (EntityNotFound entityNotFound) {
                            logger.error("EntityNotFound", entityNotFound);
                        }
                    }
                    return 0;
                },
                err -> {logger.error("Failed to read all", err); return 1;})).join();
    }

}
