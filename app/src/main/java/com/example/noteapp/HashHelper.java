package com.example.noteapp;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashHelper {

    public static String hashFunction(String string) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(string.getBytes(StandardCharsets.UTF_8));

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

    public static boolean verifyHash(String enteredString, String storedHashedString) {
        String enteredPasswordHash = hashFunction(enteredString);
        return enteredPasswordHash != null && enteredPasswordHash.equals(storedHashedString);
    }
}