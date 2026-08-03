package com.example.vacationplanner.utilities;

import android.content.SharedPreferences;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Base64;

import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;

/**
 * Utility class for handling encryption and decryption using Android KeyStore.
 */
public class CryptoUtil {
    private final SharedPreferences sharedPreferences;
    private final KeyStore keyStore;

    /**
     * Constructs a CryptoUtil instance and initializes the KeyStore.
     * @param sharedPreferences SharedPreferences to store encrypted keys and IVs.
     * @throws Exception If KeyStore initialization fails.
     */
    public CryptoUtil(SharedPreferences sharedPreferences) throws Exception {
        this.sharedPreferences = sharedPreferences;
        this.keyStore = KeyStore.getInstance("AndroidKeyStore");
        this.keyStore.load(null);
        initialize();
    }

    private void initialize() throws Exception{
        generateKeystoreKeyIfNeeded();
        if(!sharedPreferences.contains("encrypted_key")){
            generateAndEncryptSqlCipherKey();
        }
    }

    private void generateKeystoreKeyIfNeeded() throws Exception{
        if(!keyStore.containsAlias("vacation_secret_key")){
            KeyGenerator keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
            KeyGenParameterSpec keyGenSpec = new KeyGenParameterSpec.Builder(
                    "vacation_secret_key",
                    KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                    .build();
            keyGenerator.init(keyGenSpec);
            keyGenerator.generateKey();
        }
    }

    private void generateAndEncryptSqlCipherKey() throws Exception {
        SecretKey secretKey = getSecretKey("vacation_secret_key");
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        byte[] vacationKey = new byte[32];
        new SecureRandom().nextBytes(vacationKey);

        byte[] encryptedKey = cipher.doFinal(vacationKey);
        byte[] iv = cipher.getIV();

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("encrypted_key", Base64.encodeToString(encryptedKey, Base64.NO_WRAP));
        editor.putString("encryption_iv", Base64.encodeToString(iv, Base64.NO_WRAP));
        editor.apply();

        for (int i = 0; i < vacationKey.length; i++) {
            vacationKey[i] = 0;
        }
    }

    private SecretKey getSecretKey(String keyAlias) throws Exception {
        KeyStore.SecretKeyEntry entry = (KeyStore.SecretKeyEntry) keyStore.getEntry(keyAlias, null);
        return entry.getSecretKey();
    }

    /**
     * Compares an encrypted text with a plain text string.
     * @param encryptedText The encrypted text (Base64 encoded with IV).
     * @param text The plain text to compare.
     * @return true if the decrypted text matches the input text, false otherwise.
     * @throws Exception If decryption fails.
     */
    public boolean compare(String encryptedText, String text) throws Exception {
        return text.equals(decrypt(encryptedText));
    }

    /**
     * Encrypts a plain text string using AES/GCM.
     * @param text The plain text to encrypt.
     * @return A Base64 encoded string containing the cipher text and IV, separated by a dot.
     * @throws Exception If encryption fails.
     */
    public String encrypt(String text) throws Exception {
        SecretKey secretKey = getSecretKey("vacation_secret_key");
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        byte[] encryptedBytes = cipher.doFinal(text.getBytes(StandardCharsets.UTF_8));
        String cipherText = Base64.encodeToString(encryptedBytes, Base64.DEFAULT);
        String iv = Base64.encodeToString(cipher.getIV(), Base64.DEFAULT);

        return cipherText + "." + iv;
    }

    /**
     * Decrypts an encrypted text string.
     * @param cipherText The encrypted text (Base64 encoded cipher text and IV).
     * @return The decrypted plain text string.
     * @throws Exception If decryption fails.
     */
    public String decrypt(String cipherText) throws Exception{
        SecretKey secretKey = getSecretKey("vacation_secret_key");

        String[] array = cipherText.split("\\.");
        byte[] cipherData = Base64.decode(array[0], Base64.DEFAULT);
        byte[] iv = Base64.decode(array[1], Base64.DEFAULT);

        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec spec = new GCMParameterSpec(128, iv);

        cipher.init(Cipher.DECRYPT_MODE, secretKey, spec);

        byte[] text = cipher.doFinal(cipherData);
        return new String(text, StandardCharsets.UTF_8);
    }
}