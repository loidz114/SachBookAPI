package org.example.sachbookapi.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "orders")
public class OrderModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Người dùng đặt đơn hàng
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserModel user;

    // Danh sách sản phẩm trong đơn
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItemModel> orderItems = new ArrayList<>();

    @NotNull(message = "Total amount is required")
    @Min(value = 0, message = "Total amount must be non-negative")
    @Column(name = "total_amount", nullable = false)
    private Double totalAmount;

    @Min(value = 0, message = "Discount amount must be non-negative")
    @Column(name = "discount_amount")
    private Double discountAmount;

    @NotNull(message = "Final amount is required")
    @Min(value = 0, message = "Final amount must be non-negative")
    @Column(name = "final_amount", nullable = false)
    private Double finalAmount;

    // Mã giảm giá áp dụng cho đơn
    @ManyToOne
    @JoinColumn(name = "discount_id")
    private DiscountModel discount;

    @NotBlank(message = "Payment method is required")
    @Column(name = "payment_method", nullable = false)
    private String paymentMethod; // CASH, MOMO, VNPAY

    @NotBlank(message = "Status is required")
    @Column(name = "status", nullable = false)
    private String status; // PENDING, PAID, SHIPPED, DELIVERED, CANCELLED

    @NotBlank(message = "Shipping address is required")
    @Size(min = 1, max = 255, message = "Shipping address must be between 1 and 255 characters")
    @Column(name = "shipping_address", nullable = false) // Changed to match database schema
    private String shippingAddress;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false, updatable = false)
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at")
    private Date updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = new Date();
        this.updatedAt = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = new Date();
    }
}
