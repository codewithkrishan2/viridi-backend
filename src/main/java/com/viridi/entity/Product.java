package com.viridi.entity;

import java.util.List;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Product {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String productName;
	
	private String productDescription;
	
	private Double productPrice;
	
	private Double productDiscountedPrice;
	
	@ElementCollection
    private List<String> productImages;
	
	private String productCategory;
	
	
}
