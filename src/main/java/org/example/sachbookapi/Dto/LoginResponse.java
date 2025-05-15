package org.example.sachbookapi.Dto;

import lombok.Data;

@Data
public class LoginResponse {
    private boolean success;
    private String message;
    private String token; // JWT token (nếu đăng nhập thành công)

    public LoginResponse(boolean success, String message, String token) {
        this.success = success;
        this.message = message;
        this.token = token;
    }

    // Constructor cho trường hợp không có token (lỗi)
    public LoginResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
        this.token = null;
    }
}