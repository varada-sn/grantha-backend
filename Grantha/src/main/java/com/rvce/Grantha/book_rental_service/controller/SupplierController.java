package com.rvce.Grantha.book_rental_service.controller;

//SUPPLIER CONTROLLER (FOR ADDING AND DELETING(MANAGING) BOOKS)

import com.rvce.Grantha.book_rental_service.model.Book;
import com.rvce.Grantha.book_rental_service.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/supplier/books")
public class SupplierController {
    @Autowired
    private BookService bookService;

    // Add a book
    @PostMapping
    public Book addBook(@RequestBody Book book) {
        return bookService.addBook(book);
    }

    // Delete a book
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }
}

