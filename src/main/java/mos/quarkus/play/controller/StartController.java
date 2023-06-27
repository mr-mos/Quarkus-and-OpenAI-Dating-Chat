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
import org.apache.commons.collections4.MapUtils;

import java.util.LinkedHashMap;
import java.util.Map;

@Path("/")
public class StartController {

	@Inject
	NavigationService navigationService;

	@Inject
	Template start;

	@GET
	public TemplateInstance home() {
		return start.data("navigation", navigationService.getNavigation(), "testData", Map.of("name","Mr. Mos"));
	}
}
