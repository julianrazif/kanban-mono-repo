package com.julian.razif.kanban.application.column.dto;

import com.julian.razif.kanban.application.card.dto.CardResponse;

import java.util.List;

public record ColumnWithCardsResponse(Long id, String name, Long boardId, List<CardResponse> cards) {
}
