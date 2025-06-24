package com.rvce.Grantha.podcast_service.controller;

import com.rvce.Grantha.podcast_service.model.PodcastLink;
import com.rvce.Grantha.podcast_service.service.PodcastService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/podcasts")  // Ensure correct endpoint mapping
public class PodcastController {

    @Autowired
    private PodcastService podcastService;

    @GetMapping
    public ResponseEntity<List<PodcastLink>> searchPodcasts(
            @RequestParam(required = false) String bookname,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String genre
    ) {
        List<PodcastLink> results = podcastService.searchPodcastLinks(bookname, author, genre);

        return results.isEmpty() ?
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList()) :
                ResponseEntity.ok(results);
    }
}
