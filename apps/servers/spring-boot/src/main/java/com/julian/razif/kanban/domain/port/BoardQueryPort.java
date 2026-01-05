package com.julian.razif.kanban.domain.port;

import com.julian.razif.kanban.domain.model.Board;

import java.util.List;

public interface BoardQueryPort {

  List<Board> listBoards();

}
