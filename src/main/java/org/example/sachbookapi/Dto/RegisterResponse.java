package org.example.sachbookapi.Dto;

import lombok.Data;

@Data
public class RegisterResponse {
    private boolean success;
    private String message;

    public RegisterResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
}