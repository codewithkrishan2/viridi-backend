package com.viridi.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.viridi.entity.CartItems;
import com.viridi.entity.User;
import com.viridi.enums.OrderStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrdersDto {

private Long id;
	
	private String description;
	
	private LocalDateTime orderedDate;
	
	private Double amount; //total amount after applying & discount
	
	private Double totalAmount; //total amount before applying & discount
	
	private Double discountedAmount;
	
	private String address;
	
	private OrderStatus status;
	
	private UUID trackingId;
	
	private String userEmail;
	
	private List<CartItemsDto> cartItems;
}
