package mos.quarkus.play.service;

import io.quarkus.cache.CacheInvalidate;
import io.quarkus.cache.CacheKey;
import io.quarkus.cache.CacheResult;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 *  Play with the caching functionality and use it as a pseudo user-web-session
 */
@ApplicationScoped
public class CachingService {

	public static String USER_SESSION_OPENAPI_KEY = "user_session_openapi_key";
	public static String USER_SESSION_CHAT_GOAL = "user_session_chat_goal";
	public static String USER_SESSION_CHAT_LIST = "user_session_chat_list";

	public String createNewSession() {
		String sessionId = UUID.randomUUID().toString();
		readWriteUserSessionValue(sessionId, new HashMap<String, Object>());
		return sessionId;
	}

	public <T> void setUserSessionValue(String sessionId, String key, T value) {
		Log.info("Setting key '"+key+"' for user-session: " + sessionId/* + "with value: "+value*/);
		Map<String, Object> sessionMap = readWriteUserSessionValue(sessionId, null);
		invalidateUserSession(sessionId);
		if (sessionMap == null) {
			sessionMap = readWriteUserSessionValue(sessionId, new HashMap<>());
		}
		sessionMap.put(key, value);
		readWriteUserSessionValue(sessionId, sessionMap);
	}

	public <T> T getUserSessionValue(String sessionId, String key) {
		Map<String, Object> sessionMap = readWriteUserSessionValue(sessionId, null);
		if (sessionMap == null) {
			return null;
		}
		return (T) sessionMap.get(key);
	}


	@CacheResult(cacheName = "user-session")
	protected Map<String,Object> readWriteUserSessionValue(@CacheKey String sessionId, Map<String,Object> value) {
		return value;
	}

	@CacheInvalidate(cacheName = "user-session")
	protected void invalidateUserSession(String sessionId) {
	}

}
