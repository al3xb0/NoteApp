package com.example.noteapp;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HasherHelper {

    public static String hashFunction(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(password.getBytes(StandardCharsets.UTF_8));

            // bytes to string
            StringBuilder hexStringBuilder = new StringBuilder();
            for (byte b : hashBytes) {
                hexStringBuilder.append(String.format("%02x", b));
            }
            return hexStringBuilder.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean verifyHash(String enteredPassword, String storedHashedPassword) {
        String enteredPasswordHash = hashFunction(enteredPassword);
        return enteredPasswordHash != null && enteredPasswordHash.equals(storedHashedPassword);
    }
}