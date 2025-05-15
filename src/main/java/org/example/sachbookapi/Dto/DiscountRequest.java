package org.example.sachbookapi.Dto;

import jakarta.validation.constraints.*;

import java.util.Date;

public class DiscountRequest {
    @NotBlank(message = "Code is required")
    @Size(min = 1, max = 20, message = "Code must be between 1 and 20 characters")
    private String code;

    @Size(max = 255, message = "Description must be less than 255 characters")
    private String description;

    @DecimalMin(value = "0.0", message = "Discount percent must be non-negative")
    @DecimalMax(value = "100.0", message = "Discount percent must not exceed 100")
    private Double discountPercent;

    @DecimalMin(value = "0.0", message = "Discount amount must be non-negative")
    private Double discountAmount;

    @FutureOrPresent(message = "Start date must be in the present or future")
    private Date startDate;

    @Future(message = "End date must be in the future")
    private Date endDate;

    @NotNull(message = "Active status is required")
    private Boolean isActive;

    @Min(value = 1, message = "Max usage must be at least 1")
    private Integer maxUsage;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(Double discountPercent) {
        this.discountPercent = discountPercent;
    }

    public Double getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(Double discountAmount) {
        this.discountAmount = discountAmount;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Integer getMaxUsage() {
        return maxUsage;
    }

    public void setMaxUsage(Integer maxUsage) {
        this.maxUsage = maxUsage;
    }
}