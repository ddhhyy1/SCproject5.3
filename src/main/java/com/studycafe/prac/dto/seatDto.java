package com.studycafe.prac.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class seatDto {
	
	private int tempUsingNo;
	private int seatNo;
	private String userId;
	private String ticketName;
	private String selectedDate;
	private String startTime;
	private String endTime;	
	private String salesNo;

}
