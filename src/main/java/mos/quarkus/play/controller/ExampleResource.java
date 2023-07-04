package mos.quarkus.play.controller;

import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import mos.quarkus.play.service.ExampleService;

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
}


