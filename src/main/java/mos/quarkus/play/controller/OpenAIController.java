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
import mos.quarkus.play.defs.ChatGoal;
import mos.quarkus.play.model.ApiKey;
import mos.quarkus.play.service.CachingService;
import mos.quarkus.play.util.HttpHandler;

import java.net.URI;
import java.util.*;

import static mos.quarkus.play.service.CachingService.USER_SESSION_CHAT_GOAL;
import static mos.quarkus.play.service.CachingService.USER_SESSION_OPENAPI_KEY;
import static mos.quarkus.play.util.HttpHandler.DAY_IN_SECONDS;
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


    @Location("openAI/apiKey.html")
    Template apiKeyTemplate;

    @Location("openAI/openAIChat.html")
    Template openAIChatTemplate;


    @Path("/openAIChat")
    @GET
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance start() {
        String apiKey = checkForApiKey();
        return openAIChatTemplate.data("apiKey", halfLength(apiKey));
    }


    @Path("/apiKey")
    @GET
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance apiKeyGet() {
        return apiKeyTemplate.instance();
    }


    @Path("/apiKey")
    @POST
    @Produces(MediaType.TEXT_HTML)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public TemplateInstance apiKeyPost(@FormParam("key") String key) {
        ApiKey apiKey = new ApiKey(key);
        List<String> errorMessages = validate(apiKey);
        if (errorMessages == null) {
            Log.info("Store api key!");
            setSessionValue(USER_SESSION_OPENAPI_KEY, apiKey.getValue());
            redirect("/openAI/openAIChat");
        }
        Log.warn("Validation error: " + errorMessages);
        return apiKeyTemplate.data("key", key, "apiKeyErrors", String.join(" AND ", errorMessages));
    }


    @Path("/chatGoalDefinition")
    @POST
    @Produces(MediaType.TEXT_HTML)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public TemplateInstance chatGoalDefinition(@FormParam("goal") ChatGoal goal) {
        String apiKey = checkForApiKey();
        Log.info("Setting chat goal to: " + goal);
        setSessionValue(USER_SESSION_CHAT_GOAL, goal);
        redirect("/openAI/chatting");
        return null;            // should be never reached because of the redirect before
    }


    @Path("/chatting")
    @GET
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance chattingShow() {
        return generateChatTemplate(null);
    }

    @Path("/chatting")
    @POST
    @Produces(MediaType.TEXT_HTML)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public TemplateInstance chattingPost(@FormParam("otherMessage") String otherMessage) {
        if (otherMessage == null || otherMessage.trim().length() < 5) {
            return generateChatTemplate(Collections.singletonMap("formErrors","Please enter the last message of you chat partner above. Needs to be >= 5 characters!"));
        }
        redirect("/openAI/chatting");
        return null;            // should be never reached because of the redirect before
    }


    //////

    private TemplateInstance generateChatTemplate(Map<String,Object> moreModelValues) {
        String apiKey = checkForApiKey();
        ChatGoal chatGoal = getSessionValue(USER_SESSION_CHAT_GOAL);
        Map<String, Object> model = new HashMap<>();
        model.put("apiKey", halfLength(apiKey));
        model.put("chatGoal", chatGoal.name().toLowerCase());
        TemplateInstance templateInstance = openAIChatTemplate.data(
                "apiKey", halfLength(apiKey),
                "chatGoal", chatGoal.name().toLowerCase()
        );
        if (moreModelValues != null) {
            for (Map.Entry<String, Object> entry : moreModelValues.entrySet()) {
                templateInstance.data(entry.getKey(), entry.getValue());
            }
        }
        return templateInstance;
    }


    private String checkForApiKey() {
        String apiKey = getSessionValue(USER_SESSION_OPENAPI_KEY);
        if (apiKey == null) {
            Log.info("No APIKey in Session!");
            redirect("/openAI/apiKey");   // throws a RedirectException
        }
        return apiKey;
    }


    private String halfLength(String org) {
        int apiKeyLength = org.length();
        int substringLength = apiKeyLength / 2;
        int startCutPosition = substringLength / 2;
        int endCutPosition = startCutPosition + substringLength;
        return org.substring(0, startCutPosition) + " ... " + org.substring(endCutPosition);
    }


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


    private <T> void setSessionValue(String name, T value) {
        String sessionId = httpHandler.getCookieValue(SESSION_COOKIE_NAME);
        if (sessionId == null) {
            sessionId = UUID.randomUUID().toString();
            httpHandler.setCookie(SESSION_COOKIE_NAME, sessionId, DAY_IN_SECONDS);
        }
        cachingService.setUserSessionValue(sessionId, name, value);
    }

    private <T> T getSessionValue(String name) {
        String sessionID = httpHandler.getCookieValue(SESSION_COOKIE_NAME);
        if (sessionID == null) {
            return null;
        }
        return cachingService.getUserSessionValue(sessionID, name);
    }

    private void redirect(String uri) {
        throw new RedirectionException(Response.Status.FOUND, URI.create(uri));
    }


    /////////

    // Log.info(cachingService.getUserSessionValue("123", "test"));
    // httpHandler.setCookie("myTest", "1234", HttpHandler.DAY_IN_SECONDS);
    // httpHandler.removeCookie("myTest");

}
