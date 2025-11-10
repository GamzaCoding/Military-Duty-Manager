package repository.reader;

import java.io.IOException;

@FunctionalInterface
public interface IOFunctionForRead<T, R> {
    R apply(T t) throws IOException;
}
