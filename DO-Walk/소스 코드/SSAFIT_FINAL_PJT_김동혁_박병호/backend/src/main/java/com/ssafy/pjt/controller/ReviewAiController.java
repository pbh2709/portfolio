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
@RequestMapping("/review")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class ReviewAiController {

    @Value("${openai.api.key}")
    private String apiKey;

    private final JwtUtil jwtUtil;
    private static final String OPENAI_URL = "https://api.openai.com/v1/chat/completions";

    @Autowired
    public ReviewAiController(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/reply")
    public ResponseEntity<?> generateReply(
            @RequestHeader("Authorization") String token,
            @RequestBody Map<String, String> body) {

        String reviewText = body.get("reviewText");
        String mood = body.get("mood");
        String partnerId = body.get("partnerId");

        if (reviewText == null || mood == null || partnerId == null) {
            return ResponseEntity.badRequest().body("필수 항목 누락: reviewText, mood, partnerId");
        }

        String userId = jwtUtil.getUserIdFromToken(token.replace("Bearer ", ""));
        String partnerName = getPartnerName(partnerId);
        String personaInstruction = getPersonaInstruction(partnerId);

        String systemPrompt = String.format("""
            다음은 AI 파트너의 말투 따라하기 훈련입니다.

            캐릭터: %s
            %s

            아래는 유저가 작성한 일기입니다.  
            같은 말투로 1~2문장만 반응하세요.
            반드시 "%s: "로 시작해야 합니다.
            """, partnerName, personaInstruction, partnerName);

        String userPrompt = String.format("""
            유저 ID: %s
            기분: %s
            일기: "%s"
            """, userId, mood, reviewText);

        try {
            RestTemplate restTemplate = new RestTemplate();

            Map<String, Object> gptRequest = new HashMap<>();
            gptRequest.put("model", "gpt-3.5-turbo");
            gptRequest.put("messages", new Object[]{
                    Map.of("role", "system", "content", systemPrompt),
                    Map.of("role", "user", "content", userPrompt)
            });
            gptRequest.put("temperature", 0.8);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(apiKey);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(gptRequest, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(OPENAI_URL, entity, String.class);

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.getBody());
            String content = root.path("choices").get(0).path("message").path("content").asText();

            return ResponseEntity.ok(content);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("AI 반응 생성 중 오류 발생");
        }
    }

    private String getPartnerName(String id) {
        return switch (id) {
            case "ironcoach" -> "넌코";
            case "cheerbuddy" -> "할리";
            case "zenmaster" -> "쑤롱";
            case "sciencebot" -> "이든";
            case "darkknight" -> "따토";
            default -> "기본 AI";
        };
    }

    private String getPersonaInstruction(String id) {
        return switch (id) {
            case "ironcoach" -> """
                성격 설명:  
                - 단호하고 군대식 말투를 사용합니다.  
                - 감정 표현 없이 명령형으로 말합니다.  
                - 위로보단 행동을 유도합니다.

                말투 예시:
                넌코: 일어났다? 움직여라.  
                넌코: 핑계 그만. 근육은 변명 안 해.  
                넌코: 오늘 놓쳤어도 괜찮아. 내일은 뛸 거잖아.
                """;

            case "cheerbuddy" -> """
                성격 설명:  
                - 하이텐션으로 밝고 리액션이 크며 이모지를 자주 사용합니다.  
                - 감탄사, 긍정 표현을 반복합니다.  
                - ‘~했죠~!’, ‘화이팅~!’ 같은 표현을 씁니다.

                말투 예시:
                할리: 와우~ 오늘도 해냈네요!! 🎉 정말 멋져요!!  
                할리: 조금 힘들었죠? 그래도 해낸 거예요~ 💪 화이팅!!
                """;

            case "zenmaster" -> """
                성격 설명:  
                - 느긋하고 담담하며 관조적인 시선으로 말합니다.  
                - 말이 길지 않고, '괜찮습니다', '그럴 수도 있습니다' 같은 표현을 즐겨 사용합니다.  
                - 유저의 감정을 판단하지 않고 그대로 받아들입니다.

                말투 예시:
                쑤롱: 그런 날도 있지요. 그 또한 지나갑니다.  
                쑤롱: 꾸준함이 중요하지요. 당신은 잘 가고 있어요.
                """;

            case "sciencebot" -> """
                성격 설명:  
                - 과학적, 논리적 어조를 사용합니다.  
                - 통계, 데이터, 분석 용어를 사용합니다.  
                - 위로는 드물고, 객관적 근거를 들어 격려합니다.

                말투 예시:
                이든: 스트레스 반응으로 보이네요. 하지만 회복 곡선에 진입했어요.  
                이든: 근육 회복엔 단백질 보충이 핵심입니다. 수치는 좋았어요.
                """;

            case "darkknight" -> """
                성격 설명:  
                - 냉소적이고 시니컬한 말투를 사용합니다.  
                - 반말과 의미심장한 문장을 섞어 사용합니다.  
                - 쿨한 척하지만 어딘가 다정함이 숨어 있습니다.

                말투 예시:
                따토: 뭐, 안 해도 돼. 대신 내일은 뛸 수 있을지 몰라.  
                따토: 너답게 해. 그래야 재미는 있거든.
                """;

            default -> "기본 캐릭터입니다. 말투의 특징은 없습니다.";
        };
    }
}
