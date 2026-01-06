package com.julian.razif.kanban.infrastructure.config;

import com.julian.razif.kanban.common.security.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.StaticHeadersWriter;
import org.springframework.security.web.header.writers.XXssProtectionHeaderWriter;

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

  @Bean
  PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  JwtAuthenticationFilter jwtAuthenticationFilter(JWTUtil jwtUtil) {
    return new JwtAuthenticationFilter(jwtUtil);
  }

  @Bean
  AuthenticationEntryPoint authenticationEntryPoint() {
    return (_, response, _) ->
      response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
  }

  @Bean
  SecurityFilterChain securityFilterChain(
    HttpSecurity http,
    JwtAuthenticationFilter jwtAuthenticationFilter,
    AuthenticationEntryPoint authenticationEntryPoint) {

    http
      .csrf(AbstractHttpConfigurer::disable)
      .formLogin(AbstractHttpConfigurer::disable)
      .logout(AbstractHttpConfigurer::disable)
      .httpBasic(AbstractHttpConfigurer::disable)
      .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
      .authorizeHttpRequests(auth -> auth
        .requestMatchers(HttpMethod.POST, "/register", "/login").permitAll()
        .anyRequest().authenticated()

      )
      .exceptionHandling(ex -> ex.authenticationEntryPoint(authenticationEntryPoint))
      .addFilterAfter(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

    http
      .headers(headers -> headers
        .addHeaderWriter(new StaticHeadersWriter("X-XSS-Protection", "1; mode=block"))
        .addHeaderWriter(new StaticHeadersWriter("X-Content-Security-Policy", "default-src 'self'"))
        .xssProtection(xss -> xss
          .headerValue(XXssProtectionHeaderWriter.HeaderValue.ENABLED_MODE_BLOCK)
        )
        .contentSecurityPolicy(csp -> csp.policyDirectives("form-action 'self'")));

    return http.build();
  }

}
