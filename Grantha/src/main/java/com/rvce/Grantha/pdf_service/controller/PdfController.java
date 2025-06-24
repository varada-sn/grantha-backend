/*Handles HTTP Requests*/

package com.rvce.Grantha.pdf_service.controller;
import com.rvce.Grantha.pdf_service.model.PdfLink;
import com.rvce.Grantha.pdf_service.service.PdfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

/*
@RestController
@RequestMapping("/pdfs")
public class PdfController{
    @Autowired
    private PdfService pdfService;

    @GetMapping("/search")
    public ResponseEntity<List<PdfLink>> searchPdfs(
            @RequestParam String bookname,
            @RequestParam String author,
            @RequestParam String genre){
        List<PdfLink> results=pdfService.searchPdfLinks(bookname,author,genre);
        if(results.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
        }
        return ResponseEntity.ok(results);
    }
}*/

@RestController
@RequestMapping("/pdfs")
public class PdfController {
    @Autowired
    private PdfService pdfService;

    @GetMapping("/search")
    public ResponseEntity<List<PdfLink>> searchPdfs(
            @RequestParam(required = false) String bookname,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String genre
    ) {
        List<PdfLink> results = pdfService.searchPdfLinks(bookname, author, genre);
        return results.isEmpty() ? ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList()) : ResponseEntity.ok(results);
    }
}
