package mos.quarkus.play.controller;

import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("")
public class OpenAIController {


	@Inject
	Template openAIStart;


	@GET
	@Produces(MediaType.TEXT_HTML)
	@Path("/openAIStart")
	public TemplateInstance start() {
		return openAIStart.instance();
	}

}
