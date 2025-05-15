package org.example.sachbookapi.Controller;

import jakarta.validation.Valid;
import org.example.sachbookapi.Dto.OrderRequest;
import org.example.sachbookapi.Entity.OrderModel;
import org.example.sachbookapi.Exception.UnauthorizedException;
import org.example.sachbookapi.Service.OrderService;
import org.example.sachbookapi.Util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*") // Allow frontend requests
public class OrderController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private JwtUtil jwtUtil;

    private Long getUserIdFromToken(String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            throw new UnauthorizedException("Invalid or missing Authorization header");
        }
        String username = jwtUtil.getUsernameFromToken(token.substring(7));
        return orderService.getUserIdByUsername(username);
    }

    // --- USER APIs ---

    // Tạo đơn hàng cho User
    @PostMapping
    public ResponseEntity<OrderModel> createOrder(
            @RequestHeader("Authorization") String token,
            @Valid @RequestBody OrderRequest request) {
        Long userId = getUserIdFromToken(token);
        return ResponseEntity.ok(orderService.createOrder(userId, request));
    }

    // Xem tất cả đơn hàng của User
    @GetMapping
    public ResponseEntity<List<OrderModel>> getUserOrders(
            @RequestHeader("Authorization") String token) {
        Long userId = getUserIdFromToken(token);
        return ResponseEntity.ok(orderService.getOrdersByUserId(userId));
    }

    // Xem chi tiết đơn hàng của User
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderModel> getUserOrderById(
            @RequestHeader("Authorization") String token,
            @PathVariable Long orderId) {
        Long userId = getUserIdFromToken(token);
        return ResponseEntity.ok(orderService.getUserOrderById(userId, orderId));
    }

    // Huỷ đơn hàng của User
    @PutMapping("/cancel/{orderId}")
    public ResponseEntity<OrderModel> cancelOrder(
            @RequestHeader("Authorization") String token,
            @PathVariable Long orderId) {
        Long userId = getUserIdFromToken(token);
        return ResponseEntity.ok(orderService.cancelOrder(userId, orderId));
    }

    // --- ADMIN APIs ---

    // Xem tất cả đơn hàng của tất cả User (Admin only)
    @GetMapping("/admin/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<OrderModel>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    // Xem chi tiết đơn hàng (Admin)
    @GetMapping("/admin/{orderId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OrderModel> getOrderById(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.getOrderById(orderId));
    }

    // Admin huỷ đơn hàng
    @PutMapping("/admin/cancel/{orderId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OrderModel> cancelOrderByAdmin(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.cancelOrderByAdmin(orderId));
    }

    // Admin cập nhật trạng thái đơn hàng
    @PutMapping("/admin/{orderId}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OrderModel> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestParam String status) {
        return ResponseEntity.ok(orderService.updateOrderStatus(orderId, status));
    }
}