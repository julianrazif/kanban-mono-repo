package com.julian.razif.kanban.application.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RegisterRequest(
  @NotBlank @Email String email,
  @NotBlank String password,
  String organization
) {
}
