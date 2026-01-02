package com.julian.razif.kanban.common.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class EncryptDecryptUtilsTest {

  private EncryptDecryptUtils utils;

  @BeforeEach
  void setUp() {
    utils = new EncryptDecryptUtils();
  }

  @Test
  void testGenerateSalt() {
    byte[] salt = utils.generateSalt();
    assertNotNull(salt);
    assertEquals(EncryptDecryptUtils.SALT_LENGTH, salt.length);
  }

  @Test
  void testGenerateIv() {
    byte[] iv = utils.generateIv();
    assertNotNull(iv);
    assertEquals(EncryptDecryptUtils.SALT_LENGTH, iv.length);
  }

  @Test
  void testGenerateSecretKey() throws Exception {
    String password = "testPassword";
    SecretKey secretKey = utils.generateSecretKey(password);
    assertNotNull(secretKey);
  }

  /**
   * Tests encryption and decryption round trip
   */
  @Test
  void testEncryptDecrypt() throws Exception {
    String password = "testPassword";
    SecretKey secretKey = utils.generateSecretKey(password);
    byte[] originalData = "Hello, World!".getBytes(StandardCharsets.UTF_8);

    byte[] encryptedData = utils.encrypt(originalData, secretKey);
    assertNotNull(encryptedData);
    assertNotEquals(originalData, encryptedData);

    byte[] decryptedData = utils.decrypt(encryptedData, secretKey);
    assertArrayEquals(originalData, decryptedData);
  }

  @Test
  void testEncodeDecodeToString() throws Exception {
    String password = "testPassword";
    SecretKey secretKey = utils.generateSecretKey(password);
    String originalText = "Sensitive Data";

    String encoded = utils.encodeToString(originalText.getBytes(StandardCharsets.UTF_8), secretKey);
    assertNotNull(encoded);

    String decoded = utils.decodeToString(encoded, secretKey);
    assertEquals(originalText, decoded);
  }

  @Test
  void testMaskedEncodeDecodeToString() throws Exception {
    String originalText = "Password123";
    String salt = "testSalt";
    int iterations = 1000;

    String encoded = utils.encodeToString(originalText, salt, iterations);
    assertTrue(encoded.startsWith(EncryptDecryptUtils.PASS_MASK_PREFIX));

    String decoded = utils.decodeToString(encoded, salt, iterations);
    assertEquals(originalText, decoded);
  }

  @Test
  void testDecodeToStringNotMasked() throws Exception {
    String originalText = "NotMasked";
    String salt = "testSalt";
    int iterations = 1000;

    String result = utils.decodeToString(originalText, salt, iterations);
    assertEquals(originalText, result);
  }

  @Test
  void testEncryptDecryptWithWrongKey() throws Exception {
    String password = "testPassword";
    SecretKey secretKey = utils.generateSecretKey(password);
    SecretKey wrongKey = utils.generateSecretKey("wrongPassword");
    byte[] originalData = "Hello, World!".getBytes(StandardCharsets.UTF_8);

    byte[] encryptedData = utils.encrypt(originalData, secretKey);

    // Decrypting with a wrong key should fail or return garbage
    assertThrows(Exception.class, () -> utils.decrypt(encryptedData, wrongKey));
  }

  @Test
  void testDecodeWithCorruptedData() throws Exception {
    String password = "testPassword";
    SecretKey secretKey = utils.generateSecretKey(password);

    assertThrows(Exception.class, () -> utils.decodeToString("CorruptedData", secretKey));
  }

  @Test
  void testMaskedDecodeWithWrongSalt() throws Exception {
    String originalText = "Password123";
    String salt = "testSalt";
    int iterations = 1000;

    String encoded = utils.encodeToString(originalText, salt, iterations);

    assertThrows(Exception.class, () -> utils.decodeToString(encoded, "wrongSalt", iterations));
  }

  @Test
  void testDecodeToStringWithNull() throws Exception {
    assertNull(utils.decodeToString(null, "salt", 1000));
  }

  @Test
  void testBytesToHex() {
    byte[] bytes = {0x00, 0x01, 0x0f, 0x10, (byte) 0xff};
    String hex = utils.bytesToHex(bytes);
    assertEquals("00010f10ff", hex);
  }

  @Test
  void testBytesToHexWithNull() {
    assertNull(utils.bytesToHex(null));
  }

}
