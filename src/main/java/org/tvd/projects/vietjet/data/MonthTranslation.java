package org.tvd.projects.vietjet.data;

public enum MonthTranslation {
	JANUARY("January", "Tháng 01"),
	FEBRUARY("February", "Tháng 02"),
	MARCH("March", "Tháng 03"),
	APRIL("April", "Tháng 04"),
	MAY("May", "Tháng 05"),
	JUNE("June", "Tháng 06"),
	JULY("July", "Tháng 07"),
	AUGUST("August", "Tháng 08"),
	SEPTEMBER("September", "Tháng 09"),
	OCTOBER("October", "Tháng 10"),
	NOVEMBER("November", "Tháng 11"),
	DECEMBER("December", "Tháng 12");

	private final String english;
	private final String vietnamese;

	MonthTranslation(String english, String vietnamese) {
		this.english = english;
		this.vietnamese = vietnamese;
	}

	public static String getOriginalName(String month, String language) {
		for (MonthTranslation translation : values()) {
			if ("vi".equalsIgnoreCase(language) && translation.vietnamese.equalsIgnoreCase(month)) {
				return translation.english;
			} else if ("en".equalsIgnoreCase(language) && translation.english.equalsIgnoreCase(month)) {
				return translation.vietnamese;
			}
		}
		throw new IllegalArgumentException("Invalid month: " + month);
	}

}
