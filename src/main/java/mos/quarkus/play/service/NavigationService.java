package mos.quarkus.play.service;

import io.quarkus.arc.Unremovable;
import jakarta.enterprise.context.ApplicationScoped;
import org.apache.commons.collections4.MapUtils;

import java.util.LinkedHashMap;
import java.util.Map;

@ApplicationScoped
@Unremovable
public class NavigationService {

	public Map<String, String> getNavigation() {
		return MapUtils.putAll(new LinkedHashMap<>(),
				new String[][]{
						{"Home", "/"},
						{"Hello World", "/hello"},
						{"<b>ChatGPT &amp; OpenAI</b>", "/openAI/openAIStart"},
						{"Quarkus Info", "/quarkus_default.html"}
				}
		);
	}

}
