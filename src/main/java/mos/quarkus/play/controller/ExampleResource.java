package mos.quarkus.play.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import mos.quarkus.play.service.ExampleService;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Path("/hello")
public class ExampleResource {

    ExampleService exampleService;

    public ExampleResource(ExampleService exampleService) {
        this.exampleService = exampleService;
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return exampleService.generateText();
    }


    private ExecutorService executor = Executors.newFixedThreadPool(10);

    @Path("reactive")
    @GET
    @Produces(MediaType.TEXT_HTML)
    public Uni<String> reactive() {
        long start = System.currentTimeMillis();
        return Uni.createFrom().completionStage(
                CompletableFuture.supplyAsync(() -> computeGreeting("Reactive", start), executor));
//		return Uni.createFrom().item(() -> computeGreeting("Reactive", start));   // Bad: Runs on the vert.x-eventloop-thread; could lead to "The current thread cannot be blocked: vert.x-eventloop-thread-1"
    }

    @Path("blocking")
    @GET
    public String blocking() {
        return computeGreeting("Blocking", System.currentTimeMillis());
    }


    private String computeGreeting(String name, long start) {
        // Simulate a CPU-intensive computation
/*		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
		String info = "Processing in Milli-Seconds:" + (System.currentTimeMillis()-start) + " on thread: " + Thread.currentThread().getName();
		return String.format("Hello <strong>%s</strong>!<pre>\n\n%s\n</pre>", name, info);*/
        Uni<String> uni = Uni.createFrom().item(() -> {
            try {
                Thread.sleep(1000);  // Simulate a delay
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            String info = "Processing in Milli-Seconds:" + (System.currentTimeMillis() - start) + " on thread: " + Thread.currentThread().getName();
            return String.format("Hello <strong>%s</strong>!<pre>\n\n%s\n</pre>", name, info);
        });

        return uni.await().indefinitely();
    }


    @Path("testOkHttp")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String testOkHttp() throws IOException {
        final OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.github.com/rate_limit")
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful() || response.body() == null) throw new IOException("Unexpected code " + response);
            return testGson(response.body().string());
        }
    }

    private String testGson(String orgJson) {
        Gson gson = new Gson();
        Type mapType = new TypeToken<Map<String, Object>>() {}.getType();
        Map<String, Object> map = gson.fromJson(orgJson, mapType);
        map.put("From Mos", "a new test value");
        return gson.toJson(map);
    }



}


