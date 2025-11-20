package repository.writer;

import java.io.IOException;

@FunctionalInterface
public interface IOFunctionForWrite<T> {
    void accept(T t) throws IOException;
}
