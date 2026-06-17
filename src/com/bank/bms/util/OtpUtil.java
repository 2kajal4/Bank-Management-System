package com.bank.bms.util;

import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.security.SecureRandom;

public class OtpUtil {

    public static String generateOTP() {
        SecureRandom random = new SecureRandom();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }

    // ✅ save OTP to file (overwrite old)
    public static void saveOTP(String otp) {
        try {
            FileWriter fw = new FileWriter("otp.txt"); // overwrite
            fw.write(otp);
            fw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ✅ read OTP from file
    public static String readOTP() {
        try {
            BufferedReader br = new BufferedReader(new FileReader("otp.txt"));
            String otp = br.readLine();
            br.close();
            return otp;
        } catch (Exception e) {
            return null;
        }
    }
}