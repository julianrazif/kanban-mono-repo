package com.julian.razif.kanban.application.board.dto;

import com.julian.razif.kanban.application.column.dto.ColumnWithCardsResponse;

import java.util.List;

public record BoardDetailResponse(Long id, String name, List<ColumnWithCardsResponse> columns) {
}
