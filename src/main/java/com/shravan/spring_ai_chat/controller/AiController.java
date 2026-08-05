package com.shravan.spring_ai_chat.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shravan.spring_ai_chat.service.AiService;

@RestController
public class AiController {

	private final AiService aiService;
	
	public AiController(AiService aiService) {
		this.aiService=aiService;
	}
	
	@GetMapping("/ai/chat")
	public String chat(@RequestParam String message) {
		return aiService.askAi(message);
	}
}
