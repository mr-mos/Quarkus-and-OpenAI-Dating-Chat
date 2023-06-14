package mos.quarkus.play.controller;

import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

@Path("/start")
public class StartController {

	@Inject
	Template start;

	@GET
	@Produces(MediaType.TEXT_HTML)
	public TemplateInstance get(@QueryParam("name") String name) {
		return start.data("name", name);
	}
}
