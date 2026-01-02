package com.julian.razif.kanban.common.security;

import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

@Component
class Decryption {

  private final EncryptDecryptUtils encryptDecryptUtils;
  private final SecurityProperties securityProperties;

  Decryption(
    EncryptDecryptUtils encryptDecryptUtils,
    SecurityProperties securityProperties) {

    this.encryptDecryptUtils = encryptDecryptUtils;
    this.securityProperties = securityProperties;
  }

  public String getDecryptedKey() throws Exception {
    String salted = encryptDecryptUtils.bytesToHex(encryptDecryptUtils.generateSalt()).substring(0, 8);
    return encryptDecryptUtils.decodeToString(securityProperties.encryptionPassword(), salted, EncryptDecryptUtils.ITERATION_COUNT);
  }

  public SecretKey generateSecretKey() throws Exception {
    return encryptDecryptUtils.generateSecretKey(getDecryptedKey());
  }

  public String decode(String secret) throws Exception {
    return encryptDecryptUtils.decodeToString(secret, generateSecretKey());
  }

  public String encode(String plain) throws Exception {
    return encryptDecryptUtils.encodeToString(plain.getBytes(StandardCharsets.UTF_8), generateSecretKey());
  }

}
