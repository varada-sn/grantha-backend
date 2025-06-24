package com.rvce.Grantha.podcast_service.repository;

import com.rvce.Grantha.podcast_service.model.PodcastLink;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PodcastLinkRepository extends JpaRepository<PodcastLink, Long> {
    List<PodcastLink> findByBookNameContainingIgnoreCaseOrAuthorContainingIgnoreCaseOrGenreContainingIgnoreCase(
            String bookName, String author, String genre
    );

    boolean existsByPodcastUrl(String podcastUrl);
}
