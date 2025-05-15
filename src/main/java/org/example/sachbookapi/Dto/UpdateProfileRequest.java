package org.example.sachbookapi.Dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UpdateProfileRequest {
    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 50)
    private String name;

    @NotBlank(message = "Phone is required")
    @Size(min = 10, max = 15)
    private String phone;

    private String address;
}