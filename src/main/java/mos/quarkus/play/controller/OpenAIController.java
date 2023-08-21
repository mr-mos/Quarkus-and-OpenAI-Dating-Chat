package mos.quarkus.play.controller;

import io.quarkus.logging.Log;
import io.quarkus.qute.Location;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import io.smallrye.common.annotation.Blocking;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import mos.quarkus.play.model.ApiKey;
import mos.quarkus.play.service.CachingService;
import mos.quarkus.play.util.HttpHandler;

import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static mos.quarkus.play.service.CachingService.USER_SESSION_OPENAPI_KEY;
import static mos.quarkus.play.util.HttpHandler.SESSION_COOKIE_NAME;

@Path("/openAI")
@Blocking
public class OpenAIController {


	@Inject
	HttpHandler httpHandler;

	@Inject
	CachingService cachingService;

	@Inject
	@Named("englishValidator")
	Validator validator;


	@Location("openAI/openAIStart.html")
	Template openAIStartTemplate;

	@Location("openAI/apiKey.html")
	Template apiKeyTemplate;


	@Path("/openAIStart")
	@GET
	@Produces(MediaType.TEXT_HTML)
	public TemplateInstance start() {
		// Log.info(cachingService.getUserSessionValue("123", "test"));
		// httpHandler.setCookie("myTest", "1234", HttpHandler.DAY_IN_SECONDS);
		// httpHandler.removeCookie("myTest");
		String apiKey = getApiKeyTemplate();
		if (apiKey == null) {
			redirect("/openAI/apiKey");
		}
		return openAIStartTemplate.data("apiKey", "todo");
	}


	@Path("/apiKey")
	@GET
	@Produces(MediaType.TEXT_HTML)
	public TemplateInstance apiKeyGet() {
		return apiKeyTemplate.instance();
	}


//	@Path("/apiKey")
//	@POST
//	@Produces(MediaType.TEXT_HTML)
//	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
//	public TemplateInstance apiKeyPost(@Valid ApiKey key) {
//		return apiKey.data("apiKey",key);
//	}


	@Path("/apiKey")
	@POST
	@Produces(MediaType.TEXT_HTML)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public TemplateInstance apiKeyPost(@FormParam("key") String key) {
		ApiKey apiKey = new ApiKey(key);
		List<String> errorMessages = validate(apiKey);
		if (errorMessages == null) {
			Log.info("Store api key!");
			redirect("/openAI/openAIStart");
		}
		Log.warn("Validation error: " + errorMessages);
		return apiKeyTemplate.data("key", key, "apiKeyErros", String.join(" AND ", errorMessages));
	}


	@GET
	@Path("/set")
	public String set() {
		// httpHandler.setCookie("myTest", "1234", DAY_IN_SECONDS);
		cachingService.setUserSessionValue("123", "test", "hello mos " + new Date());
		return "ok";
	}


	//////

	private List<String> validate(Object bean) {
		Set<ConstraintViolation<Object>> violations = validator.validate(bean);
		if (violations.isEmpty()) {
			return null;
		} else {
			// There were validation errors
			List<String> validationErrors = new ArrayList<>();
			for (ConstraintViolation<?> violation : violations) {
				validationErrors.add(violation.getPropertyPath() + ": " + violation.getMessage());
			}
			return validationErrors;
		}
	}

	private String getApiKeyTemplate() {
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
