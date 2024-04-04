package com.viridi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnalyticsResponse {

	
	private Long placed;
	
	private Long shipped;
	
	private Long delivered;
	
	private Long cancelled;
	
	
	private Long currentMonthOrders;
	
	private Long previousMonthOrders;
	
	private Double currentMonthEarnings;
	
	private Double previousMonthEarnings;
	
	
	//private Long totalAmount;
	
	//private Long totalDiscount;
	
	//private Long totalProfit;
	
	//private Long totalSales;
	
	//private Long totalOrders;
		
}
