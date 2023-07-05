package mos.quarkus.play.util;

import io.vertx.core.http.Cookie;
import io.vertx.core.http.CookieSameSite;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

@RequestScoped
public class HttpHandler {

	public static long DAY_IN_SECONDS = 24 * 60 * 60;     // check also cache invalidation:  quarkus.cache.caffeine.expire-after-access
	public static String SESSION_COOKIE_NAME = "USER_SESSION_PLAY";

	@Inject
	HttpServerRequest request;

	/**
	 * @param maxAge  in seconds
	 */
	public void setCookie(String key, String value, Long maxAge) {
		HttpServerResponse response = request.response();
		if (!response.headWritten()) {
			Cookie cookie = Cookie.cookie(key, value)
					.setPath("/")
					.setHttpOnly(true)
					.setSameSite(CookieSameSite.NONE)
					.setSecure(request.isSSL());
			if (maxAge != null) {
				cookie.setMaxAge(maxAge);
			}
			response.addCookie(cookie);
		}
	}

	/**
	 *  No-Expire-Date: Cookie should be deleted if browser is closed (in theory)
	 */
	public void setCookie(String key, String value) {
		setCookie(key, value, null);
	}

	public String getCookieValue(String key) {
		Cookie cookie = request.getCookie(key);
		if (cookie != null) {
			return cookie.getValue();
		}
		return null;
	}

	public void removeCookie(String key) {
		// request.response().removeCookies(key);
		// request.getCookie(key).setMaxAge(-10000);
		setCookie(key, "", 0L);
	}

}
