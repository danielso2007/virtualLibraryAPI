package br.com.virtuallibrary.enums;

public enum Roles {

	ADMIN("Administrador");

	private String description;

	private Roles(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

}
