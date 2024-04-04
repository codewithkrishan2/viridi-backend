package com.viridi.service.impl;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.viridi.dto.AnalyticsResponse;
import com.viridi.entity.Orders;
import com.viridi.enums.OrderStatus;
import com.viridi.repo.OrdersRepo;
import com.viridi.service.AnalyticsService;

@Service
public class AnalyticsServiceImpl implements AnalyticsService {

	
	@Autowired
	private OrdersRepo ordersRepo;
	
	@Autowired
	private ModelMapper modelMapper;
	
	
	@Override
	public AnalyticsResponse getAnalytics() {
		
		LocalDate currentDate = LocalDate.now();
		
		LocalDate previousMonthDate = currentDate.minusMonths(1);
		
		Long currentMonthOrders = getTotalOrdersForMonth(currentDate.getMonthValue(), currentDate.getYear());
		Long previousMonthOrders = getTotalOrdersForMonth(previousMonthDate.getMonthValue(), previousMonthDate.getYear());
		
		
		Double currentMonthEaring = getTotalEarningsForMonth(currentDate.getMonthValue(), currentDate.getYear());
		
		Double previousMonthEaring = getTotalEarningsForMonth(previousMonthDate.getMonthValue(), previousMonthDate.getYear());
		
		
		Long placed = ordersRepo.countByStatus(OrderStatus.PLACED);
		
		Long shipped = ordersRepo.countByStatus(OrderStatus.SHIPPED);
		
		Long delivered = ordersRepo.countByStatus(OrderStatus.DELIVERED);
		
		Long cancelled = ordersRepo.countByStatus(OrderStatus.CANCELLED);
		
		
		AnalyticsResponse analyticsResponse = new AnalyticsResponse();
		analyticsResponse.setPlaced(placed);
		analyticsResponse.setShipped(shipped);
		analyticsResponse.setDelivered(delivered);
		analyticsResponse.setCancelled(cancelled);
		
		analyticsResponse.setCurrentMonthOrders(currentMonthOrders);
		analyticsResponse.setPreviousMonthOrders(previousMonthOrders);
		analyticsResponse.setCurrentMonthEarnings(currentMonthEaring);
		analyticsResponse.setPreviousMonthEarnings(previousMonthEaring);
		
		return analyticsResponse;
	}

	public Long getTotalOrdersForMonth(int month, int year) {
		
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month - 1);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		
		Date startOfMonth = calendar.getTime();
		
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		
		Date endOfMonth = calendar.getTime();
		
		List<Orders> orders = ordersRepo.findByOrderedDateBetweenAndStatus(startOfMonth, endOfMonth, OrderStatus.DELIVERED);
		
		Long totalOrders = (long) orders.size();
		
		return totalOrders;
	}

	public Double getTotalEarningsForMonth(int month, int year) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month - 1);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		
		Date startOfMonth = calendar.getTime();
		
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		
		Date endOfMonth = calendar.getTime();
		
		List<Orders> orders = ordersRepo.findByOrderedDateBetweenAndStatus(startOfMonth, endOfMonth, OrderStatus.DELIVERED);
		
		Double sum = 0.0;
		for (Orders order : orders) {
			sum += order.getAmount();
		}
		
		return sum;
	}
}
