package mos.quarkus.play.controller;

import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import mos.quarkus.play.service.NavigationService;

@Path("")
public class OpenAIController {

	@Inject
	NavigationService navigationService;

	@Inject
	Template openAIStart;


	@GET
	@Produces(MediaType.TEXT_HTML)
	@Path("/openAIStart")
	public TemplateInstance form(@QueryParam("text") String text) {
		return openAIStart.data("defaultText", text, "navigation", navigationService.getNavigation());
	}

}
