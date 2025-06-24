package com.rvce.Grantha.pdf_service.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rvce.Grantha.pdf_service.model.PdfLink;
import com.rvce.Grantha.pdf_service.repository.PdfLinkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

/*
@Service
public class PdfService {
    @Autowired
    /*Retrieve PDFs from database-Hardcoded
    private PdfLinkRepository pdfLinkRepository;

    /*METHOD 1 : searchPdfLinks
    public List<PdfLink> searchPdfLinks(String bookname, String author, String genre) {
        List<PdfLink> results = pdfLinkRepository.findByBookNameOrAuthorOrGenre(bookname, author, genre);

        /*If not found in DB,fetches from API
        if (results.isEmpty()) {
            results.addAll(fetchFromGoogleBooks(bookname));  //Calling METHOD 2
            results.addAll(fetchFromOpenLibrary(bookname));  //Calling METHOD 3
            pdfLinkRepository.saveAll(results); // Save new results into the database to avoid repeated API calls.
        }

        return results;
    }
    */

@Service
public class PdfService {
    @Autowired
    private PdfLinkRepository pdfLinkRepository;

    public List<PdfLink> searchPdfLinks(String bookname, String author, String genre) {
        // If all parameters are null, return an empty list
        if ((bookname == null || bookname.isBlank()) &&
                (author == null || author.isBlank()) &&
                (genre == null || genre.isBlank())) {
            return new ArrayList<>();
        }

        List<PdfLink> results = pdfLinkRepository.findByBookNameContainingIgnoreCaseOrAuthorContainingIgnoreCaseOrGenreContainingIgnoreCase(
                bookname != null ? bookname : "",
                author != null ? author : "",
                genre != null ? genre : ""
        );

        // If no results found, fetch from external sources
        if (results.isEmpty()) {
            if (bookname != null && !bookname.isBlank()) {
                results.addAll(fetchFromGoogleBooks(bookname));
                results.addAll(fetchFromOpenLibrary(bookname));
            }
            pdfLinkRepository.saveAll(results);
        }

        return results;
    }

    /*METHOD 2 : fetchFromGoogleBooks*/
    private List<PdfLink> fetchFromGoogleBooks(String bookname) {
        RestTemplate restTemplate = new RestTemplate();
        /*Searches for PDFs through Google Books API and Open Library API(gives archive links),
    then stores unique results in the database.*/
        String GOOGLE_BOOKS_API = "https://www.googleapis.com/books/v1/volumes?q=";
        String url = GOOGLE_BOOKS_API + bookname;
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        return parseGoogleBooksResponse(response.getBody());
    }

    /*METHOD 3: fetchFromOpenLibrary*/
    private List<PdfLink> fetchFromOpenLibrary(String bookname) {
        RestTemplate restTemplate = new RestTemplate();
        String OPEN_LIBRARY_API = "https://openlibrary.org/search.json?q=";
        String url = OPEN_LIBRARY_API + bookname;
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        return parseOpenLibraryResponse(response.getBody());
    }

    /*METHOD 4: fetchFromArchiveOrg*/
    private List<PdfLink> fetchFromArchiveOrg(String bookname) {
        RestTemplate restTemplate = new RestTemplate();
        /*Searches for PDFs through the Archive.org API and stores unique results in the database.*/

        String ARCHIVE_ORG_API = "https://archive.org/advancedsearch.php?q=";
        String url = ARCHIVE_ORG_API + bookname + "&fl[]=identifier,title,creator,year&output=json";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        return parseArchiveOrgResponse(response.getBody());
    }




    private List<PdfLink> parseGoogleBooksResponse(String jsonResponse) {
        List<PdfLink> pdfLinks = new ArrayList<>();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(jsonResponse);
            for (JsonNode item : root.path("items")) {
                String title = item.path("volumeInfo").path("title").asText();
                String author = item.path("volumeInfo").path("authors").toString();
                //String genre = item.path("volumeInfo").path("categories").get(0).toString();
                JsonNode categoriesNode = item.path("volumeInfo").path("categories");
                String genre = (categoriesNode.isArray() && categoriesNode.size() > 0) ? categoriesNode.get(0).asText() : "Unknown";
                String pdfUrl = item.path("accessInfo").path("webReaderLink").asText();
                if (!pdfUrl.isEmpty()) {
                    if (!pdfLinkRepository.existsByPdfUrl(pdfUrl)) {
                        pdfLinks.add(pdfLinkRepository.save(new PdfLink(null, title, author, genre, pdfUrl))); // Store in DB if unique
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pdfLinks;
    }

    private List<PdfLink> parseOpenLibraryResponse(String jsonResponse) {
        List<PdfLink> pdfLinks = new ArrayList<>();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(jsonResponse);
            for (JsonNode doc : root.path("docs")) {
                String title = doc.path("title").asText();
                String author = doc.path("author_name").toString();
                if (doc.has("ia")) {
                    String pdfUrl = "https://archive.org/details/" + doc.path("ia").get(0).asText();
                    if (!pdfLinkRepository.existsByPdfUrl(pdfUrl)) {
                        pdfLinks.add(pdfLinkRepository.save(new PdfLink(null, title, author, "", pdfUrl))); // Store in DB if unique
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pdfLinks;
    }

    private List<PdfLink> parseArchiveOrgResponse(String jsonResponse) {
        List<PdfLink> pdfLinks = new ArrayList<>();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(jsonResponse);
            for (JsonNode doc : root.path("docs")) {
                String title = doc.path("title").asText();
                String author = doc.path("author_name").toString();
                if (doc.has("ia")) {
                    String pdfUrl = "https://archive.org/details/" + doc.path("ia").get(0).asText();
                    if (!pdfLinkRepository.existsByPdfUrl(pdfUrl)) {
                        pdfLinks.add(pdfLinkRepository.save(new PdfLink(null, title, author, "", pdfUrl))); // Store in DB if unique
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pdfLinks;
    }
}
