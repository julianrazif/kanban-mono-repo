package com.julian.razif.kanban.common.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.crypto.SecretKey;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class DecryptionTest {

  private EncryptDecryptUtils encryptDecryptUtils;
  private SecurityProperties securityProperties;
  private Decryption decryption;

  @BeforeEach
  void setUp() {
    encryptDecryptUtils = new EncryptDecryptUtils();
    securityProperties = Mockito.mock(SecurityProperties.class);
    decryption = new Decryption(encryptDecryptUtils, securityProperties);
  }

  /**
   * Tests password decryption using mocked properties
   */
  @Test
  void testGetDecryptedKey() throws Exception {
    String rawPassword = "rawPassword123";
    String salt = encryptDecryptUtils.bytesToHex(encryptDecryptUtils.generateSalt()).substring(0, 8);
    String maskedPassword = encryptDecryptUtils.encodeToString(rawPassword, salt, EncryptDecryptUtils.ITERATION_COUNT);

    when(securityProperties.encryptionPassword()).thenReturn(maskedPassword);

    String decryptedKey = decryption.getDecryptedKey();
    assertEquals(rawPassword, decryptedKey);
  }

  /**
   * Tests secret key generation from masked password
   */
  @Test
  void testGenerateSecretKey() throws Exception {
    String rawPassword = "rawPassword123";
    String salt = encryptDecryptUtils.bytesToHex(encryptDecryptUtils.generateSalt()).substring(0, 8);
    String maskedPassword = encryptDecryptUtils.encodeToString(rawPassword, salt, EncryptDecryptUtils.ITERATION_COUNT);

    when(securityProperties.encryptionPassword()).thenReturn(maskedPassword);

    SecretKey secretKey = decryption.generateSecretKey();
    assertNotNull(secretKey);
  }

  /**
   * Tests round-trip encoding and decoding of text
   */
  @Test
  void testEncodeDecode() throws Exception {
    String rawPassword = "rawPassword123";
    String salt = encryptDecryptUtils.bytesToHex(encryptDecryptUtils.generateSalt()).substring(0, 8);
    String maskedPassword = encryptDecryptUtils.encodeToString(rawPassword, salt, EncryptDecryptUtils.ITERATION_COUNT);

    when(securityProperties.encryptionPassword()).thenReturn(maskedPassword);

    String originalText = "Sensitive Information";
    String encoded = decryption.encode(originalText);
    assertNotNull(encoded);
    assertNotEquals(originalText, encoded);

    String decoded = decryption.decode(encoded);
    assertEquals(originalText, decoded);
  }

}
