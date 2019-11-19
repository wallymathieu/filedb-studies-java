package se.gewalli;

import se.gewalli.commands.Command;
import se.gewalli.kyminon.Result;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

public interface AppendBatch {
    CompletableFuture<Result<Integer, FailureReason>> batch(Collection<Command> commands);

    CompletableFuture<Result<Collection<Command>, FailureReason>> readAll();
}
