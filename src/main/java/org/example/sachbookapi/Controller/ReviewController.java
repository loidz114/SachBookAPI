package org.example.sachbookapi.Controller;

import jakarta.validation.Valid;
import org.example.sachbookapi.Dto.ReviewRequest;
import org.example.sachbookapi.Entity.ReviewModel;
import org.example.sachbookapi.Service.ReviewService;
import org.example.sachbookapi.Util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    // Tạo review
    @PostMapping("/user/{userId}/book/{bookId}")
    public ResponseEntity<ReviewModel> createReview(@PathVariable Long userId, @PathVariable Long bookId, @Valid @RequestBody ReviewRequest request) {
        return reviewService.createReview(userId, bookId, request);
    }

    // Lấy tất cả review của sách
    @GetMapping("/book/{bookId}")
    public ResponseEntity<List<ReviewModel>> getReviewsByBookId(@PathVariable Long bookId) {
        return ResponseEntity.ok(reviewService.getReviewsByBookId(bookId));
    }

    // Sửa review
    @PutMapping("/{reviewId}")
    public ResponseEntity<ReviewModel> updateReview(@PathVariable Long reviewId, @Valid @RequestBody ReviewRequest request) {
        return reviewService.updateReview(reviewId, request);
    }

    // Xóa review
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<String> deleteReview(@PathVariable Long reviewId) {
        return reviewService.deleteReview(reviewId);
    }
}