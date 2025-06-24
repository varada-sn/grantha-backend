package com.rvce.Grantha.podcast_service.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rvce.Grantha.podcast_service.model.PodcastLink;
import com.rvce.Grantha.podcast_service.repository.PodcastLinkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class PodcastService {

    @Autowired
    private PodcastLinkRepository podcastLinkRepository;

    public List<PodcastLink> searchPodcastLinks(String bookname, String author, String genre) {
        // If all parameters are null, return an empty list
        if ((bookname == null || bookname.isBlank()) &&
                (author == null || author.isBlank()) &&
                (genre == null || genre.isBlank())) {
            return new ArrayList<>();
        }

        List<PodcastLink> results = podcastLinkRepository.findByBookNameContainingIgnoreCaseOrAuthorContainingIgnoreCaseOrGenreContainingIgnoreCase(
                bookname != null ? bookname : "",
                author != null ? author : "",
                genre != null ? genre : ""
        );

        // If no results found, fetch from external sources
        if (results.isEmpty()) {
            if (bookname != null && !bookname.isBlank()) {
                results.addAll(fetchFromSpotify(bookname));
            }
            podcastLinkRepository.saveAll(results);
        }

        return results;
    }

    /*METHOD 2 : fetchFromSpotify*/

    //Get Spotify Access Token
    private String getSpotifyAccessToken() {
        String clientId = "ad53fcde7a354559afb716d87da64606";
        String clientSecret = "69d519e85a9a47ccb545d09c51d732f7";
        String tokenUrl = "https://accounts.spotify.com/api/token";

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBasicAuth(clientId, clientSecret);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "client_credentials");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity(tokenUrl, request, Map.class);

        return response.getBody().get("access_token").toString();
    }

    //Search Podcasts on Spotify
    public List<PodcastLink> fetchFromSpotify(String bookname) {
        String SPOTIFY_API = "https://api.spotify.com/v1/search?q=";
        String url = SPOTIFY_API + bookname + "&type=show";

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + getSpotifyAccessToken());

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        return parseSpotifyResponse(response.getBody());
    }

    //Parse response
    private List<PodcastLink> parseSpotifyResponse(String jsonResponse) {
        List<PodcastLink> podcastLinks = new ArrayList<>();

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(jsonResponse);
            JsonNode items = root.path("shows").path("items"); // Correct path in Spotify API response

            for (JsonNode item : items) {
                String title = item.path("name").asText();
                String publisher = item.path("publisher").asText();
                String podcastUrl = item.path("external_urls").path("spotify").asText();
                String imageUrl = item.path("images").get(0).path("url").asText(); // Fetch first image URL

                if (!podcastUrl.isEmpty()) {
                    if (!podcastLinkRepository.existsByPodcastUrl(podcastUrl)) {
                        // Save only if podcast URL is unique
                        podcastLinks.add(podcastLinkRepository.save(
                                new PodcastLink(null, title, publisher, "", podcastUrl)
                        ));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return podcastLinks;
    }
}
