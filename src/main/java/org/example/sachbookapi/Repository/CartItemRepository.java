package org.example.sachbookapi.Repository;

import org.example.sachbookapi.Entity.CartItemModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItemModel, Long> {
    Optional<CartItemModel> findByCartIdAndBookId(Long cartId, Long bookId);

    void deleteByCartId(Long cartId);
}