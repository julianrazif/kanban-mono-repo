package com.julian.razif.kanban.infrastructure.config;

import com.julian.razif.kanban.common.security.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class KanbanSecurityConfiguration {

  @Bean
  EncryptDecryptUtils encryptDecryptUtils() {
    return new EncryptDecryptUtils();
  }

  @Bean
  Decryption decryption(
    EncryptDecryptUtils encryptDecryptUtils,
    SecurityProperties securityProperties) {

    return new Decryption(encryptDecryptUtils, securityProperties);
  }

  @Bean
  JWTUtil jwtUtil(
    Decryption decryption,
    JwtProperties jwtProperties) {

    return new JWTUtil(decryption, jwtProperties);
  }

}
