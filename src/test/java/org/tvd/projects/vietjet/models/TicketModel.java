package org.tvd.projects.vietjet.models;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class TicketModel {
	String ticketType;
	String flightFrom;
	String flightTo;
	PassengerCount passengerCount;

	public TicketModel(String ticketType, String flightFrom, String flightTo, PassengerCount passengerCount) {
		this.ticketType = ticketType;
		this.flightFrom = flightFrom;
		this.flightTo = flightTo;
		this.passengerCount = passengerCount;
	}
}
