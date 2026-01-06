package com.julian.razif.kanban.common.security;

import org.springframework.security.core.GrantedAuthority;

import java.io.Serial;
import java.util.Collection;

/**
 * Custom authentication token for JWT-based authentication.
 * This token handles authentication using JWT (JSON Web Token) credentials
 * and extends the base authentication token to eliminate code duplication.
 *
 * <p>This class provides type-safe handling of JWTCredentialObject instances
 * while inheriting common authentication token behavior from BaseAuthenticationToken.
 *
 * @author Julian Razif Figaro
 * @version 1.0
 * @since version 1.0
 */
public class JWTAuthenticationToken extends BaseAuthenticationToken<JWTCredentialObject> {

  @Serial
  private static final long serialVersionUID = 8924736501847362195L;

  /**
   * Creates an unauthenticated token with JWT credentials.
   * This constructor is used when creating authentication requests.
   *
   * @param credentials The JWT credentials to authenticate
   */
  public JWTAuthenticationToken(JWTCredentialObject credentials) {
    super(credentials);
  }

  /**
   * Creates an authenticated token with user principal and authorities.
   * This constructor is used after successful authentication.
   *
   * @param principal   The authenticated user principal
   * @param credentials The original JWT credentials used for authentication
   * @param authorities The granted authorities for the authenticated user
   */
  public JWTAuthenticationToken(
    Object principal,
    Object credentials,
    Collection<? extends GrantedAuthority> authorities) {

    super(principal, credentials, authorities);
  }

}
