package com.julian.razif.kanban.common.security;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import org.springframework.validation.annotation.Validated;

import java.security.Principal;

/**
 * Represents the principal entity for a user in a security context.
 * This encapsulates authenticated user information, including their unique identifier
 * and email address. Primarily used for describing a user's identity in security-related operations.
 * <p>
 * The {@link UserPrincipal} implements the {@link Principal} interface,
 * providing the `getName` method which returns the user's email address as the principal name.
 * <p>
 * Constraints:
 * - The `id` field must be a positive number.
 * - The `email` field must not be blank.
 *
 * @author Julian Razif Figaro
 * @version 1.0
 * @since version 1.0
 */
@Validated
public record UserPrincipal(@Positive Long id, @NotBlank String email) implements Principal {

  @Override
  public String getName() {
    return email;
  }

}
