package mos.quarkus.play;

import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.configuration.ConfigUtils;
import io.quarkus.runtime.configuration.ProfileManager;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class ExampleService {

	@ConfigProperty(name = "quarkus.application.version")
	String applicationVersion;

	@ConfigProperty(name = "quarkus.application.name")
	String applicationName;

	@ConfigProperty(name = "platform.quarkus.native.builder-image")
	String nativeBuildImage;


	public String generateText() {
		return "Hello World! Quarkus is running. Environment: \n\n" +
				"Profile: " + ConfigUtils.getProfiles() + "\n" +
				"Application name: " + applicationName + "\n" +
				"Application version: " + applicationVersion + "\n" +
				"NativeBuildImage: " + nativeBuildImage;
	}
}
