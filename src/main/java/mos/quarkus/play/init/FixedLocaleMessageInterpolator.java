package mos.quarkus.play.init;

import jakarta.validation.MessageInterpolator;

import java.util.Locale;

public class FixedLocaleMessageInterpolator implements MessageInterpolator {
	private final MessageInterpolator defaultInterpolator;
	private final Locale locale;

	public FixedLocaleMessageInterpolator(MessageInterpolator interpolator, Locale locale) {
		this.defaultInterpolator = interpolator;
		this.locale = locale;
	}

	@Override
	public String interpolate(String messageTemplate, Context context) {
		return defaultInterpolator.interpolate(messageTemplate, context, this.locale);
	}

	@Override
	public String interpolate(String messageTemplate, Context context, Locale locale) {
		return defaultInterpolator.interpolate(messageTemplate, context, this.locale);
	}

}
