package mos.quarkus.play.controller;

import io.quarkus.qute.Location;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import io.smallrye.common.annotation.Blocking;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import mos.quarkus.play.service.CachingService;
import mos.quarkus.play.util.HttpHandler;

import java.net.URI;
import java.util.Date;

import static mos.quarkus.play.service.CachingService.USER_SESSION_OPENAPI_KEY;
import static mos.quarkus.play.util.HttpHandler.SESSION_COOKIE_NAME;

@Path("/openAI")
@Blocking
public class OpenAIController {


	@Inject
	HttpHandler httpHandler;

	@Inject
	CachingService cachingService;


	@Location("openAI/openAIStart.html")
	Template openAIStart;

	@Location("openAI/apiKey.html")
	Template apiKey;


	@Path("/openAIStart")
	@GET
	@Produces(MediaType.TEXT_HTML)
	public TemplateInstance start() {
		// Log.info(cachingService.getUserSessionValue("123", "test"));
		// httpHandler.setCookie("myTest", "1234", HttpHandler.DAY_IN_SECONDS);
		// httpHandler.removeCookie("myTest");
		String apiKey = getApiKey();
		if (apiKey == null) {
			redirect("/openAI/apiKey");
		}
		return openAIStart.data("apiKey", "todo");
	}


	@Path("/apiKey")
	@GET
	@Produces(MediaType.TEXT_HTML)
	public TemplateInstance apiKeyGet() {
		return apiKey.instance();
	}

	@Path("/apiKey")
	@POST
	@Produces(MediaType.TEXT_HTML)
	public TemplateInstance apiKeyPost() {
		return apiKey.instance();
	}




	@GET
	@Path("/set")
	public String set() {
		// httpHandler.setCookie("myTest", "1234", DAY_IN_SECONDS);
		cachingService.setUserSessionValue("123", "test", "hello mos " + new Date());
		return "ok";
	}


	//////

	private String getApiKey() {
		String sessionID = httpHandler.getCookieValue(SESSION_COOKIE_NAME);
		if (sessionID == null) {
			return null;
		}
		return cachingService.getUserSessionValue(sessionID, USER_SESSION_OPENAPI_KEY);

	}

	private void redirect(String uri) {
		throw new RedirectionException(Response.Status.FOUND, URI.create(uri));
	}

}
