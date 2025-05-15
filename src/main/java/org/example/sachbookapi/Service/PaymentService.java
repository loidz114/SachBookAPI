package org.example.sachbookapi.Service;

import org.example.sachbookapi.Dto.PaymentRequest;
import org.example.sachbookapi.Entity.OrderModel;
import org.example.sachbookapi.Entity.PaymentModel;
import org.example.sachbookapi.Repository.OrderRepository;
import org.example.sachbookapi.Repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class PaymentService {
    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private OrderRepository orderRepository;

    public PaymentModel processPayment(PaymentRequest request) {
        OrderModel order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // Kiểm tra trạng thái đơn hàng
        if ("PAID".equals(order.getStatus())) {
            throw new RuntimeException("Order already paid");
        }

        PaymentModel payment = new PaymentModel();
        payment.setOrder(order);
        payment.setPaymentMethod(request.getPaymentMethod());
        payment.setTransactionId(request.getTransactionId());
        payment.setPaymentDate(new Date());

        // Simulate payment processing
        if (request.getPaymentMethod().equalsIgnoreCase("MOMO") || request.getPaymentMethod().equalsIgnoreCase("VNPAY")) {
            // Placeholder for SDK integration
            payment.setStatus("COMPLETED");
            order.setStatus("PAID");
        } else if (request.getPaymentMethod().equalsIgnoreCase("CASH")) {
            payment.setStatus("PENDING"); // Cash payments are pending until delivery
            order.setStatus("PENDING");
        } else {
            throw new RuntimeException("Unsupported payment method");
        }

        orderRepository.save(order);
        return paymentRepository.save(payment);
    }
}