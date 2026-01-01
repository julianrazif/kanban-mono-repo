package com.julian.razif.kanban.common.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.Nonnull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

/**
 * Utility class for JWT (JSON Web Token) operations including token generation,
 * validation, and claims extraction. This class provides methods to create,
 * parse, and validate JWT tokens using HMAC SHA algorithm.
 *
 * @author Julian Razif Figaro
 * @version 1.0
 * @since version 1.0
 */
@Component
class JWTUtil {

  private static final Logger log = LoggerFactory.getLogger(JWTUtil.class);

  /**
   * Cryptographic key used for signing and verifying JWT tokens.
   * Generated from the secret string during initialization.
   */
  private final SecretKey key;

  private final JwtProperties jwtProperties;

  JWTUtil(JwtProperties jwtProperties) {
    this.jwtProperties = jwtProperties;
    try {
      this.key = Keys.hmacShaKeyFor(jwtProperties.secret().getBytes());
    } catch (Exception e) {
      throw new RuntimeException("Failed to initialize JWT key", e);
    }
  }

  /**
   * Extracts all claims from the provided JWT token.
   * This method parses the JWT token and returns all the claims contained within it.
   *
   * @param token The JWT token string to parse
   * @return Claims object containing all token claims
   * @throws BadCredentialsException if the token is invalid, malformed, or signature verification fails
   */
  public Claims getAllClaimsFromToken(
    @Nonnull String token) {

    try {
      return Jwts.parser()
        .verifyWith(key)
        .build()
        .parseSignedClaims(token)
        .getPayload();
    } catch (Exception e) {
      log.error("Invalid JWT token: {}", e.getMessage());
      throw new BadCredentialsException("Invalid JWT token", e);
    }
  }

  /**
   * Extracts the username (subject) from the JWT token.
   * This is a convenience method that retrieves the subject claim from the token.
   *
   * @param token The JWT token string to extract username from
   * @return The username stored in the token's subject claim
   * @throws BadCredentialsException if the token is invalid or cannot be parsed
   */
  public String extractUsername(
    @Nonnull String token) {

    return getAllClaimsFromToken(token).getSubject();
  }

  /**
   * Internal method that performs the actual JWT token generation.
   * This method builds the JWT with the provided claims, subject, and expiration time.
   *
   * @param claims   Custom claims to include it in the token
   * @param username The username to set as the token subject
   * @return A signed JWT token string with all specified claims and metadata
   */
  private String generateToken(
    @Nonnull Map<String, Object> claims,
    @Nonnull String username) {

    return Jwts.builder()
      .claims(claims)
      .subject(username)
      .issuedAt(new Date())
      .expiration(new Date((new Date()).getTime() + jwtProperties.expirationInMs()))
      .signWith(key)
      .compact();
  }

  /**
   * Generates a JWT token without an expiration date.
   * This method creates a token that will not expire and remain valid indefinitely.
   *
   * @param claims   Custom claims to include it in the token
   * @param username The username to set as the token subject
   * @return A signed JWT token string with no expiration date
   */
  private String generateNonExpiredToken(
    @Nonnull Map<String, Object> claims,
    @Nonnull String username) {

    return Jwts.builder()
      .claims(claims)
      .subject(username)
      .issuedAt(new Date())
      .signWith(key)
      .compact();
  }

}
