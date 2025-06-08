package com.ssafy.pjt.model.service;

import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MoodKeywordService {

    private final Map<String, List<String>> moodKeywordMap;

    public MoodKeywordService() {
        moodKeywordMap = new HashMap<>();

        // 🎉 즐거움 - 신나고 강도 높은 운동
        moodKeywordMap.put("즐거움", List.of(
            "줌바 댄스 챌린지",
            "EDM 피트니스",
            "신나는 홈트",
            "타바타 댄스 루틴",
            "리듬 타며 운동하기",
            "에어로빅 운동 브이로그",
            "친구랑 운동하는 브이로그"
        ));

        // 🌧 슬픔 - 감정 정화, 위로, 동물 콘텐츠
        moodKeywordMap.put("슬픔", List.of(
            "위로가 되는 요가",
            "따뜻한 햇살 브이로그",
            "힐링 스트레칭",
            "눈물 날 때 듣는 음악",
            "강아지 힐링 영상",
            "혼자 감성 산책",
            "고양이 자는 영상",
            "조용한 일상 감성 브이로그"
        ));

        // 🔥 화남 - 스트레스 해소 + 익스트림
        moodKeywordMap.put("화남", List.of(
            "복싱으로 스트레스 풀기",
            "격한 타바타",
            "펀치백 운동 루틴",
            "익스트림 스포츠 하이라이트",
            "스카이다이빙 POV",
            "파쿠르 영상",
            "고음 지르는 메탈 음악",
            "샌드백 치는 영상",
            "시원한 폭포 영상"
        ));

        // 😐 아무렇지 않음 - 가볍고 무난한 루틴
        moodKeywordMap.put("아무렇지 않음", List.of(
            "기초 스트레칭 루틴",
            "10분 가벼운 요가",
            "출근 전 가벼운 홈트",
            "느릿한 루틴 따라하기",
            "산책 ASMR",
            "무념무상 브이로그",
            "창밖 보기 일상",
            "심심할 때 틀어놓는 영상"
        ));
    }

    public List<String> getYoutubeSearchKeywordsByMood(String mood) {
        List<String> keywords = new ArrayList<>(moodKeywordMap.getOrDefault(mood, List.of("홈트레이닝", "힐링 영상")));
        Collections.shuffle(keywords);
        return keywords.subList(0, Math.min(3, keywords.size()));
    }
}
