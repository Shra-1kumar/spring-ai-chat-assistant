package com.shravan.spring_ai_chat.controller;

import java.io.IOException;

import reactor.core.publisher.Flux;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shravan.spring_ai_chat.dto.AiResponse;
import com.shravan.spring_ai_chat.service.AiService;

@RestController
public class AiController {

	private final AiService aiService;
	public AiController(AiService aiService) {
		this.aiService=aiService;
	}
	
	@GetMapping("/ai/chat")
	public ResponseEntity<AiResponse> chat(@RequestParam String message) throws IOException {
		AiResponse response = aiService.askAi(message);
		return ResponseEntity.ok(response);
	}
	
	@GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<String> stream(@RequestParam String message) throws IOException{
		return aiService.streamAiResponse(message);
	}
}
