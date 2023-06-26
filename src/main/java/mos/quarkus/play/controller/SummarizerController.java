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
public class SummarizerController {

	@Inject
	NavigationService navigationService;

	@Inject
	Template summarizeForm;


	@GET
	@Produces(MediaType.TEXT_HTML)
	@Path("/summarizer")
	public TemplateInstance form(@QueryParam("text") String text) {
		return summarizeForm.data("defaultText", text, "navigation", navigationService.getNavigation());
	}

}
