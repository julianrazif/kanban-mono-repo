package com.julian.razif.kanban.common.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Data transfer object that encapsulates JWT authentication credentials.
 * This class holds the username and JWT token required for JWT-based authentication.
 *
 * <p>This object is used to transport authentication credentials between different
 * layers of the application during the JWT authentication process. It implements
 * Serializable to support session storage and network transmission.
 *
 * <p>The class uses Lombok annotations for automatic generation of:
 * <ul>
 *   <li>Getter and setter methods (@Data)</li>
 *   <li>All-arguments constructor (@AllArgsConstructor)</li>
 *   <li>No-arguments constructor (@NoArgsConstructor)</li>
 * </ul>
 *
 * @author Julian Razif Figaro
 * @version 1.0
 * @since version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JWTCredentialObject implements Serializable {

  /**
   * The username associated with the JWT token.
   * This field contains the user identifier that should match the subject claim in the JWT token.
   */
  private String username;

  /**
   * The JWT token string containing encoded authentication information.
   * This token includes claims such as subject, expiration time, and other user-related data.
   */
  private String token;

}
