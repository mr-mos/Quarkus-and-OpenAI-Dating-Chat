package mos.quarkus.play.controller;

import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import org.apache.commons.collections4.MapUtils;

import java.util.LinkedHashMap;
import java.util.Map;

@Path("/")
public class StartController {

	@Inject
	Template start;

	@GET
	@Produces(MediaType.TEXT_HTML)
	public TemplateInstance get(@QueryParam("name") String name) {
		Map<String, String> navigation = MapUtils.putAll(new LinkedHashMap(),
				new String[][]{
						{"Home", "/"},
						{"Hello World", "/hello"},
						{"Quarkus Info", "quarkus_default.html"}
				}
		);
		return start.data("navigation", navigation);
	}
}
