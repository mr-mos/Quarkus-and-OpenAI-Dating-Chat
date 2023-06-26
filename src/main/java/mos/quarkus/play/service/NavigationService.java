package mos.quarkus.play.service;

import jakarta.enterprise.context.ApplicationScoped;
import org.apache.commons.collections4.MapUtils;

import java.util.LinkedHashMap;
import java.util.Map;

@ApplicationScoped
public class NavigationService {

	public Map<String, String> getNavigation() {
		return MapUtils.putAll(new LinkedHashMap(),
				new String[][]{
						{"Home", "/"},
						{"Hello World", "/hello"},
						{"<b>Summarize with ChatGPT</b>", "/summarizer"},
						{"Quarkus Info", "quarkus_default.html"}
				}
		);
	}

}
