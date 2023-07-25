package mos.quarkus.play.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ApiKey {

	private String value;

	public ApiKey() {
	}
	public ApiKey(String value) {
		this.value = value;
	}

	@NotBlank
	@Size(min = 50, max=120)
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
