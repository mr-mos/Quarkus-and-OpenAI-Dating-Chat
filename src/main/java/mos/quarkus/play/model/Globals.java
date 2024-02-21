package mos.quarkus.play.model;

import io.quarkus.arc.Arc;
import io.quarkus.qute.TemplateExtension;
import io.quarkus.qute.TemplateGlobal;
import mos.quarkus.play.defs.ChatGoal;
import mos.quarkus.play.service.NavigationService;

import java.util.Map;

public class Globals {


	@TemplateGlobal
	static Map<String, String> navigation() {
		return Arc.container().instance(NavigationService.class).get().getNavigation();
	}

	@TemplateExtension(namespace = "ChatGoal", matchName = "*")
	static ChatGoal enumValue(String value) {
		return ChatGoal.valueOf(value);
	}

}
