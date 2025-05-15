package org.example.sachbookapi.Controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.example.sachbookapi.Dto.CartItemRequest;
import org.example.sachbookapi.Entity.CartModel;
import org.example.sachbookapi.Repository.UserRepository;
import org.example.sachbookapi.Service.CartService;
import org.example.sachbookapi.Util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin(origins = "*")
public class CartController {
    @Autowired
    private CartService cartService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    private Long getUserIdFromToken(String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            throw new RuntimeException("Invalid or missing Authorization header");
        }
        String username = jwtUtil.getUsernameFromToken(token.substring(7));
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"))
                .getId();
    }

    @GetMapping
    public ResponseEntity<CartModel> getCart(@RequestHeader("Authorization") String token) {
        Long userId = getUserIdFromToken(token);
        return ResponseEntity.ok(cartService.getCartByUserId(userId));
    }

    @PostMapping("/add")
    public ResponseEntity<CartModel> addToCart(
            @RequestHeader("Authorization") String token,
            @Valid @RequestBody CartItemRequest request) {
        Long userId = getUserIdFromToken(token);
        return ResponseEntity.ok(cartService.addToCart(userId, request));
    }

    @PutMapping("/item/{cartItemId}")
    public ResponseEntity<CartModel> updateCartItem(
            @RequestHeader("Authorization") String token,
            @PathVariable Long cartItemId,
            @RequestParam @Min(0) Integer quantity) {
        Long userId = getUserIdFromToken(token);
        return ResponseEntity.ok(cartService.updateCartItem(userId, cartItemId, quantity));
    }

    @DeleteMapping("/item/{cartItemId}")
    public ResponseEntity<Void> removeFromCart(
            @RequestHeader("Authorization") String token,
            @PathVariable Long cartItemId) {
        Long userId = getUserIdFromToken(token);
        cartService.removeFromCart(userId, cartItemId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/total")
    public ResponseEntity<Double> getCartTotal(
            @RequestHeader("Authorization") String token,
            @RequestParam(required = false) String discountCode) {
        Long userId = getUserIdFromToken(token);
        return ResponseEntity.ok(cartService.calculateCartTotal(userId, discountCode));
    }
}