package mos.quarkus.play.controller;

import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

import java.util.Map;

@Path("/")
public class StartController {


	@Inject
	Template start;

	@GET
	public TemplateInstance home() {
		return start.data("testData", Map.of("name","Mr. Mos"));
	}
}
