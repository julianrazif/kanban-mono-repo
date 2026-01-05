package com.julian.razif.kanban.domain.port;

import com.julian.razif.kanban.domain.model.Board;

public interface BoardPort {

  Board createBoard(Board board);

  Board updateBoard(Board board, String name);

  Board getBoardById(Long id);

  Board getBoardByName(String name);

}
