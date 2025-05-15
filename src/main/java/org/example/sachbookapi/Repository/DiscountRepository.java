package org.example.sachbookapi.Repository;

import org.example.sachbookapi.Entity.DiscountModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DiscountRepository extends JpaRepository<DiscountModel, Long> {
    Optional<DiscountModel> findByCode(String code);
}
