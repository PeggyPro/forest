package com.dtflys.forest.sse.sink;

import com.dtflys.forest.sse.SSESink;

import java.util.function.Consumer;

public class ConsumerSink implements SSESink {
    
    private final Consumer<Object> consumer;

    public ConsumerSink(Consumer<Object> consumer) {
        this.consumer = consumer;
    }

    @Override
    public SSESink next(Object data) {
        consumer.accept(data);
        return this;
    }
}
