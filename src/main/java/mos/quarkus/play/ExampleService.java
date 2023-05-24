package mos.quarkus.play;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ExampleService {

	public String generateText() {
		return "Hello World! Quarkus is coming...";
	}
}
