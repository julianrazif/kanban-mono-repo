package com.julian.razif.kanban.common.security;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;

public class Decryption {

  private final EncryptDecryptUtils encryptDecryptUtils;
  private final SecurityProperties securityProperties;

  public Decryption(
    EncryptDecryptUtils encryptDecryptUtils,
    SecurityProperties securityProperties) {

    this.encryptDecryptUtils = encryptDecryptUtils;
    this.securityProperties = securityProperties;
  }

  public String getDecryptedKey() throws GeneralSecurityException {
    String salted = encryptDecryptUtils.bytesToHex(encryptDecryptUtils.generateSalt()).substring(0, 8);
    return encryptDecryptUtils.decodeToString(securityProperties.encryptionPassword(), salted, EncryptDecryptUtils.ITERATION_COUNT);
  }

  public SecretKey generateSecretKey() throws GeneralSecurityException {
    return encryptDecryptUtils.generateSecretKey(getDecryptedKey());
  }

  public String decode(String secret) throws GeneralSecurityException {
    return encryptDecryptUtils.decodeToString(secret, generateSecretKey());
  }

  public String encode(String plain) throws GeneralSecurityException {
    return encryptDecryptUtils.encodeToString(plain.getBytes(StandardCharsets.UTF_8), generateSecretKey());
  }

}
