package com.dtflys.forest.reactor.sse;

import com.dtflys.forest.config.ForestConfiguration;
import com.dtflys.forest.converter.ForestConverter;
import com.dtflys.forest.http.ForestSSE;
import com.dtflys.forest.sse.EventSource;
import com.dtflys.forest.sse.SSELinesMode;
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

public class ReactorSSE extends ForestSSE {

    @Override
    public ReactorSSE setOnMessage(SSEOnMessage onMessageConsumer) {
        return (ReactorSSE) super.setOnMessage(onMessageConsumer);
    }

    @Override
    public ReactorSSE setOnMessage(SSEOnMessageWithSink onMessageConsumer) {
        return (ReactorSSE) super.setOnMessage(onMessageConsumer);
    }

    @Override
    public ReactorSSE setOnOpen(Consumer<EventSource> onOpenConsumer) {
        return (ReactorSSE) super.setOnOpen(onOpenConsumer);
    }

    @Override
    public ReactorSSE setOnClose(Consumer<EventSource> onCloseConsumer) {
        return (ReactorSSE) super.setOnClose(onCloseConsumer);
    }


    @Override
    public ReactorSSE addConsumer(String name, SSEStringMessageConsumer consumer) {
        return (ReactorSSE) super.addConsumer(name, consumer);
    }

    @Override
    public ReactorSSE addConsumer(String name, SSEStringMessageConsumerWithSink consumer) {
        return (ReactorSSE) super.addConsumer(name, consumer);
    }

    @Override
    public <T> ReactorSSE addConsumer(String name, Class<T> valueType, SSEMessageConsumer<T> consumer) {
        return (ReactorSSE) super.addConsumer(name, valueType, consumer);
    }

    @Override
    public <T> ReactorSSE addConsumer(String name, Class<T> valueType, SSEMessageConsumerWithSink<T> consumer) {
        return (ReactorSSE) super.addConsumer(name, valueType, consumer);
    }

    @Override
    public <T> ReactorSSE addConsumer(String name, TypeReference<T> valueType, SSEMessageConsumer<T> consumer) {
        return (ReactorSSE) super.addConsumer(name, valueType, consumer);
    }

    @Override
    public ReactorSSE addConsumer(String name, Function<EventSource, Boolean> matcher, SSEStringMessageConsumer consumer) {
        return (ReactorSSE) super.addConsumer(name, matcher, consumer);
    }

    @Override
    public ReactorSSE addConsumer(String name, Function<EventSource, Boolean> matcher, SSEStringMessageConsumerWithSink consumer) {
        return (ReactorSSE) super.addConsumer(name, matcher, consumer);
    }

    @Override
    public ReactorSSE addConsumerMatchesPrefix(String name, String valuePrefix, SSEStringMessageConsumer consumer) {
        return (ReactorSSE) super.addConsumerMatchesPrefix(name, valuePrefix, consumer);
    }

    @Override
    public ReactorSSE addConsumerMatchesPrefix(String name, String valuePrefix, SSEStringMessageConsumerWithSink consumer) {
        return (ReactorSSE) super.addConsumerMatchesPrefix(name, valuePrefix, consumer);
    }

    @Override
    public ReactorSSE addConsumerMatchesPostfix(String name, String valuePostfix, SSEStringMessageConsumer consumer) {
        return (ReactorSSE) super.addConsumerMatchesPostfix(name, valuePostfix, consumer);
    }

    @Override
    public ReactorSSE addConsumerMatchesPostfix(String name, String valuePostfix, SSEStringMessageConsumerWithSink consumer) {
        return (ReactorSSE) super.addConsumerMatchesPostfix(name, valuePostfix, consumer);
    }

    @Override
    public ReactorSSE addConsumerMatches(String name, String valueRegex, SSEStringMessageConsumer consumer) {
        return (ReactorSSE) super.addConsumerMatches(name, valueRegex, consumer);
    }

    @Override
    public ReactorSSE addConsumerMatches(String name, String valueRegex, String valuePrefix, String valuePostfix, SSEStringMessageConsumer consumer) {
        return (ReactorSSE) super.addConsumerMatches(name, valueRegex, valuePrefix, valuePostfix, consumer);
    }

    @Override
    public ReactorSSE addOnData(SSEStringMessageConsumer consumer) {
        return (ReactorSSE) super.addOnData(consumer);
    }

    @Override
    public ReactorSSE addOnData(SSEStringMessageConsumerWithSink consumer) {
        return (ReactorSSE) super.addOnData(consumer);
    }

    @Override
    public <T> ReactorSSE addOnData(Class<T> valueClass, SSEMessageConsumer<T> consumer) {
        return (ReactorSSE) super.addOnData(valueClass, consumer);
    }

    @Override
    public <T> ReactorSSE addOnData(Class<T> valueClass, SSEMessageConsumerWithSink<T> consumer) {
        return (ReactorSSE) super.addOnData(valueClass, consumer);
    }

    @Override
    public ReactorSSE addOnDataMatchesPrefix(String valuePrefix, SSEStringMessageConsumer consumer) {
        return (ReactorSSE) super.addOnDataMatchesPrefix(valuePrefix, consumer);
    }

    @Override
    public ReactorSSE addOnDataMatchesPrefix(String valuePrefix, SSEStringMessageConsumerWithSink consumer) {
        return (ReactorSSE) super.addOnDataMatchesPrefix(valuePrefix, consumer);
    }

    @Override
    public ReactorSSE addOnDataMatchesPostfix(String valuePostfix, SSEStringMessageConsumer consumer) {
        return (ReactorSSE) super.addOnDataMatchesPostfix(valuePostfix, consumer);
    }

    @Override
    public ForestSSE addOnDataMatchesPostfix(String valuePostfix, SSEStringMessageConsumerWithSink consumer) {
        return (ReactorSSE) super.addOnDataMatchesPostfix(valuePostfix, consumer);
    }

    @Override
    public ReactorSSE addOnEvent(SSEStringMessageConsumer consumer) {
        return (ReactorSSE) super.addOnEvent(consumer);
    }

    @Override
    public ReactorSSE addOnEvent(SSEStringMessageConsumerWithSink consumer) {
        return (ReactorSSE) super.addOnEvent(consumer);
    }

    @Override
    public <T> ReactorSSE addOnEvent(Class<T> valueClass, SSEMessageConsumer<T> consumer) {
        return (ReactorSSE) super.addOnEvent(valueClass, consumer);
    }

    @Override
    public <T> ReactorSSE addOnEvent(Class<T> valueClass, SSEMessageConsumerWithSink<T> consumer) {
        return (ReactorSSE) super.addOnEvent(valueClass, consumer);
    }

    @Override
    public ReactorSSE addOnEventMatchesPrefix(String valuePrefix, SSEStringMessageConsumer consumer) {
        return (ReactorSSE) super.addOnEventMatchesPrefix(valuePrefix, consumer);
    }

    @Override
    public ReactorSSE addOnEventMatchesPrefix(String valuePrefix, SSEStringMessageConsumerWithSink consumer) {
        return (ReactorSSE) super.addOnEventMatchesPrefix(valuePrefix, consumer);
    }

    @Override
    public ReactorSSE addOnEventMatchesPostfix(String valuePostfix, SSEStringMessageConsumer consumer) {
        return (ReactorSSE) super.addOnEventMatchesPostfix(valuePostfix, consumer);
    }

    @Override
    public ReactorSSE addOnEventMatchesPostfix(String valuePostfix, SSEStringMessageConsumerWithSink consumer) {
        return (ReactorSSE) super.addOnEventMatchesPostfix(valuePostfix, consumer);
    }


    @Override
    public ReactorSSE addOnId(SSEStringMessageConsumer consumer) {
        return (ReactorSSE) super.addOnId(consumer);
    }

    @Override
    public ReactorSSE addOnId(SSEStringMessageConsumerWithSink consumer) {
        return (ReactorSSE) super.addOnId(consumer);
    }


    @Override
    public <T> ReactorSSE addOnId(Class<T> valueClass, SSEMessageConsumer<T> consumer) {
        return (ReactorSSE) super.addOnId(valueClass, consumer);
    }

    @Override
    public <T> ReactorSSE addOnId(Class<T> valueClass, SSEMessageConsumerWithSink<T> consumer) {
        return (ReactorSSE) super.addOnId(valueClass, consumer);
    }

    @Override
    public ReactorSSE addOnIdMatchesPrefix(String valuePrefix, SSEStringMessageConsumer consumer) {
        return (ReactorSSE) super.addOnIdMatchesPrefix(valuePrefix, consumer);
    }

    @Override
    public ReactorSSE addOnIdMatchesPrefix(String valuePrefix, SSEStringMessageConsumerWithSink consumer) {
        return (ReactorSSE) super.addOnIdMatchesPrefix(valuePrefix, consumer);
    }

    @Override
    public ReactorSSE addOnIdMatchesPostfix(String valuePostfix, SSEStringMessageConsumer consumer) {
        return (ReactorSSE) super.addOnIdMatchesPostfix(valuePostfix, consumer);
    }

    @Override
    public ReactorSSE addOnIdMatchesPostfix(String valuePostfix, SSEStringMessageConsumerWithSink consumer) {
        return (ReactorSSE) super.addOnIdMatchesPostfix(valuePostfix, consumer);
    }

    @Override
    public ReactorSSE addOnRetry(SSEStringMessageConsumer consumer) {
        return (ReactorSSE) super.addOnRetry(consumer);
    }

    @Override
    public ReactorSSE addOnRetry(SSEStringMessageConsumerWithSink consumer) {
        return (ReactorSSE) super.addOnRetry(consumer);
    }

    @Override
    public <T> ReactorSSE addOnRetry(Class<T> valueClass, SSEMessageConsumer<T> consumer) {
        return (ReactorSSE) super.addOnRetry(valueClass, consumer);
    }

    @Override
    public <T> ReactorSSE addOnRetry(Class<T> valueClass, SSEMessageConsumerWithSink<T> consumer) {
        return (ReactorSSE) super.addOnRetry(valueClass, consumer);
    }

    @Override
    public ReactorSSE addOnRetryMatchesPrefix(String valuePrefix, SSEStringMessageConsumer consumer) {
        return (ReactorSSE) super.addOnRetryMatchesPrefix(valuePrefix, consumer);
    }

    @Override
    public ReactorSSE addOnRetryMatchesPrefix(String valuePrefix, SSEStringMessageConsumerWithSink consumer) {
        return (ReactorSSE) super.addOnRetryMatchesPrefix(valuePrefix, consumer);
    }

    @Override
    public ReactorSSE addOnRetryMatchesPostfix(String valuePostfix, SSEStringMessageConsumer consumer) {
        return (ReactorSSE) super.addOnRetryMatchesPostfix(valuePostfix, consumer);
    }

    @Override
    public ReactorSSE addOnRetryMatchesPostfix(String valuePostfix, SSEStringMessageConsumerWithSink consumer) {
        return (ReactorSSE) super.addOnRetryMatchesPostfix(valuePostfix, consumer);
    }

    public <T> Flux<T> toFlux(TypeReference<T> typeReference) {
        return toFlux(typeReference.getType());
    }

    public <T> Flux<T> toFlux(Class<T> clazz) {
        return toFlux(ReflectUtils.toType(clazz));
    }


    public <T> Flux<T> toFlux(Type targetType) {
        return toFlux(SSELinesMode.AUTO, targetType);
    }

    public <T> Flux<T> toFlux(SSELinesMode mode, TypeReference<T> typeReference) {
        return toFlux(mode, typeReference.getType());
    }

    public <T> Flux<T> toFlux(SSELinesMode mode, Class<T> clazz) {
        return toFlux(mode, ReflectUtils.toType(clazz));
    }

    public <T> Flux<T> toFlux(SSELinesMode mode, Type targetType) {
        final ForestConfiguration configuration = getRequest().getConfiguration();
        final ForestConverter converter = configuration.getConverter(ForestDataType.AUTO);
        final ReactorSSE self = this;
        
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
            .listen(mode,data -> {
                try {
                    emitter.next((T) data);
                } catch (Exception e) {
                    emitter.error(e);
                }
            });
        });
    } 
    
}
