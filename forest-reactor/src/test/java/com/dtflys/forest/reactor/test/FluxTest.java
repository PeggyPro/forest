package com.dtflys.forest.reactor.test;

import com.dtflys.forest.Forest;
import com.dtflys.forest.config.ForestConfiguration;
import com.dtflys.forest.reactor.sse.ReactorSSE;
import com.dtflys.forest.sse.SSELinesMode;
import com.dtflys.forest.utils.TypeReference;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.Rule;
import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

public class FluxTest extends ForestClientTest {

    @Rule
    public final MockWebServer server = new MockWebServer();

    public FluxTest(String backendName, String jsonConverterName) {
        super(backendName, jsonConverterName, ForestConfiguration.createConfiguration());
    }

    @Test
    public void testFlux() {
        int len = "{\"name\": \"a\"}\n".getBytes().length;
        server.enqueue(new MockResponse().setResponseCode(200)
                .setBody(
                        "{\"name\": \"a\"}\n" +
                        "{\"name\": \"b\"}\n" +
                        "{\"name\": \"c\"}\n" +
                        "{\"name\": \"d\"}\n" +
                        "{\"name\": \"e\"}\n" +
                        "{\"name\": \"f\"}\n"
                ).throttleBody(len, 1, TimeUnit.SECONDS));
        Forest.get("http://localhost:{}", server.getPort())
                .execute(new TypeReference<Flux<String>>() {})
                .subscribe(System.out::print);
    }


    @Test
    public void testFlux_sse() {
        int len = "{\"name\": \"a\"}\n".getBytes().length;
        server.enqueue(new MockResponse().setResponseCode(200)
                        .setHeader("Content-Type", "text/event-stream")
                .setBody(
                "{\"name\": \"a\"}\n" +
                "{\"name\": \"b\"}\n" +
                "{\"name\": \"c\"}\n" +
                "{\"name\": \"d\"}\n" +
                "{\"name\": \"e\"}\n" +
                "{\"name\": \"f\"}\n"
        ).throttleBody(len, 1, TimeUnit.SECONDS));
        Forest.get("http://localhost:{}", server.getPort())
                .execute(new TypeReference<Flux<String>>() {})
                .subscribe(data -> {
                    System.out.print(data);
                });
    }


    @Test
    public void testFlux_sse_toFlux() {
        int len = "{\"name\": \"a\"}\n".getBytes().length;
        server.enqueue(new MockResponse().setResponseCode(200)
                .setBody(
                        "{\"name\": \"a\"}\n" +
                        "{\"name\": \"b\"}\n" +
                        "{\"name\": \"c\"}\n" +
                        "{\"name\": \"d\"}\n" +
                        "{\"name\": \"e\"}\n" +
                        "{\"name\": \"f\"}\n"
                ).throttleBody(len, 1, TimeUnit.SECONDS));
        StringBuffer buffer = new StringBuffer();
        
        Forest.get("http://localhost:{}", server.getPort())
                .sse(ReactorSSE.class)
                .setOnMessage((event, sink) -> {
                    final MyName myName = event.value(MyName.class);
                    sink.next(myName);
                })
                .toFlux(MyName.class)
                .subscribe(myName -> {
                    buffer.append("name -> " + myName.getName() + "\n");
                    System.out.println("name -> " + myName.getName());
                });
                
        assertThat(buffer.toString()).isEqualTo(
                "name -> a\n" +
                "name -> b\n" +
                "name -> c\n" +
                "name -> d\n" +
                "name -> e\n" +
                "name -> f\n"
        );
    }



    @Test
    public void testFlux_sse_json_type() {
        int len = "{\"name\": \"a\"}\n".getBytes().length;
        server.enqueue(new MockResponse().setResponseCode(200)
                .setHeader("Content-Type", "text/event-stream")
                .setBody(
                        "{\"name\": \"a\"}\n" +
                        "{\"name\": \"b\"}\n" +
                        "{\"name\": \"c\"}\n" +
                        "{\"name\": \"d\"}\n" +
                        "{\"name\": \"e\"}\n" +
                        "{\"name\": \"f\"}\n"
                ).throttleBody(len, 1, TimeUnit.SECONDS));
        Forest.get("http://localhost:{}", server.getPort())
                .logEnabled(true)
                .execute(new TypeReference<Flux<MyName>>() {})
                .subscribe(data -> {
                    System.out.println("name -> " + data.getName());
                });
    }

    @Test
    public void testFlux_sse_mono_json_type() {
        int len = "{\"name\": \"a\"}\n".getBytes().length;
        server.enqueue(new MockResponse().setResponseCode(200)
                .setHeader("Content-Type", "text/event-stream")
                .setBody(
                        "{\"name\": \"a\"}\n" +
                        "{\"name\": \"b\"}\n" +
                        "{\"name\": \"c\"}\n" +
                        "{\"name\": \"d\"}\n" +
                        "{\"name\": \"e\"}\n" +
                        "{\"name\": \"f\"}\n"
                ).throttleBody(len, 1, TimeUnit.SECONDS));
        Forest.get("http://localhost:{}", server.getPort())
                .logEnabled(true)
                .execute(new TypeReference<Flux<Mono<MyName>>>() {})
                .subscribe(data -> {
                    System.out.println("mono -> ");
                    data.subscribe(myName -> {
                        System.out.println("    name -> " + myName.getName());
                    });
                });
    }


    @Test
    public void testFlux_json_type() {
        int len = "{\"name\": \"a\"}".getBytes().length;
        server.enqueue(new MockResponse().setResponseCode(200)
                .setBody(
                        "{\"name\": \"a\"}"
                ).throttleBody(len, 1, TimeUnit.SECONDS));
        Forest.get("http://localhost:{}", server.getPort())
                .logEnabled(true)
                .execute(new TypeReference<Flux<MyName>>() {})
                .subscribe(data -> {
                    System.out.println("name -> " + data.getName());
                });
    }

}
