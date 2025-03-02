package org.tvd.projects.leapfrog.model;

import lombok.Data;

@Data
public class GameData {
	private final String title;
	private final String age;
	private final String price;

	public GameData(String title, String age, String price) {
		this.title = title;
		this.age = age;
		this.price = price;
	}

	@Override
	public String toString() {
		return "GameData{" +
				"title='" + title + '\'' +
				", age='" + age + '\'' +
				", price='" + price + '\'' +
				'}';
	}
}
