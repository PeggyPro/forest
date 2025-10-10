package com.dtflys.forest.sse.sink;

import com.dtflys.forest.sse.SSESink;

public class EmptySink implements SSESink {
    @Override
    public SSESink next(Object data) {
        return this;
    }
}
