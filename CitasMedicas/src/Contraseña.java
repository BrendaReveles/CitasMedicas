package com.clinica.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public final class Contraseña {
    private Password() {}

    public static String hash(String plain) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] b = md.digest(plain.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte x : b) sb.append(String.format("%02x", x));
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("No se pudo calcular hash", e);
        }
    }
}
