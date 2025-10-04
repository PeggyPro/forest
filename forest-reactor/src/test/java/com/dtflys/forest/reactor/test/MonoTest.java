package com.dtflys.forest.reactor.test;

import com.dtflys.forest.Forest;
import com.dtflys.forest.config.ForestConfiguration;
import com.dtflys.forest.utils.TypeReference;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.Rule;
import org.junit.Test;
import reactor.core.publisher.Mono;

import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;

public class MonoTest extends ForestClientTest {
    
    @Rule
    public final MockWebServer server = new MockWebServer();
    
    public MonoTest(String backendName, String jsonConverterName) {
        super(backendName, jsonConverterName, ForestConfiguration.createConfiguration());
    }
    
    @Test
    public void testMono() {
        server.enqueue(new MockResponse().setResponseCode(200)
                .setBody("{\"name\": \"a\"}"));

        AtomicReference<String> result = new AtomicReference<>();
        
        Forest.get("http://localhost:{}", server.getPort())
                .execute(new TypeReference<Mono<String>>() {})
                .subscribe(data -> {
                    System.out.println(data);
                    result.set(data);
                });
        assertThat(result.get()).isNotEmpty().isEqualTo("{\"name\": \"a\"}");
    }

    @Test
    public void testMono_result_type() {
        server.enqueue(new MockResponse().setResponseCode(200)
                .setBody("{\"name\": \"a\"}"));

        AtomicReference<MyName> result = new AtomicReference<>();

        Forest.get("http://localhost:{}", server.getPort())
                .execute(new TypeReference<Mono<MyName>>() {})
                .subscribe(data -> {
                    System.out.println(data);
                    result.set(data);
                });
        assertThat(result.get()).isNotNull();
        assertThat(result.get().getName()).isEqualTo("a");
    }

}
