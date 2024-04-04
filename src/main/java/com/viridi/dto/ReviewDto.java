package com.viridi.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDto {

	private long id;

	private Long rating;

	private String description;
	
	private List<String> images;

	private Long userId;

	private Long productId;
}
