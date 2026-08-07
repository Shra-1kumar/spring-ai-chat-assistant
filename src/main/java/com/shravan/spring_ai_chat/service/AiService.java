package com.shravan.spring_ai_chat.service;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.time.LocalDateTime;
import java.io.IOException;
import reactor.core.publisher.Flux;
import java.nio.charset.StandardCharsets;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StreamUtils;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.stereotype.Service;

import com.shravan.spring_ai_chat.dto.AiResponse;

@Service
public class AiService {

	private static final Logger logger=
			LoggerFactory.getLogger(AiService.class);
	
	private final ChatClient chatClient;
	
	public AiService(ChatClient chatClient) {
		this.chatClient=chatClient;
	}
	
	private String loadPromptTemplate() throws IOException{
		ClassPathResource resource = 
				new ClassPathResource("prompts/java-trainer.st");
		
		return StreamUtils.copyToString(
				resource.getInputStream(),
				StandardCharsets.UTF_8);
	}
	
	public AiResponse askAi(String message) throws IOException {
		long startTime = System.currentTimeMillis();
		logger.info("User Question : {}",message);
		String template = loadPromptTemplate();
		String systemPrompt = template.replace("{question}", message);
//		String answer = chatClient
//				.prompt(message)
//				.call()
//				.content();
		
		String answer=chatClient
				.prompt()
//				.system("""
//						You are an experinced Java Full Stack Trainer.
//						
//						Rules:
//						
//						1. Explain in Simple English.
//						2. Give one real-world example.
//						3. Keep answer under 200 words.
//						4. Use bullet points whenever possible.
//						5. If thr question is about Java, provide a small code example.
//						""")
				.system(systemPrompt)
				.user(message)
				.advisors(advisor -> advisor.param(
						ChatMemory.CONVERSATION_ID,"user-1"))
				.call()
				.content();
		
		long endTime = System.currentTimeMillis();
		
		logger.info("Gemini Response Generated Successfully");
		logger.info("Response Time : {} ms",(endTime-startTime));
		
		return new AiResponse(
				message,
				answer,
				"Gemini",
				LocalDateTime.now()
				);
//		throw new RuntimeException("ai service is temporarily unavailable");
		
		
	}
	public Flux<String> streamAiResponse(String message) throws IOException{
		logger.info("Streaming Request : {}", message);
		
		String systemPrompt = loadPromptTemplate();
		
		return chatClient
				.prompt()
				.system(systemPrompt)
				.user(message)
				.advisors(advisor -> advisor.param(
						org.springframework.ai.chat.memory.ChatMemory.CONVERSATION_ID,
						"user-1"))
				.stream()
				.content();
				}
}
