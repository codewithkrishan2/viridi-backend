package com.viridi.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.viridi.enums.OrderStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "orders")
public class Orders {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String description;
	
	private String userName;
	
	private Long contactNumber;
	
	private Date orderedDate;
	
	private Double amount; //total amount after applying & discount
	
	private Double totalAmount; //total amount before applying & discount
	
	private Double discountedAmount;
	
	private String address;
	
	@Enumerated(value = EnumType.STRING)
	private OrderStatus status;
	
	private UUID trackingId;
	
	@ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	private User user;
	
	@OneToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
	@JoinColumn(name = "coupon_id", referencedColumnName = "id")
	private Coupon coupon;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "orders")
	private List<CartItems> cartItems = new ArrayList<>();
	
	public void setUserName(String userName) {
		this.userName = user.getFirstName() + " " + user.getLastName();
	}
	
	
}
