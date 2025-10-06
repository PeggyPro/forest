package com.dtflys.forest.reactor.sse;

import com.dtflys.forest.config.ForestConfiguration;
import com.dtflys.forest.converter.ForestConverter;
import com.dtflys.forest.http.ForestSSE;
import com.dtflys.forest.sse.EventSource;
import com.dtflys.forest.sse.SSEMessageConsumer;
import com.dtflys.forest.sse.SSEMessageConsumerWithSink;
import com.dtflys.forest.sse.SSEOnMessage;
import com.dtflys.forest.sse.SSEOnMessageWithSink;
import com.dtflys.forest.sse.SSEStringMessageConsumer;
import com.dtflys.forest.sse.SSEStringMessageConsumerWithSink;
import com.dtflys.forest.utils.ForestDataType;
import com.dtflys.forest.utils.ReflectUtils;
import com.dtflys.forest.utils.TypeReference;
import reactor.core.publisher.Flux;

import java.lang.reflect.Type;
import java.util.function.Consumer;
import java.util.function.Function;

public class ForestReactorSSE extends ForestSSE {

    @Override
    public ForestReactorSSE setOnMessage(SSEOnMessage onMessageConsumer) {
        return (ForestReactorSSE) super.setOnMessage(onMessageConsumer);
    }

    @Override
    public ForestReactorSSE setOnMessage(SSEOnMessageWithSink onMessageConsumer) {
        return (ForestReactorSSE) super.setOnMessage(onMessageConsumer);
    }

    @Override
    public ForestReactorSSE setOnOpen(Consumer<EventSource> onOpenConsumer) {
        return (ForestReactorSSE) super.setOnOpen(onOpenConsumer);
    }

    @Override
    public ForestReactorSSE setOnClose(Consumer<EventSource> onCloseConsumer) {
        return (ForestReactorSSE) super.setOnClose(onCloseConsumer);
    }


    @Override
    public ForestReactorSSE addConsumer(String name, SSEStringMessageConsumer consumer) {
        return (ForestReactorSSE) super.addConsumer(name, consumer);
    }

    @Override
    public ForestReactorSSE addConsumer(String name, SSEStringMessageConsumerWithSink consumer) {
        return (ForestReactorSSE) super.addConsumer(name, consumer);
    }

    @Override
    public <T> ForestReactorSSE addConsumer(String name, Class<T> valueType, SSEMessageConsumer<T> consumer) {
        return (ForestReactorSSE) super.addConsumer(name, valueType, consumer);
    }

    @Override
    public <T> ForestReactorSSE addConsumer(String name, Class<T> valueType, SSEMessageConsumerWithSink<T> consumer) {
        return (ForestReactorSSE) super.addConsumer(name, valueType, consumer);
    }

    @Override
    public <T> ForestReactorSSE addConsumer(String name, TypeReference<T> valueType, SSEMessageConsumer<T> consumer) {
        return (ForestReactorSSE) super.addConsumer(name, valueType, consumer);
    }

    @Override
    public ForestReactorSSE addConsumer(String name, Function<EventSource, Boolean> matcher, SSEStringMessageConsumer consumer) {
        return (ForestReactorSSE) super.addConsumer(name, matcher, consumer);
    }

    @Override
    public ForestReactorSSE addConsumer(String name, Function<EventSource, Boolean> matcher, SSEStringMessageConsumerWithSink consumer) {
        return (ForestReactorSSE) super.addConsumer(name, matcher, consumer);
    }

    @Override
    public ForestReactorSSE addConsumerMatchesPrefix(String name, String valuePrefix, SSEStringMessageConsumer consumer) {
        return (ForestReactorSSE) super.addConsumerMatchesPrefix(name, valuePrefix, consumer);
    }

    @Override
    public ForestReactorSSE addConsumerMatchesPrefix(String name, String valuePrefix, SSEStringMessageConsumerWithSink consumer) {
        return (ForestReactorSSE) super.addConsumerMatchesPrefix(name, valuePrefix, consumer);
    }

    @Override
    public ForestReactorSSE addConsumerMatchesPostfix(String name, String valuePostfix, SSEStringMessageConsumer consumer) {
        return (ForestReactorSSE) super.addConsumerMatchesPostfix(name, valuePostfix, consumer);
    }

    @Override
    public ForestReactorSSE addConsumerMatchesPostfix(String name, String valuePostfix, SSEStringMessageConsumerWithSink consumer) {
        return (ForestReactorSSE) super.addConsumerMatchesPostfix(name, valuePostfix, consumer);
    }

    @Override
    public ForestReactorSSE addConsumerMatches(String name, String valueRegex, SSEStringMessageConsumer consumer) {
        return (ForestReactorSSE) super.addConsumerMatches(name, valueRegex, consumer);
    }

    @Override
    public ForestReactorSSE addConsumerMatches(String name, String valueRegex, String valuePrefix, String valuePostfix, SSEStringMessageConsumer consumer) {
        return (ForestReactorSSE) super.addConsumerMatches(name, valueRegex, valuePrefix, valuePostfix, consumer);
    }

    @Override
    public ForestReactorSSE addOnData(SSEStringMessageConsumer consumer) {
        return (ForestReactorSSE) super.addOnData(consumer);
    }

    @Override
    public ForestReactorSSE addOnData(SSEStringMessageConsumerWithSink consumer) {
        return (ForestReactorSSE) super.addOnData(consumer);
    }

    @Override
    public <T> ForestReactorSSE addOnData(Class<T> valueClass, SSEMessageConsumer<T> consumer) {
        return (ForestReactorSSE) super.addOnData(valueClass, consumer);
    }

    @Override
    public <T> ForestReactorSSE addOnData(Class<T> valueClass, SSEMessageConsumerWithSink<T> consumer) {
        return (ForestReactorSSE) super.addOnData(valueClass, consumer);
    }

    @Override
    public ForestReactorSSE addOnDataMatchesPrefix(String valuePrefix, SSEStringMessageConsumer consumer) {
        return (ForestReactorSSE) super.addOnDataMatchesPrefix(valuePrefix, consumer);
    }

    @Override
    public ForestReactorSSE addOnDataMatchesPrefix(String valuePrefix, SSEStringMessageConsumerWithSink consumer) {
        return (ForestReactorSSE) super.addOnDataMatchesPrefix(valuePrefix, consumer);
    }

    @Override
    public ForestReactorSSE addOnDataMatchesPostfix(String valuePostfix, SSEStringMessageConsumer consumer) {
        return (ForestReactorSSE) super.addOnDataMatchesPostfix(valuePostfix, consumer);
    }

    @Override
    public ForestSSE addOnDataMatchesPostfix(String valuePostfix, SSEStringMessageConsumerWithSink consumer) {
        return (ForestReactorSSE) super.addOnDataMatchesPostfix(valuePostfix, consumer);
    }

    @Override
    public ForestReactorSSE addOnEvent(SSEStringMessageConsumer consumer) {
        return (ForestReactorSSE) super.addOnEvent(consumer);
    }

    @Override
    public ForestReactorSSE addOnEvent(SSEStringMessageConsumerWithSink consumer) {
        return (ForestReactorSSE) super.addOnEvent(consumer);
    }

    @Override
    public <T> ForestReactorSSE addOnEvent(Class<T> valueClass, SSEMessageConsumer<T> consumer) {
        return (ForestReactorSSE) super.addOnEvent(valueClass, consumer);
    }

    @Override
    public <T> ForestReactorSSE addOnEvent(Class<T> valueClass, SSEMessageConsumerWithSink<T> consumer) {
        return (ForestReactorSSE) super.addOnEvent(valueClass, consumer);
    }

    @Override
    public ForestReactorSSE addOnEventMatchesPrefix(String valuePrefix, SSEStringMessageConsumer consumer) {
        return (ForestReactorSSE) super.addOnEventMatchesPrefix(valuePrefix, consumer);
    }

    @Override
    public ForestReactorSSE addOnEventMatchesPrefix(String valuePrefix, SSEStringMessageConsumerWithSink consumer) {
        return (ForestReactorSSE) super.addOnEventMatchesPrefix(valuePrefix, consumer);
    }

    @Override
    public ForestReactorSSE addOnEventMatchesPostfix(String valuePostfix, SSEStringMessageConsumer consumer) {
        return (ForestReactorSSE) super.addOnEventMatchesPostfix(valuePostfix, consumer);
    }

    @Override
    public ForestReactorSSE addOnEventMatchesPostfix(String valuePostfix, SSEStringMessageConsumerWithSink consumer) {
        return (ForestReactorSSE) super.addOnEventMatchesPostfix(valuePostfix, consumer);
    }


    @Override
    public ForestReactorSSE addOnId(SSEStringMessageConsumer consumer) {
        return (ForestReactorSSE) super.addOnId(consumer);
    }

    @Override
    public ForestReactorSSE addOnId(SSEStringMessageConsumerWithSink consumer) {
        return (ForestReactorSSE) super.addOnId(consumer);
    }


    @Override
    public <T> ForestReactorSSE addOnId(Class<T> valueClass, SSEMessageConsumer<T> consumer) {
        return (ForestReactorSSE) super.addOnId(valueClass, consumer);
    }

    @Override
    public <T> ForestReactorSSE addOnId(Class<T> valueClass, SSEMessageConsumerWithSink<T> consumer) {
        return (ForestReactorSSE) super.addOnId(valueClass, consumer);
    }

    @Override
    public ForestReactorSSE addOnIdMatchesPrefix(String valuePrefix, SSEStringMessageConsumer consumer) {
        return (ForestReactorSSE) super.addOnIdMatchesPrefix(valuePrefix, consumer);
    }

    @Override
    public ForestReactorSSE addOnIdMatchesPrefix(String valuePrefix, SSEStringMessageConsumerWithSink consumer) {
        return (ForestReactorSSE) super.addOnIdMatchesPrefix(valuePrefix, consumer);
    }

    @Override
    public ForestReactorSSE addOnIdMatchesPostfix(String valuePostfix, SSEStringMessageConsumer consumer) {
        return (ForestReactorSSE) super.addOnIdMatchesPostfix(valuePostfix, consumer);
    }

    @Override
    public ForestReactorSSE addOnIdMatchesPostfix(String valuePostfix, SSEStringMessageConsumerWithSink consumer) {
        return (ForestReactorSSE) super.addOnIdMatchesPostfix(valuePostfix, consumer);
    }

    @Override
    public ForestReactorSSE addOnRetry(SSEStringMessageConsumer consumer) {
        return (ForestReactorSSE) super.addOnRetry(consumer);
    }

    @Override
    public ForestReactorSSE addOnRetry(SSEStringMessageConsumerWithSink consumer) {
        return (ForestReactorSSE) super.addOnRetry(consumer);
    }

    @Override
    public <T> ForestReactorSSE addOnRetry(Class<T> valueClass, SSEMessageConsumer<T> consumer) {
        return (ForestReactorSSE) super.addOnRetry(valueClass, consumer);
    }

    @Override
    public <T> ForestReactorSSE addOnRetry(Class<T> valueClass, SSEMessageConsumerWithSink<T> consumer) {
        return (ForestReactorSSE) super.addOnRetry(valueClass, consumer);
    }

    @Override
    public ForestReactorSSE addOnRetryMatchesPrefix(String valuePrefix, SSEStringMessageConsumer consumer) {
        return (ForestReactorSSE) super.addOnRetryMatchesPrefix(valuePrefix, consumer);
    }

    @Override
    public ForestReactorSSE addOnRetryMatchesPrefix(String valuePrefix, SSEStringMessageConsumerWithSink consumer) {
        return (ForestReactorSSE) super.addOnRetryMatchesPrefix(valuePrefix, consumer);
    }

    @Override
    public ForestReactorSSE addOnRetryMatchesPostfix(String valuePostfix, SSEStringMessageConsumer consumer) {
        return (ForestReactorSSE) super.addOnRetryMatchesPostfix(valuePostfix, consumer);
    }

    @Override
    public ForestReactorSSE addOnRetryMatchesPostfix(String valuePostfix, SSEStringMessageConsumerWithSink consumer) {
        return (ForestReactorSSE) super.addOnRetryMatchesPostfix(valuePostfix, consumer);
    }

    public <T> Flux<T> toFlux(TypeReference<T> typeReference) {
        return toFlux(typeReference.getType());
    }

    public <T> Flux<T> toFlux(Class<T> clazz) {
        return toFlux(ReflectUtils.toType(clazz));
    }

    public <T> Flux<T> toFlux(Type targetType) {
        final ForestConfiguration configuration = getRequest().getConfiguration();
        final ForestConverter converter = configuration.getConverter(ForestDataType.AUTO);
        final ForestReactorSSE self = this;
        
        return Flux.create(emitter -> {
            final Consumer<EventSource> superOnCloseConsumer = onCloseConsumer;
            self.setOnClose(event -> {
                try {
                    if (superOnCloseConsumer != null) {
                        superOnCloseConsumer.accept(event);
                    }
                } catch (Exception e) {
                    emitter.error(e);
                } finally {
                    emitter.complete();
                }
            })
            .listen(data -> {
                try {
                    emitter.next((T) data);
                } catch (Exception e) {
                    emitter.error(e);
                }
            });
        });
    } 
    
}
