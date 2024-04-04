package com.viridi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.viridi.service.AnalyticsService;

@RestController
@RequestMapping("/api/v1/analytics")
public class AnalyticsController {

	@Autowired
	private AnalyticsService analyticsService;
	
	
	@GetMapping
	public ResponseEntity<?> getAnalytics() {
		try {
			return ResponseEntity.ok(analyticsService.getAnalytics());
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>("Unable to get analytics", HttpStatus.BAD_REQUEST);
		}
	}
	
	
	
	
}
