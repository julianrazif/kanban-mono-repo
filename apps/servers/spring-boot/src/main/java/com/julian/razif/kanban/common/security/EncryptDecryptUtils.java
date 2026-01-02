package com.julian.razif.kanban.common.security;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Base64;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Security;

@Component
class EncryptDecryptUtils {

  public static final String BOUNCY_CASTLE_PROVIDER = "BC";

  public static final String AES = "AES";
  public static final String PASS_MASK_PREFIX = "MASK-";
  public static final String AES_CBC_PKCS5Padding = "AES/CBC/PKCS5Padding";
  public static final int KEY_SIZE = 256;

  public static final String PBKDF2_WITH_HMAC_SHA256 = "PBKDF2WithHmacSHA256";
  public static final String PBE_WITH_MD5_AND_DES = "PBEwithMD5andDES";

  public static final int ITERATION_COUNT = 10000;
  public static final int SALT_LENGTH = 16;

  EncryptDecryptUtils() {
    Security.addProvider(new BouncyCastleProvider());
  }

  public byte[] generateSalt() {
    return new byte[SALT_LENGTH];
  }

  public byte[] generateIv() {
    return new byte[SALT_LENGTH];
  }

  public SecretKey generateSecretKey(String password) throws Exception {
    PBEKeySpec pbeKeySpec = new PBEKeySpec(password.toCharArray(), generateSalt(), ITERATION_COUNT, KEY_SIZE);
    SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(PBKDF2_WITH_HMAC_SHA256);
    return secretKeyFactory.generateSecret(pbeKeySpec);
  }

  /**
   * Encrypts data using a provided secret key
   */
  public byte[] encrypt(byte[] data, SecretKey secretKey) throws Exception {
    SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getEncoded(), AES);
    Cipher cipher = Cipher.getInstance(AES_CBC_PKCS5Padding, BOUNCY_CASTLE_PROVIDER);
    cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, new IvParameterSpec(generateIv()));
    return cipher.doFinal(data);
  }

  public String encodeToString(byte[] data, SecretKey secretKey) throws Exception {
    return Base64.toBase64String(encrypt(data, secretKey));
  }

  /**
   * Decrypts secret using a provided secret key
   */
  public byte[] decrypt(byte[] secret, SecretKey secretKey) throws Exception {
    SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getEncoded(), AES);
    Cipher cipher = Cipher.getInstance(AES_CBC_PKCS5Padding, BOUNCY_CASTLE_PROVIDER);
    cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, new IvParameterSpec(generateIv()));
    return cipher.doFinal(secret);
  }

  public String decodeToString(String secret, SecretKey secretKey) throws Exception {
    return new String(decrypt(Base64.decode(secret), secretKey), StandardCharsets.UTF_8);
  }

  public String decodeToString(String secret, String salt, int iterationCount) throws Exception {
    if (secret == null) {
      return null;
    }
    // Decrypts masked secret using fixed password and provided parameters
    if (secret.startsWith(PASS_MASK_PREFIX)) {
      char[] password = "somearbitrarycrazystringthatdoesnotmatter".toCharArray();
      PBEParameterSpec pbeParameterSpec = new PBEParameterSpec(salt.getBytes(), iterationCount);
      PBEKeySpec pbeKeySpec = new PBEKeySpec(password);
      SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(PBE_WITH_MD5_AND_DES);
      SecretKey secretKey = secretKeyFactory.generateSecret(pbeKeySpec);
      secret = secret.substring(PASS_MASK_PREFIX.length());
      Cipher cipher = Cipher.getInstance(PBE_WITH_MD5_AND_DES);
      cipher.init(Cipher.DECRYPT_MODE, secretKey, pbeParameterSpec);
      byte[] bytes = cipher.doFinal(Base64.decode(secret));
      return new String(bytes, StandardCharsets.UTF_8);
    }
    return secret;
  }

  /**
   * Encodes secret using fixed password and provided salt
   */
  public String encodeToString(String secret, String salt, int iterationCount) throws Exception {
    char[] password = "somearbitrarycrazystringthatdoesnotmatter".toCharArray();
    PBEParameterSpec pbeParameterSpec = new PBEParameterSpec(salt.getBytes(), iterationCount);
    PBEKeySpec pbeKeySpec = new PBEKeySpec(password);
    SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(PBE_WITH_MD5_AND_DES);
    SecretKey secretKey = secretKeyFactory.generateSecret(pbeKeySpec);
    Cipher cipher = Cipher.getInstance(PBE_WITH_MD5_AND_DES);
    cipher.init(Cipher.ENCRYPT_MODE, secretKey, pbeParameterSpec);
    byte[] encryptedBytes = cipher.doFinal(secret.getBytes(StandardCharsets.UTF_8));
    return PASS_MASK_PREFIX + Base64.toBase64String(encryptedBytes);
  }

  public String bytesToHex(byte[] hash) {
    if (hash == null) {
      return null;
    }
    StringBuilder hexString = new StringBuilder();
    // Converts bytes to hexadecimal string representation
    for (byte b : hash) {
      String hex = Integer.toHexString(0xff & b);
      if (hex.length() == 1) {
        hexString.append('0');
      }
      hexString.append(hex);
    }
    return hexString.toString();
  }

}
