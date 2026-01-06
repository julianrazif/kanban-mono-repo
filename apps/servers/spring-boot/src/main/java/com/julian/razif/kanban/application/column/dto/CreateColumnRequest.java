package com.julian.razif.kanban.application.column.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateColumnRequest(
  @NotBlank String name,
  @NotNull Long boardId) {
}
