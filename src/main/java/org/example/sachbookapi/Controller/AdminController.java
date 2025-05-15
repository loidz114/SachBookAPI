package org.example.sachbookapi.Controller;

import org.example.sachbookapi.Entity.BookModel;
import org.example.sachbookapi.Entity.OrderModel;
import org.example.sachbookapi.Entity.UserModel;
import org.example.sachbookapi.Entity.CategoryModel;
import org.example.sachbookapi.Service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    // Chỉ cho phép người dùng có vai trò ADMIN mới có quyền thêm sách
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/books")
    public ResponseEntity<BookModel> createBook(@RequestBody BookModel book,
                                                @RequestParam Long categoryId) {
        return ResponseEntity.ok(adminService.createBook(book, categoryId));
    }

    // Chỉ cho phép người dùng có vai trò ADMIN mới có quyền cập nhật sách
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/books/{id}")
    public ResponseEntity<BookModel> updateBook(@PathVariable Long id,
                                                @RequestBody BookModel book,
                                                @RequestParam(required = false) Long categoryId) {
        return ResponseEntity.ok(adminService.updateBook(id, book, categoryId));
    }

    // Chỉ cho phép người dùng có vai trò ADMIN mới có quyền xóa sách
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/books/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        adminService.deleteBook(id);
        return ResponseEntity.ok().build();
    }

    // Chỉ cho phép người dùng có vai trò ADMIN mới có quyền cập nhật trạng thái đơn hàng
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/orders/{orderId}/status")
    public ResponseEntity<OrderModel> updateOrderStatus(@PathVariable Long orderId,
                                                        @RequestParam String status) {
        return ResponseEntity.ok(adminService.updateOrderStatus(orderId, status));
    }

    // Chỉ cho phép người dùng có vai trò ADMIN mới có quyền cập nhật thông tin người dùng
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/users/{id}")
    public ResponseEntity<UserModel> updateUser(@PathVariable Long id,
                                                @RequestBody UserModel user) {
        return ResponseEntity.ok(adminService.updateUser(id, user));
    }

    // Chỉ cho phép người dùng có vai trò ADMIN mới có quyền xóa người dùng
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        adminService.deleteUser(id);
        return ResponseEntity.ok().build();
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/categories")
    public ResponseEntity<CategoryModel> createCategory(@RequestBody CategoryModel category) {
        return ResponseEntity.ok(adminService.createCategory(category));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/categories/{id}")
    public ResponseEntity<CategoryModel> updateCategory(@PathVariable Long id,
                                                        @RequestBody CategoryModel category) {
        return ResponseEntity.ok(adminService.updateCategory(id, category));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/categories/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        adminService.deleteCategory(id);
        return ResponseEntity.ok().build();
    }

}
