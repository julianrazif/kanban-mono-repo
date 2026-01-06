package com.julian.razif.kanban.application.card.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateCardRequest(
  @NotBlank String title,
  @NotNull Long columnId,
  String description) {
}
