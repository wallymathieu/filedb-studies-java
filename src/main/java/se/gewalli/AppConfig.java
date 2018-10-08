package se.gewalli;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import se.gewalli.data.InMemoryRepository;
import se.gewalli.data.Repository;
import se.gewalli.json.AppendToFile;

import java.util.concurrent.Executors;



@Configuration
public class AppConfig {
    @Autowired
    private Environment env;

    @Bean
    public Repository repository() {
        return new InMemoryRepository();
    }

    @Bean
    public AppendBatch appendBatch() {
        String dbLocation = env.getProperty("FILE_DB_LOCATION");
        Logger logger = LoggerFactory.getLogger(AppConfig.class);
        if (dbLocation == null || dbLocation.isEmpty()) {
            logger.info("No database location found, using tmp");
            dbLocation = "/tmp/test.db";
        }
        return new AppendToFile(dbLocation,
                Executors.newFixedThreadPool(1),
                err -> logger.error("Error while appending ", err));
    }

    @Bean
    public CommandsHandler persistCommandsHandler() {
        return new CommandsHandler();
    }
}