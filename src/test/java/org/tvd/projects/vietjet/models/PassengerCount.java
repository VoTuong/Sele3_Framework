package org.tvd.projects.vietjet.models;

import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class PassengerCount {
	private int adults;

	public PassengerCount(int adults) {
		this.adults = adults;
	}
//	int children;
//	int infants;
}
