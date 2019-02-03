package se.gewalli.results;

import java.util.function.Consumer;
import java.util.function.Function;


public abstract class Result<T, TError> {

    private static class Ok<T, TError> extends Result<T, TError> {
        private final T ok;

        public Ok(T ok) {
            this.ok = ok;
        }

        @Override
        public void match(Consumer<T> onOk, Consumer<TError> onError) {
            onOk.accept(ok);
        }

        @Override
        public <TResult> TResult fold(Function<T, TResult> onOk, Function<TError, TResult> onError) {
            return onOk.apply(ok);
        }

        @Override
        public <T1, T2> Result<T1, T2> bimap(Function<T, T1> onOk, Function<TError, T2> onError) {
            return new Ok<>(onOk.apply(ok));
        }

        @Override
        public String toString() {
            return String.format("ok %s", ok);
        }

        @Override
        public int hashCode() {
            return ok.hashCode();
        }

        @Override
        @SuppressWarnings("unchecked")
        public boolean equals(Object obj) {
            if (this.getClass().isInstance(obj)) {
                Ok<T, TError> err = (Ok<T, TError>) obj;
                return ok.equals(err.ok);
            }
            return false;
        }

    }

    private static class Error<T, TError> extends Result<T, TError> {
        private final TError error;

        Error(TError error) {
            this.error = error;
        }

        @Override
        public void match(Consumer<T> onOk, Consumer<TError> onError) {
            onError.accept(error);
        }

        @Override
        public <TResult> TResult fold(Function<T, TResult> onOk, Function<TError, TResult> onError) {
            return onError.apply(error);
        }

        @Override
        public <T1, T2> Result<T1, T2> bimap(Function<T, T1> onOk, Function<TError, T2> onError) {
            return new Error<>(onError.apply(error));
        }

        @Override
        public String toString() {
            return String.format("error %s", error);
        }

        @Override
        public int hashCode() {
            return error.hashCode();
        }

        @Override
        @SuppressWarnings("unchecked")
        public boolean equals(Object obj) {
            if (this.getClass().isInstance(obj)) {
                Error<T, TError> err = (Error<T, TError>) obj;
                return error.equals(err.error);
            }
            return false;
        }
    }
    public static <T, TError> Result<T, TError> ok(T ok){
        return new Ok<>(ok);
    }
    public static <T, TError> Result<T, TError> error(TError error){
        return new Error<>(error);
    }
    public abstract void match(Consumer<T> onOk, Consumer<TError> onError);
    public abstract <TResult> TResult fold(Function<T, TResult> onOk, Function<TError, TResult> onError);
    public abstract <T1,T2> Result<T1,T2> bimap(Function<T, T1> onOk, Function<TError, T2> onError);
}
