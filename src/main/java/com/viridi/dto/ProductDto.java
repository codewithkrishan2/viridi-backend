package com.viridi.dto;

import java.util.List;

import jakarta.persistence.ElementCollection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ProductDto {

	private Long id;
	
	private String productName;
	
	private String productDescription;
	
	private Double productPrice;
	
	private Double productDiscountedPrice;
	
    private List<String> productImages;
	
	private String productCategory;

	
}
