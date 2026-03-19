package com.policy.domain.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Converter
public class Aes256Converter implements AttributeConverter<String, String> {

    private static final String ALGORITHM = "AES/GCM/NoPadding";
    private static final int IV_LENGTH = 12;
    private static final int TAG_LENGTH = 128;

    @Value("${security.aes.secret-key}")
    private String secretKey;

    @Override
    public String convertToDatabaseColumn(String attribute) {
        if (attribute == null || attribute.isBlank()) {
            return attribute;
        }

        try {
            byte[] iv = new byte[IV_LENGTH];
            new SecureRandom().nextBytes(iv);

            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, buildKey(), new GCMParameterSpec(TAG_LENGTH, iv));

            byte[] encrypted = cipher.doFinal(attribute.getBytes(StandardCharsets.UTF_8));
            byte[] ivAndCipher = new byte[iv.length + encrypted.length];
            System.arraycopy(iv, 0, ivAndCipher, 0, iv.length);
            System.arraycopy(encrypted, 0, ivAndCipher, iv.length, encrypted.length);

            return Base64.getEncoder().encodeToString(ivAndCipher);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to encrypt field with AES-256.", e);
        }
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isBlank()) {
            return dbData;
        }

        try {
            byte[] ivAndCipher = Base64.getDecoder().decode(dbData);
            byte[] iv = new byte[IV_LENGTH];
            byte[] encrypted = new byte[ivAndCipher.length - IV_LENGTH];

            System.arraycopy(ivAndCipher, 0, iv, 0, IV_LENGTH);
            System.arraycopy(ivAndCipher, IV_LENGTH, encrypted, 0, encrypted.length);

            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, buildKey(), new GCMParameterSpec(TAG_LENGTH, iv));

            return new String(cipher.doFinal(encrypted), StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to decrypt field with AES-256.", e);
        }
    }

    private SecretKeySpec buildKey() throws Exception {
        if (secretKey == null || secretKey.isBlank()) {
            throw new IllegalStateException("security.aes.secret-key must be configured.");
        }

        // 32-byte key derived from the external secret for AES-256.
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] keyBytes = digest.digest(secretKey.getBytes(StandardCharsets.UTF_8));
        return new SecretKeySpec(keyBytes, "AES");
    }
}
