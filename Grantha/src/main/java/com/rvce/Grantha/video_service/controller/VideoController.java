package com.rvce.Grantha.video_service.controller;

import com.rvce.Grantha.video_service.model.VideoLink;
import com.rvce.Grantha.video_service.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

        import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/youtube")  // Ensure correct endpoint mapping
public class VideoController {

    @Autowired
    private VideoService videoService;

    @GetMapping
    public ResponseEntity<List<VideoLink>> searchYouTubeLinks(
            @RequestParam(required = false) String bookname,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String genre
    ) {
        List<VideoLink> results = videoService.searchVideoLinks(bookname, author, genre);

        return results.isEmpty() ?
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList()) :
                ResponseEntity.ok(results);
    }
}