package mos.quarkus.play.init;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Named;
import jakarta.validation.MessageInterpolator;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.util.Locale;

@ApplicationScoped
public class Producer {

	@Produces
	@Named("englishValidator")
	public Validator produceEnglishValidator() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();

		MessageInterpolator interpolator = new FixedLocaleMessageInterpolator(factory.getMessageInterpolator(), Locale.US); // Set your desired Locale here
		return factory.usingContext().messageInterpolator(interpolator).getValidator();
	}

}
