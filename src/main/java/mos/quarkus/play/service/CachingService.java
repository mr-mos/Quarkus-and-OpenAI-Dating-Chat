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

	public String createNewSession() {
		String sessionId = UUID.randomUUID().toString();
		readWriteUserSessionValue(sessionId, new HashMap<String, String>());
		return sessionId;
	}

	public void setUserSessionValue(String sessionId, String key, String value) {
		Log.info("Setting key '"+key+"' for user-session: " + sessionId/* + "with value: "+value*/);
		Map<String, String> sessionMap = readWriteUserSessionValue(sessionId, null);
		invalidateUserSession(sessionId);
		if (sessionMap == null) {
			sessionMap = readWriteUserSessionValue(sessionId, new HashMap<String, String>());
		}
		sessionMap.put(key, value);
		readWriteUserSessionValue(sessionId, sessionMap);
	}

	public String getUserSessionValue(String sessionId, String key) {
		Map<String, String> sessionMap = readWriteUserSessionValue(sessionId, null);
		if (sessionMap == null) {
			return null;
		}
		return sessionMap.get(key);
	}


	@CacheResult(cacheName = "user-session")
	protected Map<String,String> readWriteUserSessionValue(@CacheKey String sessionId, Map<String,String> value) {
		return value;
	}

	@CacheInvalidate(cacheName = "user-session")
	protected void invalidateUserSession(String sessionId) {
	}

}
