package com.rvce.Grantha.book_rental_service.controller;
import com.rvce.Grantha.book_rental_service.model.Book;
import com.rvce.Grantha.book_rental_service.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

//Exposes REST API's for searching books by title,author or genre
//CUSTOMER CONTROLLER (FOR SEARCHING BOOKS)

@RestController
@RequestMapping("/books")
public class CustomerController {
    @Autowired
    private BookService bookService;

    //Opn 1: Search Books by title or author or genre
    @GetMapping("/search")
    public List<Book> getBooksByTitleOrAuthorOrGenre(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String genre) {
        return bookService.getBooksByTitleOrAuthorOrGenre(title, author, genre);
    }
}
