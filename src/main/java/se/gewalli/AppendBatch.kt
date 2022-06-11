package se.gewalli

import se.gewalli.commands.Command
import se.gewalli.kyminon.Result
import java.util.concurrent.CompletableFuture

interface AppendBatch {
    fun batch(commands: Collection<Command>): CompletableFuture<Result<Int, FailureReason>>
    fun readAll(): CompletableFuture<Result<Collection<Command>, FailureReason>>
}