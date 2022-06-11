package se.gewalli

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import se.gewalli.commands.Command
import se.gewalli.data.EntityNotFound
import se.gewalli.data.Repository
import se.gewalli.kyminon.Result
import javax.annotation.PostConstruct

@Component
class PostInitializationBean {
    @Autowired
    private val appendBatch: AppendBatch? = null

    @Autowired
    private val repository: Repository? = null
    var logger = LoggerFactory.getLogger(PostInitializationBean::class.java)
    @PostConstruct
    fun init() {
        appendBatch!!.readAll()!!
            .thenApply { res: Result<Collection<Command>, FailureReason> ->
                res!!.fold(
                    { collection: Collection<Command?>? ->
                        logger.info("booting up repository information based on stored information")
                        for (command in collection!!) {
                            try {
                                command!!.run(repository!!)
                            } catch (entityNotFound: EntityNotFound) {
                                logger.error("EntityNotFound", entityNotFound)
                            }
                        }
                        0
                    }
                ) { err: FailureReason? ->
                    logger.error("Failed to read all", err)
                    1
                }
            }.join()
    }
}