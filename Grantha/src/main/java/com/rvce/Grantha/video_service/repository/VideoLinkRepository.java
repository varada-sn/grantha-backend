package com.rvce.Grantha.video_service.repository;

import com.rvce.Grantha.video_service.model.VideoLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VideoLinkRepository extends JpaRepository<VideoLink, Long> {
    List<VideoLink> findByBookNameContainingIgnoreCaseOrAuthorContainingIgnoreCaseOrGenreContainingIgnoreCase(
            String bookName, String author, String genre
    );

    boolean existsByVideoUrl(String videoUrl);
}