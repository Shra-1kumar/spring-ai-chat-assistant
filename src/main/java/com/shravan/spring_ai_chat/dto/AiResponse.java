package com.shravan.spring_ai_chat.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AiResponse {

	private String question;
	private String answer;
	private String model;
	private LocalDateTime timestamp;
}
