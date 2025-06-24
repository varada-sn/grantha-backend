/*Handles DB queries*/
package com.rvce.Grantha.pdf_service.repository;
import com.rvce.Grantha.pdf_service.model.PdfLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/*
@Repository
public interface PdfLinkRepository extends JpaRepository<PdfLink,Long>{
    List<PdfLink> findByBookNameOrAuthorOrGenre(String bookName,String author,String genre);

    boolean existsByPdfUrl(String pdfUrl);
}
*/

@Repository
public interface PdfLinkRepository extends JpaRepository<PdfLink, Long> {
    List<PdfLink> findByBookNameContainingIgnoreCaseOrAuthorContainingIgnoreCaseOrGenreContainingIgnoreCase(
            String bookName, String author, String genre
    );
    boolean existsByPdfUrl(String pdfUrl);
}


