package org.example.sachbookapi.Controller;

import jakarta.validation.constraints.Min;
import org.example.sachbookapi.Entity.BookModel;
import org.example.sachbookapi.Service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/books")
@CrossOrigin(origins = "*")
public class BookController {
    private static final Logger logger = Logger.getLogger(BookController.class.getName());

    @Autowired
    private BookService bookService;

    @Value("${server.address:192.168.1.46}")
    private String serverAddress;

    @Value("${server.port:8080}")
    private String serverPort;

    private String getBaseUrl() {
        String baseUrl = "http://" + serverAddress + ":" + serverPort + "/";
        logger.info("Generated base URL: " + baseUrl);
        return baseUrl;
    }

    @GetMapping("/new")
    public ResponseEntity<List<BookModel>> getNewBooks() {
        List<BookModel> books = bookService.getNewBooks();
        String baseUrl = getBaseUrl();
        books.forEach(book -> {
            if (book.getImageUrl() != null && !book.getImageUrl().isEmpty()) {
                if (!book.getImageUrl().startsWith("http://") && !book.getImageUrl().startsWith("https://")) {
                    String newImageUrl = baseUrl + book.getImageUrl();
                    logger.info("Transformed image URL for book " + book.getTitle() + ": " + newImageUrl);
                    book.setImageUrl(newImageUrl);
                } else {
                    logger.info("Image URL already absolute for book " + book.getTitle() + ": " + book.getImageUrl());
                }
            } else {
                logger.warning("Image URL is null or empty for book: " + book.getTitle());
            }
        });
        return ResponseEntity.ok(books);
    }

    @GetMapping("/discounted")
    public ResponseEntity<List<BookModel>> getDiscountedBooks() {
        List<BookModel> books = bookService.getDiscountedBooks();
        String baseUrl = getBaseUrl();
        books.forEach(book -> {
            if (book.getImageUrl() != null && !book.getImageUrl().isEmpty()) {
                if (!book.getImageUrl().startsWith("http://") && !book.getImageUrl().startsWith("https://")) {
                    String newImageUrl = baseUrl + book.getImageUrl();
                    logger.info("Transformed image URL for book " + book.getTitle() + ": " + newImageUrl);
                    book.setImageUrl(newImageUrl);
                } else {
                    logger.info("Image URL already absolute for book " + book.getTitle() + ": " + book.getImageUrl());
                }
            } else {
                logger.warning("Image URL is null or empty for book: " + book.getTitle());
            }
        });
        return ResponseEntity.ok(books);
    }

    @GetMapping("/search")
    public ResponseEntity<List<BookModel>> searchBooks(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) Long categoryId) {
        List<BookModel> books = bookService.searchBooks(title, author, categoryId);
        String baseUrl = getBaseUrl();
        books.forEach(book -> {
            if (book.getImageUrl() != null && !book.getImageUrl().isEmpty()) {
                if (!book.getImageUrl().startsWith("http://") && !book.getImageUrl().startsWith("https://")) {
                    String newImageUrl = baseUrl + book.getImageUrl();
                    logger.info("Transformed image URL for book " + book.getTitle() + ": " + newImageUrl);
                    book.setImageUrl(newImageUrl);
                } else {
                    logger.info("Image URL already absolute for book " + book.getTitle() + ": " + book.getImageUrl());
                }
            } else {
                logger.warning("Image URL is null or empty for book: " + book.getTitle());
            }
        });
        return ResponseEntity.ok(books);
    }

    @GetMapping("/sorted")
    public ResponseEntity<Page<BookModel>> getSortedBooks(
            @RequestParam(defaultValue = "title") String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) int size) {
        Page<BookModel> booksPage = bookService.getSortedBooks(sortBy, direction, page, size);
        String baseUrl = getBaseUrl();
        booksPage.forEach(book -> {
            if (book.getImageUrl() != null && !book.getImageUrl().isEmpty()) {
                if (!book.getImageUrl().startsWith("http://") && !book.getImageUrl().startsWith("https://")) {
                    String newImageUrl = baseUrl + book.getImageUrl();
                    logger.info("Transformed image URL for book " + book.getTitle() + ": " + newImageUrl);
                    book.setImageUrl(newImageUrl);
                } else {
                    logger.info("Image URL already absolute for book " + book.getTitle() + ": " + book.getImageUrl());
                }
            } else {
                logger.warning("Image URL is null or empty for book: " + book.getTitle());
            }
        });
        return ResponseEntity.ok(booksPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookModel> getBookById(@PathVariable Long id) {
        BookModel book = bookService.getBookById(id);
        if (book != null) {
            String baseUrl = getBaseUrl();
            if (book.getImageUrl() != null && !book.getImageUrl().isEmpty()) {
                if (!book.getImageUrl().startsWith("http://") && !book.getImageUrl().startsWith("https://")) {
                    String newImageUrl = baseUrl + book.getImageUrl();
                    logger.info("Transformed image URL for book " + book.getTitle() + ": " + newImageUrl);
                    book.setImageUrl(newImageUrl);
                } else {
                    logger.info("Image URL already absolute for book " + book.getTitle() + ": " + book.getImageUrl());
                }
            } else {
                logger.warning("Image URL is null or empty for book: " + book.getTitle());
            }
            return ResponseEntity.ok(book);
        }
        return ResponseEntity.notFound().build();
    }
}