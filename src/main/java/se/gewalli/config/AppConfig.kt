package se.gewalli.config

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import se.gewalli.AppendBatch
import se.gewalli.CommandsHandler
import se.gewalli.data.InMemoryRepository
import se.gewalli.data.Repository
import se.gewalli.json.AppendToFile
import java.io.File
import java.io.IOException
import java.util.concurrent.Executors

@Configuration
class AppConfig {
    @Autowired
    private val env: Environment? = null
    @Bean
    fun repository(): Repository {
        return InMemoryRepository()
    }

    @Bean
    @Throws(IOException::class)
    fun appendBatch(): AppendBatch {
        var dbLocation = env!!.getProperty("FILE_DB_LOCATION")
        val logger = LoggerFactory.getLogger(AppConfig::class.java)
        if (dbLocation == null || dbLocation.isEmpty()) {
            logger.info("No database location found, using tmp")
            dbLocation = "/tmp/test.db"
        }
        val db = File(dbLocation)
        if (!db.exists()) {
            db.createNewFile()
        }
        return AppendToFile(
            dbLocation,
            Executors.newFixedThreadPool(1)
        ) { err: Exception? -> logger.error("Error while appending ", err) }
    }

    @Bean
    fun persistCommandsHandler(): CommandsHandler {
        return CommandsHandler()
    }
}