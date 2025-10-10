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
                    System.out.println(data);
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
    public void testFlux_sse_toFlux_multilines() {
        int len = "{\"name\": \"a\"}\n".getBytes().length;
        server.enqueue(new MockResponse().setResponseCode(200)
                .setBody(
                        "id:1\n" +
                        "event:name\n" +
                        "data:a\n" +
                        "\n" +
                        "id:2\n" +
                        "event:name\n" +
                        "data:b\n" +
                        "\n" +
                        "id:3\n" +
                        "event:name\n" +
                        "data:c\n" +
                        "\n" +
                        "id:4\n" +
                        "event:name\n" +
                        "data:d\n" +
                        "\n" +
                        "id:5\n" +
                        "event:name\n" +
                        "data:e\n" +
                        "\n" +
                        "id:6\n" +
                        "event:name\n" +
                        "data:f\n"
                ).throttleBody(len, 1, TimeUnit.SECONDS));
        StringBuffer buffer = new StringBuffer();

        Forest.get("http://localhost:{}", server.getPort())
                .sse(ReactorSSE.class)
                .setOnMessage((event, sink) -> {
                    String id = event.id();
                    String name = event.data(String.class);
                    sink.next("id: " + id + ", name: " + name);
                })
                .toFlux(String.class)
                .subscribe(name -> {
                    buffer.append("data -> " + name + "\n");
                    System.out.println("data -> " + name);
                });

        assertThat(buffer.toString()).isEqualTo(
                "data -> id: 1, name: a\n" +
                "data -> id: 2, name: b\n" +
                "data -> id: 3, name: c\n" +
                "data -> id: 4, name: d\n" +
                "data -> id: 5, name: e\n" +
                "data -> id: 6, name: f\n"
        );
    }

    @Test
    public void testFlux_sse_toFlux_auto_multilines() {
        int len = ("id:1\n" +
                "event:name\n" +
                "data:a\n").getBytes().length;
        server.enqueue(new MockResponse().setResponseCode(200)
                .setBody(
                        "id:1\n" +
                        "event:name\n" +
                        "data:a\n" +
                        "id:2\n" +
                        "event:name\n" +
                        "data:b\n" +
                        "id:3\n" +
                        "event:name\n" +
                        "data:c\n" +
                        "id:4\n" +
                        "event:name\n" +
                        "data:d\n" +
                        "id:5\n" +
                        "event:name\n" +
                        "data:e\n" +
                        "id:6\n" +
                        "event:name\n" +
                        "data:f\n"
                ).throttleBody(len, 1, TimeUnit.SECONDS));
        StringBuffer buffer = new StringBuffer();

        Forest.get("http://localhost:{}", server.getPort())
                .sse(ReactorSSE.class)
                .setOnMessage((event, sink) -> {
                    String id = event.id();
                    String type = event.event();
                    String value = event.data(String.class);
                    sink.next("id: " + id + ", event: " + type + ", data: " + value);
                })
                .toFlux(String.class)
                .subscribe(name -> {
                    buffer.append("value -> " + name + "\n");
                    System.out.println("value -> " + name);
                });

        assertThat(buffer.toString()).isEqualTo(
                "value -> id: 1, event: name, data: a\n" +
                "value -> id: 2, event: name, data: b\n" +
                "value -> id: 3, event: name, data: c\n" +
                "value -> id: 4, event: name, data: d\n" +
                "value -> id: 5, event: name, data: e\n" +
                "value -> id: 6, event: name, data: f\n"
        );
    }


    @Test
    public void testFlux_sse_toFlux_auto_2lines() {
        int len = ("id:1\n" +
                "data:a\n").getBytes().length;
        server.enqueue(new MockResponse().setResponseCode(200)
                .setBody(
                        "id:1\n" +
                        "data:a\n" +
                        "id:2\n" +
                        "data:b\n" +
                        "id:3\n" +
                        "data:c\n" +
                        "id:4\n" +
                        "data:d\n" +
                        "id:5\n" +
                        "data:e\n" +
                        "id:6\n" +
                        "data:f\n"
                ).throttleBody(len, 1, TimeUnit.SECONDS));
        StringBuffer buffer = new StringBuffer();

        Forest.get("http://localhost:{}", server.getPort())
                .sse(ReactorSSE.class)
                .setOnMessage((event, sink) -> {
                    String id = event.id();
                    String value = event.data(String.class);
                    sink.next("id: " + id + ", data: " + value);
                })
                .toFlux(String.class)
                .subscribe(name -> {
                    buffer.append("value -> " + name + "\n");
                    System.out.println("value -> " + name);
                });

        assertThat(buffer.toString()).isEqualTo(
                "value -> id: 1, data: a\n" +
                "value -> id: 2, data: b\n" +
                "value -> id: 3, data: c\n" +
                "value -> id: 4, data: d\n" +
                "value -> id: 5, data: e\n" +
                "value -> id: 6, data: f\n"
        );
    }


    @Test
    public void testFlux_sse_toFlux_auto_singleLine() {
        int len = ("data:a\n").getBytes().length;
        server.enqueue(new MockResponse().setResponseCode(200)
                .setBody(
                    "data:a\n" +
                    "data:b\n" +
                    "data:c\n" +
                    "data:d\n" +
                    "data:e\n" +
                    "data:f\n"
                ).throttleBody(len, 1, TimeUnit.SECONDS));
        StringBuffer buffer = new StringBuffer();

        Forest.get("http://localhost:{}", server.getPort())
                .sse(ReactorSSE.class)
                .setOnMessage((event, sink) -> {
                    String data = event.data(String.class);
                    sink.next("data: " + data);
                })
                .toFlux(String.class)
                .subscribe(name -> {
                    buffer.append("value -> " + name + "\n");
                    System.out.println("value -> " + name);
                });

        assertThat(buffer.toString()).isEqualTo(
                "value -> data: a\n" +
                "value -> data: b\n" +
                "value -> data: c\n" +
                "value -> data: d\n" +
                "value -> data: e\n" +
                "value -> data: f\n"
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
