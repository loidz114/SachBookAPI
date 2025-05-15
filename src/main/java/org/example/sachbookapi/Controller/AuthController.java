package org.example.sachbookapi.Controller;

import org.example.sachbookapi.Dto.*;
import org.example.sachbookapi.Entity.UserModel;
import org.example.sachbookapi.Service.AuthService;
import org.example.sachbookapi.Util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@Valid @RequestBody RegisterRequest request) {
        try {
            String message = authService.register(request);
            return ResponseEntity.ok(new RegisterResponse(true, message));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new RegisterResponse(false, "Error during registration: " + e.getMessage()));
        }
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<RegisterResponse> verifyOtp(@Valid @RequestBody RegisterRequest request, @RequestParam String otp) {
        try {
            String message = authService.verifyOtpAndRegister(request, otp);
            return ResponseEntity.ok(new RegisterResponse(true, message));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new RegisterResponse(false, "Error during OTP verification: " + e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        try {
            LoginResponse loginResponse = authService.login(request);
            return ResponseEntity.ok(loginResponse);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new LoginResponse(false, "Error during login: " + e.getMessage()));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        try {
            return ResponseEntity.ok(authService.logout());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error during logout: " + e.getMessage());
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ForgotPasswordResponse> forgotPassword(@RequestParam String email) {
        try {
            String message = authService.forgotPassword(email);
            return ResponseEntity.ok(new ForgotPasswordResponse(true, message));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ForgotPasswordResponse(false, "Error during forgot password: " + e.getMessage()));
        }
    }

    @PostMapping("/verify-otp-forgot")
    public ResponseEntity<ForgotPasswordResponse> verifyOtpForgot(@Valid @RequestBody ResetPasswordRequest request) {
        try {
            boolean isValid = authService.validateOtp(request.getEmail(), request.getOtp());
            if (isValid) {
                return ResponseEntity.ok(new ForgotPasswordResponse(true, "OTP verified successfully"));
            } else {
                return ResponseEntity.badRequest().body(new ForgotPasswordResponse(false, "Invalid or expired OTP"));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ForgotPasswordResponse(false, "Error during OTP verification: " + e.getMessage()));
        }
    }
    @GetMapping("/profile")
    public ResponseEntity<UserModel> getProfile(@RequestHeader("Authorization") String token) {
        try {
            if (token == null || !token.startsWith("Bearer ")) {
                return ResponseEntity.status(401).body(null);
            }
            String username = jwtUtil.getUsernameFromToken(token.substring(7));
            UserModel user = authService.getUserProfile(username);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
    @PostMapping("/reset-password")
    public ResponseEntity<ResetPasswordResponse> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        try {
            String message = authService.resetPassword(request);
            return ResponseEntity.ok(new ResetPasswordResponse(true, message));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResetPasswordResponse(false, "Error during reset password: " + e.getMessage()));
        }
    }

    @PutMapping("/profile")
    public ResponseEntity<String> updateProfile(
            @Valid @RequestBody UpdateProfileRequest request,
            @RequestHeader("Authorization") String token) {
        try {
            if (token == null || !token.startsWith("Bearer ")) {
                return ResponseEntity.status(401).body("Invalid or missing Authorization header");
            }
            String username = jwtUtil.getUsernameFromToken(token.substring(7));
            return ResponseEntity.ok(authService.updateProfile(username, request));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error during profile update: " + e.getMessage());
        }
    }

    @PutMapping("/change-password")
    public ResponseEntity<String> changePassword(
            @Valid @RequestBody ChangePasswordRequest request,
            @RequestHeader("Authorization") String token) {
        try {
            if (token == null || !token.startsWith("Bearer ")) {
                return ResponseEntity.status(401).body("Invalid or missing Authorization header");
            }
            String username = jwtUtil.getUsernameFromToken(token.substring(7));
            return ResponseEntity.ok(authService.changePassword(username, request));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error during password change: " + e.getMessage());
        }
    }

    @GetMapping("/validate-token")
    public ResponseEntity<Boolean> validateToken(@RequestParam String token) {
        try {
            boolean isValid = authService.validateToken(token);
            return ResponseEntity.ok(isValid);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(false);
        }
    }
}