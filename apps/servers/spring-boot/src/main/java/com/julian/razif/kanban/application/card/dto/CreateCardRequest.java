package com.julian.razif.kanban.application.card.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateCardRequest(
  @NotBlank String title,
  String description,
  @NotNull Long columnId,
  @NotNull Long boardId) {
}
