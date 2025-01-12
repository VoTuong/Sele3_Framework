package org.tvd.projects.vietjet.models;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketModel {
	String ticketType;
	String flightFrom;
	String flightTo;
	PassengerCount passengerCount;

}
