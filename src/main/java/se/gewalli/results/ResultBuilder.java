package se.gewalli.results;

public interface ResultBuilder<T, TError> {

    Result<T, TError> ok(T ok);

    Result<T, TError> error(TError error);
}
