package com.julian.razif.kanban.common.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.Assert;

import java.util.Collection;

/**
 * Base authentication token that provides common functionality for custom authentication tokens.
 * This abstract class eliminates code duplication between different authentication token implementations
 * by providing shared behavior for credential management and authentication state.
 *
 * <p>This class follows the Template Method pattern, allowing subclasses to specify their
 * credential types while inheriting common authentication token behavior.
 *
 * @param <T> The type of credentials this token handles
 * @author Julian Razif Figaro
 * @version 1.0
 * @since version 1.0
 */
public abstract class BaseAuthenticationToken<T> extends AbstractAuthenticationToken {

  /**
   * The principal (user) associated with this authentication token.
   * This is null for unauthenticated tokens and contains user information for authenticated tokens.
   */
  private final Object principal;

  /**
   * The credentials associated with this authentication token.
   * This contains the authentication credentials specific to the token type.
   */
  private Object credentials;

  /**
   * Constructor for creating a base authentication token with specified principal, credentials, authorities, and authentication state.
   * This private constructor is intended to be used internally by other constructors to initialize the token.
   *
   * @param principal     The authenticated user or system entity. Can be null for unauthenticated tokens.
   * @param credentials   The credentials that prove the identity of the principal. Can be null in certain cases.
   * @param authorities   The collection of granted authorities assigned to the principal.
   * @param authenticated The flag indicating whether the token is authenticated.
   */
  private BaseAuthenticationToken(
    Object principal,
    Object credentials,
    Collection<? extends GrantedAuthority> authorities,
    boolean authenticated) {

    super(authorities);
    this.principal = principal;
    this.credentials = credentials;
    super.setAuthenticated(authenticated);
  }

  /**
   * Creates an unauthenticated token with the specified credentials.
   * This constructor is used when creating tokens for authentication requests.
   *
   * @param credentials The credentials to authenticate
   */
  protected BaseAuthenticationToken(T credentials) {
    this(null, credentials, null, false);
    setAuthenticated(false);
  }

  /**
   * Creates an authenticated token with user principal and authorities.
   * This constructor is used when creating tokens after successful authentication.
   *
   * @param principal   The authenticated user principal
   * @param credentials The original credentials used for authentication
   * @param authorities The granted authorities for the authenticated user
   */
  protected BaseAuthenticationToken(
    Object principal,
    Object credentials,
    Collection<? extends GrantedAuthority> authorities) {

    this(principal, credentials, authorities, true);
  }

  @Override
  public Object getCredentials() {
    return this.credentials;
  }

  @Override
  public Object getPrincipal() {
    return this.principal;
  }

  @Override
  public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
    Assert.isTrue(
      !isAuthenticated,
      "Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead"
    );
    super.setAuthenticated(false);
  }

  @Override
  public void eraseCredentials() {
    super.eraseCredentials();
    this.credentials = null;
  }

}
