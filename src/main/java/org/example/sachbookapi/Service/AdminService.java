package org.example.sachbookapi.Service;

import org.example.sachbookapi.Entity.BookModel;
import org.example.sachbookapi.Entity.CategoryModel;
import org.example.sachbookapi.Entity.OrderModel;
import org.example.sachbookapi.Entity.UserModel;
import org.example.sachbookapi.Repository.OrderRepository;
import org.example.sachbookapi.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService {
    @Autowired
    private BookService bookService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DiscountService discountService;

    @Autowired
    private CategoryService categoryService;

    public BookModel createBook(BookModel book, Long categoryId) {
        return bookService.createBook(book, categoryId);
    }

    public BookModel updateBook(Long id, BookModel book, Long categoryId) {
        return bookService.updateBook(id, book, categoryId);
    }

    public void deleteBook(Long id) {
        bookService.deleteBook(id);
    }

    public OrderModel updateOrderStatus(Long orderId, String status) {
        OrderModel order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        order.setStatus(status);
        return orderRepository.save(order);
    }

    public UserModel updateUser(Long id, UserModel user) {
        UserModel existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        existingUser.setName(user.getName());
        existingUser.setEmail(user.getEmail());
        existingUser.setPhone(user.getPhone());
        existingUser.setAddress(user.getAddress());
        existingUser.setRole(user.getRole());
        existingUser.setIsActive(user.getIsActive());
        return userRepository.save(existingUser);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
    // Category methods
    public CategoryModel createCategory(CategoryModel category) {
        return categoryService.createCategory(category);
    }

    public CategoryModel updateCategory(Long id, CategoryModel updatedCategory) {
        return categoryService.updateCategory(id, updatedCategory);
    }

    public void deleteCategory(Long id) {
        categoryService.deleteCategory(id);
    }

}
