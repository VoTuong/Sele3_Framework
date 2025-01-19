package org.tvd.projects.vietjet.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketModel {
	String ticketType;
	String flightFrom;
	String flightTo;
	PassengerCount passengerCount;

}
