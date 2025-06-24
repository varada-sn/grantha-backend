/*Handles DB queries using Spring Data JPA*/
package com.rvce.Grantha.book_rental_service.repository;
import com.rvce.Grantha.book_rental_service.model.Book; //Book table
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book , Long>{
    //List<Book>findByTitleContainingIgnoreCase(String title);
    //List<Book>findByAuthorContainingIgnoreCase(String author);
    //List<Book>findByGenreContainingIgnoreCase(String genre);

    //Customer: Repository searching book in the database by title, author, genre
    List<Book> findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCaseOrGenreContainingIgnoreCase(String title, String author, String genre);

    //Supplier: Fetch the books by ID for the supplier to manage
    List<Book> findBySupplier_Sid(Long sid);
}