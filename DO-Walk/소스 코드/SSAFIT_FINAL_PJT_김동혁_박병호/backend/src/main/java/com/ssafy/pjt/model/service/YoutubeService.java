package com.ssafy.pjt.model.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.pjt.model.dto.Video;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class YoutubeService {

    @Value("${youtube.api.key}")
    private String apiKey;

    public List<Video> fetchVideosByKeyword(String keyword) {
        List<Video> videos = new ArrayList<>();

        try {
            String encodedKeyword = URLEncoder.encode(keyword, StandardCharsets.UTF_8);
            String urlStr = "https://www.googleapis.com/youtube/v3/search?part=snippet"
                    + "&q=" + encodedKeyword
                    + "&maxResults=10"
                    + "&type=video"
                    + "&key=" + apiKey;

            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            InputStream is = conn.getInputStream();
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(is);

            for (JsonNode item : root.get("items")) {
                Video video = new Video();
                video.setVideoId(item.get("id").get("videoId").asText());
                video.setVideoTitle(item.get("snippet").get("title").asText());
                video.setChannelName(item.get("snippet").get("channelTitle").asText());
                video.setThumbnailUrl(item.get("snippet").get("thumbnails").get("default").get("url").asText());
                video.setTag("기분추천");  // 추천 영상 표시용
                videos.add(video);
            }

        } catch (Exception e) {
            e.printStackTrace(); // or log
        }

        return videos;
    }
}
