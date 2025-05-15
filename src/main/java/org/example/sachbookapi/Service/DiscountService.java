package org.example.sachbookapi.Service;

import org.example.sachbookapi.Dto.DiscountRequest;
import org.example.sachbookapi.Entity.DiscountModel;
import org.example.sachbookapi.Repository.DiscountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class DiscountService {
    @Autowired
    private DiscountRepository discountRepository;

    public DiscountModel createDiscount(DiscountRequest request) {
        if (discountRepository.findByCode(request.getCode()).isPresent()) {
            throw new RuntimeException("Discount code already exists");
        }
        DiscountModel discount = new DiscountModel();
        discount.setCode(request.getCode());
        discount.setDescription(request.getDescription());
        discount.setDiscountPercent(request.getDiscountPercent());
        discount.setDiscountAmount(request.getDiscountAmount());
        discount.setStartDate(request.getStartDate());
        discount.setEndDate(request.getEndDate());
        discount.setIsActive(request.getIsActive());
        return discountRepository.save(discount);
    }

    public DiscountModel applyDiscount(String code) {
        DiscountModel discount = discountRepository.findByCode(code)
                .orElseThrow(() -> new RuntimeException("Invalid discount code"));
        if (!discount.getIsActive() || (discount.getEndDate() != null && discount.getEndDate().before(new Date()))) {
            throw new RuntimeException("Discount code is not valid");
        }
        return discount;
    }

    public DiscountModel updateDiscount(Long id, DiscountRequest request) {
        DiscountModel discount = discountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Discount not found"));
        discount.setCode(request.getCode());
        discount.setDescription(request.getDescription());
        discount.setDiscountPercent(request.getDiscountPercent());
        discount.setDiscountAmount(request.getDiscountAmount());
        discount.setStartDate(request.getStartDate());
        discount.setEndDate(request.getEndDate());
        discount.setIsActive(request.getIsActive());
        return discountRepository.save(discount);
    }

    public void deleteDiscount(Long id) {
        discountRepository.deleteById(id);
    }
}