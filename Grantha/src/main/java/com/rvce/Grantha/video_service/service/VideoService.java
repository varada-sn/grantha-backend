package com.rvce.Grantha.video_service.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rvce.Grantha.video_service.model.VideoLink;
import com.rvce.Grantha.video_service.repository.VideoLinkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class VideoService {

    @Autowired
    private VideoLinkRepository videoLinkRepository;

    private static final String YOUTUBE_API_KEY = "AIzaSyAI7Td-kep6xk4OmXhI2H1xSVo0k5fbuSA";  // Replace with actual key
    private static final String YOUTUBE_SEARCH_URL = "https://www.googleapis.com/youtube/v3/search";

    public List<VideoLink> searchVideoLinks(String bookname, String author, String genre) {
        if ((bookname == null || bookname.isBlank()) &&
                (author == null || author.isBlank()) &&
                (genre == null || genre.isBlank())) {
            return new ArrayList<>();
        }

        List<VideoLink> results = videoLinkRepository.findByBookNameContainingIgnoreCaseOrAuthorContainingIgnoreCaseOrGenreContainingIgnoreCase(
                bookname != null ? bookname : "",
                author != null ? author : "",
                genre != null ? genre : ""
        );

        if (results.isEmpty()) {
            if (bookname != null && !bookname.isBlank()) {
                results.addAll(fetchFromYouTube(bookname));
            }
            videoLinkRepository.saveAll(results);
        }

        return results;
    }

    // Fetch movie-related videos from YouTube
    private List<VideoLink> fetchFromYouTube(String bookname) {
        String url = YOUTUBE_SEARCH_URL + "?part=snippet&q=" + bookname + "+movie&type=video&key=" + YOUTUBE_API_KEY;

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        return parseYouTubeResponse(response.getBody());
    }

    // Parse YouTube API Response
    private List<VideoLink> parseYouTubeResponse(String jsonResponse) {
        List<VideoLink> movieLinks = new ArrayList<>();

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(jsonResponse);
            JsonNode items = root.path("items");

            for (JsonNode item : items) {
                String videoId = item.path("id").path("videoId").asText();
                String title = item.path("snippet").path("title").asText();
                String channel = item.path("snippet").path("channelTitle").asText();
                String thumbnailUrl = item.path("snippet").path("thumbnails").path("high").path("url").asText();
                String videoUrl = "https://www.youtube.com/watch?v=" + videoId;

                if (!videoUrl.isEmpty() && !videoLinkRepository.existsByVideoUrl(videoUrl)) {
                    movieLinks.add(videoLinkRepository.save(
                            new VideoLink(null, title, channel,  "", videoUrl)
                    ));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return movieLinks;
    }
}