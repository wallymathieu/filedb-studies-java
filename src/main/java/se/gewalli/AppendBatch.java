package se.gewalli;

import io.atlassian.fugue.Either;
import se.gewalli.commands.Command;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

public interface AppendBatch {
    CompletableFuture<Either<FailureReason, Integer>> batch(Collection<Command> commands);

    CompletableFuture<Either<FailureReason, Collection<Command>>> readAll();
}
