package com.rvce.Grantha.podcast_service.model;

import jakarta.persistence.*;

@Entity
@Table(name = "podcast_links")
public class PodcastLink {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String bookName;
    private String author;
    private String genre;
    private String podcastUrl;

    public PodcastLink() {
    }

    public PodcastLink(Long id, String bookName, String author, String genre, String podcastUrl) {
        this.id = id;
        this.bookName = bookName;
        this.author = author;
        this.genre = genre;
        this.podcastUrl = podcastUrl;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getPodcastUrl() {
        return podcastUrl;
    }

    public void setPodcastUrl(String podcastUrl) {
        this.podcastUrl = podcastUrl;
    }

}
