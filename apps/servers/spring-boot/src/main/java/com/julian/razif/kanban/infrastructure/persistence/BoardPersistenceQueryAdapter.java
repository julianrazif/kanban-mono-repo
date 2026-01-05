package com.julian.razif.kanban.infrastructure.persistence;

import com.julian.razif.kanban.domain.model.Board;
import com.julian.razif.kanban.domain.port.BoardQueryPort;
import com.julian.razif.kanban.infrastructure.persistence.repository.BoardJpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class BoardPersistenceQueryAdapter implements BoardQueryPort {

  private final BoardJpaRepository boardJpaRepository;

  public BoardPersistenceQueryAdapter(
    BoardJpaRepository boardJpaRepository) {

    this.boardJpaRepository = boardJpaRepository;
  }

  @Override
  @Transactional(readOnly = true)
  public List<Board> listBoards() {
    // Maps persisted boards to domain objects
    return boardJpaRepository
      .findAll()
      .stream()
      .map(b -> Board
        .fromBuilder(
          Board.builder()
            .id(b.getId())
            .name(b.getName())
        )
      )
      .toList();
  }

}
