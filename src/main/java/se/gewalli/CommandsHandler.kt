package se.gewalli

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import se.gewalli.commands.Command
import se.gewalli.data.EntityNotFound
import se.gewalli.data.Repository
import se.gewalli.kyminon.Result
import java.util.concurrent.CompletableFuture

/**
 * Wraps append batch and repository in order to append command results to repository and append them once done.
 */
class CommandsHandler {
    @Autowired
    private val appendBatch: AppendBatch? = null

    @Autowired
    private val repository: Repository? = null
    var logger = LoggerFactory.getLogger(CommandsHandler::class.java)
    fun handle(c: Command): CompletableFuture<Result<Int, FailureReason>> {
        val l = ArrayList<Command>()
        l.add(c)
        try {
            c.run(repository!!)
        } catch (entityNotFound: EntityNotFound) {
            logger.error("entity not found", entityNotFound)
        }
        return appendBatch!!.batch(l)
    }
}