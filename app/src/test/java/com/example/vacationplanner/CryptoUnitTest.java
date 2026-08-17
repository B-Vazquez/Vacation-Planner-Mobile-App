package com.example.vacationplanner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.vacationplanner.utilities.CryptoUtil;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import java.security.KeyStore;

/**
 * Unit tests for {@link CryptoUtil}.
 * Uses Robolectric and a standard JVM KeyStore for testing.
 */
@RunWith(RobolectricTestRunner.class)
public class CryptoUnitTest {

    private CryptoUtil cryptoUtil;
    private SharedPreferences sharedPreferences;

    @Before
    public void setUp() throws Exception {
        sharedPreferences = RuntimeEnvironment.getApplication()
                .getSharedPreferences("test_prefs", Context.MODE_PRIVATE);
        // Use a standard JVM KeyStore provider for unit tests
        cryptoUtil = new CryptoUtil(sharedPreferences, KeyStore.getDefaultType());
    }

    @Test
    public void encrypt_And_Decrypt_ReturnsOriginalText() throws Exception {
        String originalText = "Password123";
        
        String encryptedText = cryptoUtil.encrypt(originalText);
        assertNotEquals("Encrypted text should not be equal to original", originalText, encryptedText);
        
        String decryptedText = cryptoUtil.decrypt(encryptedText);
        assertEquals("Decrypted text should match original", originalText, decryptedText);
    }

    @Test
    public void compare_ReturnsTrueForMatchingText() throws Exception {
        String originalText = "Password123";
        String encryptedText = cryptoUtil.encrypt(originalText);
        
        assertTrue("Compare should return true for correct password", 
                cryptoUtil.compare(encryptedText, originalText));
    }

    @Test
    public void initialization_GeneratesKeysInSharedPreferences() {
        assertTrue("SharedPreferences should contain encrypted_key", 
                sharedPreferences.contains("encrypted_key"));
        assertTrue("SharedPreferences should contain encryption_iv", 
                sharedPreferences.contains("encryption_iv"));
    }
}
