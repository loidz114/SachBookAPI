package org.example.sachbookapi.Service;

import org.example.sachbookapi.Dto.CartItemRequest;
import org.example.sachbookapi.Entity.*;
import org.example.sachbookapi.Repository.BookRepository;
import org.example.sachbookapi.Repository.CartItemRepository;
import org.example.sachbookapi.Repository.CartRepository;
import org.example.sachbookapi.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CartService {
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DiscountService discountService;

    public CartModel getCartByUserId(Long userId) {
        return cartRepository.findByUserId(userId)
                .orElseGet(() -> createCart(userId));
    }

    private CartModel createCart(Long userId) {
        UserModel user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        CartModel cart = new CartModel();
        cart.setUser(user);
        return cartRepository.save(cart);
    }

    public CartModel addToCart(Long userId, CartItemRequest request) {
        CartModel cart = getCartByUserId(userId);
        BookModel book = bookRepository.findById(request.getBookId())
                .orElseThrow(() -> new RuntimeException("Book not found"));

        Optional<CartItemModel> existingItem = cartItemRepository.findByCartIdAndBookId(cart.getId(), request.getBookId());
        if (existingItem.isPresent()) {
            CartItemModel item = existingItem.get();
            item.setQuantity(item.getQuantity() + request.getQuantity());
            item.setPrice(book.getPrice());
            cartItemRepository.save(item);
        } else {
            CartItemModel item = new CartItemModel();
            item.setCart(cart);
            item.setBook(book);
            item.setQuantity(request.getQuantity());
            item.setPrice(book.getPrice());
            cart.getItems().add(item);
            cartItemRepository.save(item);
        }
        return cartRepository.save(cart);
    }

    public CartModel updateCartItem(Long userId, Long cartItemId, Integer quantity) {
        CartModel cart = getCartByUserId(userId);
        CartItemModel item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));
        if (!item.getCart().getId().equals(cart.getId())) {
            throw new RuntimeException("Cart item does not belong to this cart");
        }
        if (quantity <= 0) {
            cart.getItems().remove(item);
            cartItemRepository.delete(item);
        } else {
            item.setQuantity(quantity);
            cartItemRepository.save(item);
        }
        return cartRepository.save(cart);
    }

    public void removeFromCart(Long userId, Long cartItemId) {
        CartModel cart = getCartByUserId(userId);
        CartItemModel item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));
        if (!item.getCart().getId().equals(cart.getId())) {
            throw new RuntimeException("Cart item does not belong to this cart");
        }
        cart.getItems().remove(item);
        cartItemRepository.delete(item);
        cartRepository.save(cart);
    }

    public Double calculateCartTotal(Long userId, String discountCode) {
        CartModel cart = getCartByUserId(userId);
        double total = cart.getItems().stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();

        if (discountCode != null && !discountCode.isEmpty()) {
            DiscountModel discount = discountService.applyDiscount(discountCode);
            if (discount.getDiscountPercent() != null && discount.getDiscountPercent() > 0) {
                total -= total * (discount.getDiscountPercent() / 100);
            } else if (discount.getDiscountAmount() != null && discount.getDiscountAmount() > 0) {
                total -= discount.getDiscountAmount();
            }
        }
        return Math.max(total, 0);
    }

    public void clearCart(Long userId) {
        CartModel cart = getCartByUserId(userId);
        cart.getItems().clear();
        cartItemRepository.deleteByCartId(cart.getId());
        cartRepository.save(cart);
    }
}