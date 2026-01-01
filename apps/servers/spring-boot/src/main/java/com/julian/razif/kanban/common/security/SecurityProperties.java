package com.julian.razif.kanban.common.security;

import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "kanban.security")
record SecurityProperties(
  @NotBlank
  String encryptionPassword
) {
}
