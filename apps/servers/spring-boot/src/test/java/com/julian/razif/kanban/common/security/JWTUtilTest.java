package com.julian.razif.kanban.common.security;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;

import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JWTUtilTest {

  @Mock
  private Decryption decryption;

  @Mock
  private JwtProperties jwtProperties;

  private JWTUtil jwtUtil;

  @BeforeEach
  void setUp() throws Exception {
    when(jwtProperties.secret()).thenReturn("encryptedSecret");
    String secret = "myVerySecretKeyThatIsAtLeast32BytesLong";
    when(decryption.decode("encryptedSecret")).thenReturn(secret);

    jwtUtil = new JWTUtil(decryption, jwtProperties);
  }

  @Test
  void shouldThrowExceptionWhenInitializationFails() throws Exception {
    when(jwtProperties.secret()).thenReturn("errorSecret");
    when(decryption.decode("errorSecret")).thenThrow(new GeneralSecurityException("Decryption failed"));

    assertThatThrownBy(() -> new JWTUtil(decryption, jwtProperties))
      .isInstanceOf(IllegalStateException.class)
      .hasMessage("Failed to initialize JWT key");
  }

  @Test
  void shouldGenerateAndExtractToken() {
    when(jwtProperties.expirationInMs()).thenReturn(3600000L); // 1 hour
    String username = "testuser";
    Map<String, Object> claims = new HashMap<>();
    claims.put("role", "ADMIN");

    // These methods are private in the original class, so this test will fail to compile initially
    // if I don't change their visibility.
    String token = jwtUtil.generateToken(claims, username);

    assertThat(token).isNotBlank();
    assertThat(jwtUtil.extractUsername(token)).isEqualTo(username);

    Claims extractedClaims = jwtUtil.getAllClaimsFromToken(token);
    assertThat(extractedClaims.getSubject()).isEqualTo(username);
    assertThat(extractedClaims.get("role")).isEqualTo("ADMIN");
  }

  @Test
  void shouldThrowExceptionForInvalidToken() {
    assertThatThrownBy(() -> jwtUtil.getAllClaimsFromToken("invalidToken"))
      .isInstanceOf(BadCredentialsException.class)
      .hasMessage("Invalid JWT token");
  }

  @Test
  void shouldThrowExceptionForExpiredToken() {
    when(jwtProperties.expirationInMs()).thenReturn(-1000L); // expired 1 second ago
    String token = jwtUtil.generateToken(new HashMap<>(), "testuser");

    assertThatThrownBy(() -> jwtUtil.getAllClaimsFromToken(token))
      .isInstanceOf(BadCredentialsException.class)
      .hasMessage("Invalid JWT token");
  }

  @Test
  void shouldGenerateNonExpiredToken() {
    String username = "testuser";
    Map<String, Object> claims = new HashMap<>();

    String token = jwtUtil.generateNonExpiredToken(claims, username);

    assertThat(token).isNotBlank();
    assertThat(jwtUtil.extractUsername(token)).isEqualTo(username);

    Claims extractedClaims = jwtUtil.getAllClaimsFromToken(token);
    assertThat(extractedClaims.getExpiration()).isNull();
  }
}
