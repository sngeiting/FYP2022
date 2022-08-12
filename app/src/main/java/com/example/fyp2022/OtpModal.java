package com.example.fyp2022;

public class OtpModal {
    private final int otpID;
    private final String otp;
    private final String otpGen_DateTime;

    public OtpModal(int otpID, String otp, String otpGen_DateTime) {
        this.otpID = otpID;
        this.otp = otp;
        this.otpGen_DateTime = otpGen_DateTime;
    }

    public int getOtpID() {
        return otpID;
    }
    public String getOtp() {
        return otp;
    }
    public String getOtpGen_DateTime() {
        return otpGen_DateTime;
    }

    @Override
    public String toString() {
        return "OtpModal{" +
                "otpID=" + otpID +
                ", otp=" + otp +
                ", otpGen_DateTime='" + otpGen_DateTime + '\'' +
                '}';
    }
}
