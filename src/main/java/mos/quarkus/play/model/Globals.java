package mos.quarkus.play.model;

import io.quarkus.arc.Arc;
import io.quarkus.qute.TemplateGlobal;
import mos.quarkus.play.service.NavigationService;

import java.util.Map;

@TemplateGlobal
public class Globals {


	static Map<String, String> navigation() {
		return Arc.container().instance(NavigationService.class).get().getNavigation();
	}

}
