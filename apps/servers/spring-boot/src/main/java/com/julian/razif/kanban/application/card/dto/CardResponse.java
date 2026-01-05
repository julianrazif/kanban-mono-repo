package com.julian.razif.kanban.application.card.dto;

import java.time.LocalDateTime;

public record CardResponse(
  Long id,
  String title,
  Long columnId,
  String description,
  Long userId,
  Long boardId,
  LocalDateTime createdAt,
  LocalDateTime updatedAt) {
}
