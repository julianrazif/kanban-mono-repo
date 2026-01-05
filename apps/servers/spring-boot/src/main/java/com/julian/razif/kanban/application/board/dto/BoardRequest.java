package com.julian.razif.kanban.application.board.dto;

import jakarta.validation.constraints.NotBlank;

public record BoardRequest(@NotBlank String name) {
}
