package mos.quarkus.play.service;

import io.quarkus.runtime.configuration.ConfigUtils;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class ExampleService {

	@ConfigProperty(name = "quarkus.application.version")
	String applicationVersion;

	@ConfigProperty(name = "quarkus.application.name")
	String applicationName;

	@ConfigProperty(name = "platform.quarkus.native.builder-image")
	String nativeBuildImage;

	@ConfigProperty(name = "quarkus.framework.version")
	String quarkusFrameworkVersion;


	public String generateText() {
		return "Hello World! Quarkus is running. \n\n" +
				"Quarkus Version: " + quarkusFrameworkVersion + "\n" +
				"Profile: " + ConfigUtils.getProfiles() + "\n" +
				"Application name: " + applicationName + "\n" +
				"Application version: " + applicationVersion + "\n" +
				"NativeBuildImage: " + nativeBuildImage;
	}
}
