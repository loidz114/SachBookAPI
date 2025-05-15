package org.example.sachbookapi.Repository;

import org.example.sachbookapi.Entity.BookModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<BookModel, Long> {
    List<BookModel> findByTitleContainingIgnoreCase(String title);
    List<BookModel> findByAuthorContainingIgnoreCase(String author);
    List<BookModel> findByCategoryId(Long categoryId);
    List<BookModel> findByDiscountIsNotNull();
    List<BookModel> findTop10ByOrderByCreatedAtDesc();
}