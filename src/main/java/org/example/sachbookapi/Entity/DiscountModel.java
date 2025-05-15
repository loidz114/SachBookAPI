package org.example.sachbookapi.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "discounts")
public class DiscountModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Code is required")
    @Size(min = 1, max = 20, message = "Code must be between 1 and 20 characters")
    @Column(unique = true)
    private String code; // SALE10, BOOK20...

    @Size(max = 255, message = "Description must be less than 255 characters")
    private String description;

    @DecimalMin(value = "0.0", message = "Discount percent must be non-negative")
    @DecimalMax(value = "100.0", message = "Discount percent must not exceed 100")
    private Double discountPercent; // Nếu dùng giảm theo %

    @DecimalMin(value = "0.0", message = "Discount amount must be non-negative")
    private Double discountAmount; // Nếu dùng giảm cố định

    @FutureOrPresent(message = "Start date must be in the present or future")
    @Temporal(TemporalType.DATE)
    private Date startDate;

    @Future(message = "End date must be in the future")
    @Temporal(TemporalType.DATE)
    private Date endDate;

    @NotNull(message = "Active status is required")
    private Boolean isActive;

    @Min(value = 1, message = "Max usage must be at least 1")
    private Integer maxUsage;
}