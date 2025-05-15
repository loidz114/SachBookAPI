package org.example.sachbookapi.Service;

import jakarta.transaction.Transactional;
import org.example.sachbookapi.Dto.OrderRequest;
import org.example.sachbookapi.Dto.PaymentRequest;
import org.example.sachbookapi.Entity.*;
import org.example.sachbookapi.Exception.NotFoundException;
import org.example.sachbookapi.Exception.UnauthorizedException;
import org.example.sachbookapi.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private DiscountRepository discountRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private PaymentService paymentService;

    @Transactional(rollbackOn = Exception.class)
    public OrderModel createOrder(Long userId, OrderRequest request) {
        // Validate payment method
        String paymentMethod = request.getPaymentMethod() != null ? request.getPaymentMethod().toUpperCase() : null;
        if (paymentMethod == null || !List.of("CASH", "MOMO", "VNPAY").contains(paymentMethod)) {
            throw new IllegalArgumentException("Invalid payment method: " + (paymentMethod != null ? paymentMethod : "null"));
        }

        // Validate shipping address
        String shippingAddress = request.getShippingAddress();
        if (shippingAddress == null || shippingAddress.trim().isEmpty()) {
            throw new IllegalArgumentException("Shipping address is required and cannot be empty");
        }

        // Get cart
        CartModel cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException("Cart not found for user ID: " + userId));
        if (cart.getItems().isEmpty()) {
            throw new IllegalArgumentException("Cart is empty");
        }

        // Get user
        UserModel user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found with ID: " + userId));

        // Validate stock
        for (CartItemModel item : cart.getItems()) {
            BookModel book = bookRepository.findById(item.getBook().getId())
                    .orElseThrow(() -> new NotFoundException("Book not found with ID: " + item.getBook().getId()));
            if (book.getQuantity() < item.getQuantity()) {
                throw new IllegalArgumentException("Insufficient stock for book: " + book.getTitle());
            }
        }

        // Create order
        OrderModel order = new OrderModel();
        order.setUser(user);
        order.setPaymentMethod(paymentMethod);
        order.setStatus("PENDING");
        order.setShippingAddress(shippingAddress.trim());

        // Add order items
        double totalAmount = 0;
        for (CartItemModel item : cart.getItems()) {
            OrderItemModel orderItem = new OrderItemModel();
            orderItem.setBook(item.getBook());
            orderItem.setQuantity(item.getQuantity());
            orderItem.setUnitPrice(item.getPrice());
            orderItem.setTotalPrice(item.getPrice() * item.getQuantity());
            orderItem.setOrder(order);
            order.getOrderItems().add(orderItem);
            totalAmount += orderItem.getTotalPrice();
        }

        order.setTotalAmount(totalAmount);

        // Apply discount
        if (request.getDiscountCode() != null && !request.getDiscountCode().isEmpty()) {
            DiscountModel discount = discountRepository.findByCode(request.getDiscountCode())
                    .orElseThrow(() -> new NotFoundException("Invalid discount code: " + request.getDiscountCode()));
            if (!discount.getIsActive() ||
                    (discount.getEndDate() != null && discount.getEndDate().before(new Date())) ||
                    (discount.getMaxUsage() != null && discount.getMaxUsage() <= 0)) {
                throw new IllegalArgumentException("Discount code is not valid or has expired");
            }
            double discountAmount = discount.getDiscountAmount() != null
                    ? discount.getDiscountAmount()
                    : totalAmount * (discount.getDiscountPercent() / 100);
            order.setDiscount(discount);
            order.setDiscountAmount(discountAmount);
            order.setFinalAmount(totalAmount - discountAmount);
            // Update maxUsage
            if (discount.getMaxUsage() != null) {
                discount.setMaxUsage(discount.getMaxUsage() - 1);
                discountRepository.save(discount);
            }
        } else {
            order.setFinalAmount(totalAmount);
        }

        // Save order
        OrderModel savedOrder = orderRepository.save(order);

        // Create payment
        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setOrderId(savedOrder.getId());
        paymentRequest.setPaymentMethod(paymentMethod);
        paymentRequest.setTransactionId(paymentMethod + "_" + System.currentTimeMillis());
        paymentService.processPayment(paymentRequest);

        // Update stock and clear cart
        for (CartItemModel item : cart.getItems()) {
            BookModel book = bookRepository.findById(item.getBook().getId()).get();
            book.setQuantity(book.getQuantity() - item.getQuantity());
            bookRepository.save(book);
        }
        cart.getItems().clear();
        cartRepository.save(cart);

        return savedOrder;
    }

    public List<OrderModel> getOrdersByUserId(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User not found with ID: " + userId);
        }
        return orderRepository.findByUserId(userId);
    }

    public OrderModel getUserOrderById(Long userId, Long orderId) {
        OrderModel order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found with ID: " + orderId));
        if (!order.getUser().getId().equals(userId)) {
            throw new UnauthorizedException("You are not authorized to view this order");
        }
        return order;
    }

    public OrderModel getOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found with ID: " + orderId));
    }

    @Transactional
    public OrderModel cancelOrder(Long userId, Long orderId) {
        OrderModel order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found with ID: " + orderId));
        if (!order.getUser().getId().equals(userId)) {
            throw new UnauthorizedException("You are not authorized to cancel this order");
        }
        if (!order.getStatus().equals("PENDING")) {
            throw new IllegalArgumentException("Order cannot be canceled in its current state: " + order.getStatus());
        }
        order.setStatus("CANCELLED");

        // Restore stock
        for (OrderItemModel orderItem : order.getOrderItems()) {
            BookModel book = orderItem.getBook();
            book.setQuantity(book.getQuantity() + orderItem.getQuantity());
            bookRepository.save(book);
        }

        // Update payment status
        PaymentModel payment = paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new NotFoundException("Payment not found for order ID: " + orderId));
        payment.setStatus("CANCELLED");
        paymentRepository.save(payment);

        return orderRepository.save(order);
    }

    @Transactional
    public OrderModel cancelOrderByAdmin(Long orderId) {
        OrderModel order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found with ID: " + orderId));
        if (!order.getStatus().equals("PENDING")) {
            throw new IllegalArgumentException("Order cannot be canceled in its current state: " + order.getStatus());
        }
        order.setStatus("CANCELLED");

        // Restore stock
        for (OrderItemModel orderItem : order.getOrderItems()) {
            BookModel book = orderItem.getBook();
            book.setQuantity(book.getQuantity() + orderItem.getQuantity());
            bookRepository.save(book);
        }

        // Update payment status
        PaymentModel payment = paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new NotFoundException("Payment not found for order ID: " + orderId));
        payment.setStatus("CANCELLED");
        paymentRepository.save(payment);

        return orderRepository.save(order);
    }

    @Transactional
    public OrderModel updateOrderStatus(Long orderId, String status) {
        OrderModel order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found with ID: " + orderId));
        if (!List.of("PENDING", "PAID", "SHIPPED", "DELIVERED", "CANCELLED").contains(status)) {
            throw new IllegalArgumentException("Invalid status: " + status);
        }
        order.setStatus(status);
        if (status.equals("CANCELLED")) {
            // Restore stock
            for (OrderItemModel orderItem : order.getOrderItems()) {
                BookModel book = orderItem.getBook();
                book.setQuantity(book.getQuantity() + orderItem.getQuantity());
                bookRepository.save(book);
            }
            // Update payment status
            PaymentModel payment = paymentRepository.findByOrderId(orderId)
                    .orElseThrow(() -> new NotFoundException("Payment not found for order ID: " + orderId));
            payment.setStatus("CANCELLED");
            paymentRepository.save(payment);
        }
        return orderRepository.save(order);
    }

    public List<OrderModel> getAllOrders() {
        return orderRepository.findAll();
    }

    public Long getUserIdByUsername(String username) {
        UserModel user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User not found with username: " + username));
        return user.getId();
    }
}
