/*Defines database entity - table structure in the database*/
package com.rvce.Grantha.pdf_service.model;
import jakarta.persistence.*;

@Entity
@Table(name = "pdf_links")
public class PdfLink {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String bookName;
    private String author;
    private String genre;
    private String pdfUrl;

    public PdfLink() {
    }

    public PdfLink(Long id, String bookName, String author, String genre, String pdfUrl) {
        this.id = id;
        this.bookName = bookName;
        this.author = author;
        this.genre = genre;
        this.pdfUrl = pdfUrl;
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

    public String getPdfUrl() {
        return pdfUrl;
    }

    public void setPdfUrl(String pdfUrl) {
        this.pdfUrl = pdfUrl;
    }
}