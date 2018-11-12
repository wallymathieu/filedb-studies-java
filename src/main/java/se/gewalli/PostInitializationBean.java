package se.gewalli;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.gewalli.commands.Command;
import se.gewalli.data.EntityNotFound;
import se.gewalli.data.Repository;

import javax.annotation.PostConstruct;

@Component
public class PostInitializationBean {
    @Autowired
    private AppendBatch appendBatch;
    @Autowired
    private Repository repository;
    Logger logger = LoggerFactory.getLogger(PostInitializationBean.class);

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
