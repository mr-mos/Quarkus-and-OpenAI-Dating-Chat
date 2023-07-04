package mos.quarkus.play.util;

import io.vertx.core.http.Cookie;
import io.vertx.core.http.CookieSameSite;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

@RequestScoped
public class HttpHandler {

	@Inject
	HttpServerRequest request;

	public void setCookie(String key, String value) {
		HttpServerResponse response = request.response();
		if (!response.headWritten())
			response.addCookie(
					Cookie.cookie(key, value)
							.setPath("/")
							.setHttpOnly(true)
							.setSameSite(CookieSameSite.NONE)
							.setSecure(request.isSSL()));
	}

	public void removeCookie(String key) {
		// request.response().removeCookies(key);
		// request.getCookie(key).setMaxAge(-10000);
		HttpServerResponse response = request.response();
		response.addCookie(
				Cookie.cookie(key, "")
						.setPath("/")
						.setMaxAge(0)
		);
	}

}
