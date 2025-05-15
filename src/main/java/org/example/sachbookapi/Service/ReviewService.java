package org.example.sachbookapi.Service;

import org.example.sachbookapi.Dto.ReviewRequest;
import org.example.sachbookapi.Entity.BookModel;
import org.example.sachbookapi.Entity.ReviewModel;
import org.example.sachbookapi.Entity.UserModel;
import org.example.sachbookapi.Repository.BookRepository;
import org.example.sachbookapi.Repository.ReviewRepository;
import org.example.sachbookapi.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

    // Tạo review mới
    @Transactional
    public ResponseEntity<ReviewModel> createReview(Long userId, Long bookId, ReviewRequest request) {
        // Kiểm tra rating hợp lệ
        if (request.getRating() < 1 || request.getRating() > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5.");
        }

        // Tìm user và book
        UserModel user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found"));
        BookModel book = bookRepository.findById(bookId)
                .orElseThrow(() -> new NoSuchElementException("Book not found"));

        // Tạo review
        ReviewModel review = new ReviewModel();
        review.setUser(user);
        review.setBook(book);
        review.setRating(request.getRating());
        review.setComment(request.getComment());

        // Lưu review
        ReviewModel savedReview = reviewRepository.save(review);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedReview);
    }

    // Lấy tất cả review của sách theo bookId
    public List<ReviewModel> getReviewsByBookId(Long bookId) {
        return reviewRepository.findByBookId(bookId);
    }

    // Sửa review
    @Transactional
    public ResponseEntity<ReviewModel> updateReview(Long reviewId, ReviewRequest request) {
        // Tìm review theo reviewId
        ReviewModel review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new NoSuchElementException("Review not found"));

        // Kiểm tra rating hợp lệ
        if (request.getRating() < 1 || request.getRating() > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5.");
        }

        // Cập nhật thông tin review
        review.setRating(request.getRating());
        review.setComment(request.getComment());

        // Lưu lại review đã cập nhật
        ReviewModel updatedReview = reviewRepository.save(review);
        return ResponseEntity.ok(updatedReview);
    }

    // Xóa review
    @Transactional
    public ResponseEntity<String> deleteReview(Long reviewId) {
        // Tìm review theo reviewId
        ReviewModel review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new NoSuchElementException("Review not found"));

        // Xóa review
        reviewRepository.delete(review);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Review deleted successfully");
    }
}