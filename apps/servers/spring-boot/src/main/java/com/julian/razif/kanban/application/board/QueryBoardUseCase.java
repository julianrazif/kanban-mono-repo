package com.julian.razif.kanban.application.board;

import com.julian.razif.kanban.application.board.dto.BoardDetailResponse;
import com.julian.razif.kanban.application.board.dto.BoardResponse;

import java.util.List;

public interface QueryBoardUseCase {

  List<BoardResponse> listBoards();

  BoardDetailResponse getBoardDetail(Long id);

}
