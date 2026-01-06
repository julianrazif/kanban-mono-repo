package com.julian.razif.kanban.application.column.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdateColumnRequest(@NotBlank String name) {
}
