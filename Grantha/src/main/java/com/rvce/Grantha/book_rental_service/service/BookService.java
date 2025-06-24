package com.rvce.Grantha.book_rental_service.service;
import com.rvce.Grantha.book_rental_service.model.Book;
import com.rvce.Grantha.book_rental_service.repository.BookRepository;
import com.rvce.Grantha.book_rental_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.rvce.Grantha.book_rental_service.model.Supplier;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

//Contains both Customer and Supplier logic -exposed via separate controllers

@Service
public class BookService {
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    //Get Supplier ID from logged-in user
    public Supplier getSupplierByUsername(String email) {
        return userRepository.findSupplierByEmail(email)
                .orElseThrow(() -> new RuntimeException("Supplier not found"));
    }

    //Get books by Supplier ID
    public List<Book> getBooksBySupplierId(Long supplierId) {
        return bookRepository.findBySupplier_Sid(supplierId);
    }

    //Fetches book by title , author or genre (Customer's action)
    public List<Book> getBooksByTitleOrAuthorOrGenre(String title, String author , String genre){
        return bookRepository.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCaseOrGenreContainingIgnoreCase(title,author,genre);
    }

    //Add a new Book (Supplier's action)
    public Book addBook(Book book){
        return bookRepository.save(book);
    }

    //Delete a book (Supplier's action)
    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }
}

