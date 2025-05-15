package org.example.sachbookapi.Repository;

import org.example.sachbookapi.Entity.PaymentModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentModel, Long> {
    Optional<PaymentModel> findByOrderId(Long orderId);
}