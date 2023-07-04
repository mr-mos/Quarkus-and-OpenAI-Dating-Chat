package mos.quarkus.play.controller;

import io.quarkus.qute.Location;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import mos.quarkus.play.util.HttpHandler;

@Path("/openAI")
public class OpenAIController {

	@Inject
	HttpHandler httpHandler;

	@Location("openAI/openAIStart.html")
	Template openAIStart;


	@GET
	@Produces(MediaType.TEXT_HTML)
	@Path("/openAIStart")
	public TemplateInstance start() {
		// httpHandler.setCookie("myTest","1234");
		// httpHandler.removeCookie("myTest");
		return openAIStart.data("apiKey", "todo");
	}

}
