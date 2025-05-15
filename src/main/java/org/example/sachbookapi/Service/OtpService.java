package org.example.sachbookapi.Service;

import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OtpService {
    private final Map<String, OtpData> otpStore = new ConcurrentHashMap<>();
    private static final int OTP_VALIDITY = 5 * 60 * 1000; // 5 phút

    public String generateOtp(String email) {
        String otp = String.format("%06d", new Random().nextInt(999999));
        otpStore.put(email, new OtpData(otp, System.currentTimeMillis() + OTP_VALIDITY));
        System.out.println("Generated OTP for " + email + ": " + otp);
        return otp;
    }

    public boolean validateOtp(String email, String otp) {
        OtpData otpData = otpStore.get(email);
        if (otpData == null) {
            System.out.println("No OTP found for " + email);
            return false;
        }
        boolean isValid = otpData.getOtp().equals(otp) && System.currentTimeMillis() <= otpData.getExpirationTime();
        System.out.println("Validating OTP for " + email + ": input=" + otp + ", stored=" + otpData.getOtp() + ", isValid=" + isValid);
        return isValid;
    }

    public void removeOtp(String email) {
        otpStore.remove(email);
        System.out.println("OTP removed for " + email);
    }

    private static class OtpData {
        private final String otp;
        private final long expirationTime;

        public OtpData(String otp, long expirationTime) {
            this.otp = otp;
            this.expirationTime = expirationTime;
        }

        public String getOtp() {
            return otp;
        }

        public long getExpirationTime() {
            return expirationTime;
        }
    }
}