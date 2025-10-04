package com.dtflys.forest.reactor.test;

import com.dtflys.forest.Forest;
import com.dtflys.forest.utils.TypeReference;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.Rule;
import org.junit.Test;
import reactor.core.publisher.Flux;

import java.util.concurrent.TimeUnit;

public class FluxTest {

    @Rule
    public final MockWebServer server = new MockWebServer();


    @Test
    public void testSSE() {
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
        Forest.get("http://localhost:" + server.getPort() + "/test")
                .sse()
                .setOnMessage(event -> {
                    System.out.println(event.rawData());
                })
                .listen();
//        Forest.get("http://localhost:{}", server.getPort())
//                .logEnabled(true)
//                .execute(new TypeReference<Flux<String>>() {})
//                .subscribe(data -> {
//                    System.out.println(data);
//                });
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


}
