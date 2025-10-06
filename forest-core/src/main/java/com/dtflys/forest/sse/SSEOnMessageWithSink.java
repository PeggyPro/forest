package com.dtflys.forest.sse;

@FunctionalInterface
public interface SSEOnMessageWithSink {

    void onMessage(EventSource event, SSESink sink);
    
}
