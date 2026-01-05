package com.julian.razif.kanban.application.board;

import com.julian.razif.kanban.application.board.dto.BoardResponse;
import com.julian.razif.kanban.application.board.dto.BoardRequest;

public interface UpdateBoardUseCase {

  BoardResponse updateBoard(Long id, BoardRequest request);

}
