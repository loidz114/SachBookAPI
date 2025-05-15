package org.example.sachbookapi.Controller;

import jakarta.validation.Valid;
import org.example.sachbookapi.Dto.PaymentRequest;
import org.example.sachbookapi.Entity.PaymentModel;
import org.example.sachbookapi.Service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {
    @Autowired
    private PaymentService paymentService;

    @PostMapping
    public ResponseEntity<PaymentModel> processPayment(@Valid @RequestBody PaymentRequest request) {
        return ResponseEntity.ok(paymentService.processPayment(request));
    }
}