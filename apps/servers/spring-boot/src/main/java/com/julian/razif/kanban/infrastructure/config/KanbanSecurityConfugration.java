package com.julian.razif.kanban.infrastructure.config;

import com.julian.razif.kanban.common.security.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KanbanSecurityConfugration {

  @Bean
  public EncryptDecryptUtils encryptDecryptUtils() {
    return new EncryptDecryptUtils();
  }

  @Bean
  public Decryption decryption(
    EncryptDecryptUtils encryptDecryptUtils,
    SecurityProperties securityProperties) {

    return new Decryption(encryptDecryptUtils, securityProperties);
  }

  @Bean
  public JWTUtil jwtUtil(
    Decryption decryption,
    JwtProperties jwtProperties) {

    return new JWTUtil(decryption, jwtProperties);
  }

}
