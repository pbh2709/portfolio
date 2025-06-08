package com.ssafy.pjt.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.pjt.auth.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/ai")
public class AiController {

    @Value("${openai.api.key}")
    private String apiKey;

    private final JwtUtil jwtUtil;

    private static final String OPENAI_URL = "https://api.openai.com/v1/chat/completions";

    @Autowired
    public AiController(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/recommend")
    public ResponseEntity<?> recommendRoutine(
            @RequestHeader("Authorization") String token,
            @RequestBody Map<String, String> requestBody) {

        String prompt = requestBody.get("prompt");

        if (prompt == null || prompt.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("입력된 내용이 없습니다.");
        }

        String userId = jwtUtil.getUserIdFromToken(token.replace("Bearer ", ""));

        try {
            RestTemplate restTemplate = new RestTemplate();

            Map<String, Object> request = new HashMap<>();
            request.put("model", "gpt-3.5-turbo");
            request.put("messages", new Object[] {
                    Map.of("role", "user", "content", "유저 ID: " + userId + "\n운동 루틴을 추천해줘: " + prompt)
            });
            request.put("temperature", 0.7);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(apiKey);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(OPENAI_URL, entity, String.class);

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.getBody());
            String content = root.path("choices").get(0).path("message").path("content").asText();

            return ResponseEntity.ok(content);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("AI 추천 처리 중 오류 발생");
        }
    }
}
