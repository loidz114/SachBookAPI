package org.example.sachbookapi.Service;

import org.example.sachbookapi.Entity.BookModel;
import org.example.sachbookapi.Entity.CategoryModel;
import org.example.sachbookapi.Repository.BookRepository;
import org.example.sachbookapi.Repository.CategoryRepository;
import org.example.sachbookapi.Repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class BookService {
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    public List<BookModel> getNewBooks() {
        return bookRepository.findTop10ByOrderByCreatedAtDesc();
    }

    public List<BookModel> getDiscountedBooks() {
        return bookRepository.findByDiscountIsNotNull();
    }

    public List<BookModel> searchBooks(String title, String author, Long categoryId) {
        if (title != null && !title.isEmpty()) {
            return bookRepository.findByTitleContainingIgnoreCase(title);
        } else if (author != null && !author.isEmpty()) {
            return bookRepository.findByAuthorContainingIgnoreCase(author);
        } else if (categoryId != null) {
            return bookRepository.findByCategoryId(categoryId);
        }
        return bookRepository.findAll();
    }

    public Page<BookModel> getSortedBooks(String sortBy, String direction, int page, int size) {
        Sort sort = direction.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        return bookRepository.findAll(PageRequest.of(page, size, sort));
    }

    public BookModel getBookById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found"));
    }

    public BookModel createBook(BookModel book, Long categoryId) {
        CategoryModel category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        book.setCategory(category);
        return bookRepository.save(book);
    }

    public BookModel updateBook(Long id, BookModel updatedBook, Long categoryId) {
        BookModel book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found"));
        book.setTitle(updatedBook.getTitle());
        book.setAuthor(updatedBook.getAuthor());
        book.setDescription(updatedBook.getDescription());
        book.setPrice(updatedBook.getPrice());
        book.setQuantity(updatedBook.getQuantity());
        book.setImageUrl(updatedBook.getImageUrl());
        book.setIsAvailable(updatedBook.getIsAvailable());
        if (categoryId != null) {
            CategoryModel category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            book.setCategory(category);
        }
        return bookRepository.save(book);
    }

    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }
}