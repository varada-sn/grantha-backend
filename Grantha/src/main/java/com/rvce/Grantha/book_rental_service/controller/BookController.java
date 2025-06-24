package com.rvce.Grantha.book_rental_service.controller;

import com.rvce.Grantha.book_rental_service.model.Book;
import com.rvce.Grantha.book_rental_service.model.Supplier;
import com.rvce.Grantha.book_rental_service.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@CrossOrigin("*")
public class BookController {

    @Autowired
    private BookService bookService;

    @GetMapping("/supplier")
    public ResponseEntity<List<Book>> getBooksForSupplier(@AuthenticationPrincipal UserDetails userDetails) {
        // Get supplier ID from logged-in user (Username is Email)
        Supplier supplier = bookService.getSupplierByUsername(userDetails.getUsername());

        // Fetch books only for this supplier
        List<Book> books = bookService.getBooksBySupplierId(supplier.getSid());

        return ResponseEntity.ok(books);
    }
}
