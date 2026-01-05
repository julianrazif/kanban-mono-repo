package com.julian.razif.kanban.infrastructure.persistence;

import com.julian.razif.kanban.domain.model.Board;
import com.julian.razif.kanban.domain.port.BoardPort;
import com.julian.razif.kanban.infrastructure.persistence.model.BoardEntity;
import com.julian.razif.kanban.infrastructure.persistence.repository.BoardJpaRepository;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class BoardPersistenceAdapter implements BoardPort {

  private final BoardJpaRepository boardJpaRepository;

  public BoardPersistenceAdapter(
    BoardJpaRepository boardJpaRepository) {

    this.boardJpaRepository = boardJpaRepository;
  }

  /**
   * Persists board; returns board with assigned identifier
   */
  @Override
  public Board createBoard(
    @Nonnull Board board) {

    BoardEntity entity = new BoardEntity();
    entity.setName(board.getName());
    BoardEntity saved = boardJpaRepository.save(entity);
    return Board
      .fromBuilder(
        Board
          .toBuilder(board)
          .id(saved.getId())
      );
  }

  /**
   * Updates board name and returns the updated board
   */
  @Override
  public Board updateBoard(
    @Nonnull Board board,
    @Nonnull String name) {

    BoardEntity entity = new BoardEntity();
    entity.setId(board.getId());
    entity.setName(name);
    BoardEntity saved = boardJpaRepository.save(entity);
    return Board
      .fromBuilder(
        Board
          .toBuilder(board)
          .name(saved.getName())
      );
  }

  /**
   * Retrieves board by ID; returns board or empty board
   */
  @Override
  @Transactional(readOnly = true)
  public Board getBoardById(
    @Nonnull Long id) {

    BoardEntity boardEntity = boardJpaRepository
      .findById(id)
      .orElseGet(BoardEntity::new);

    return Board
      .fromBuilder(
        Board
          .builder()
          .id(boardEntity.getId())
          .name(boardEntity.getName())
      );
  }

  /**
   * Retrieves board by name; returns a populated result
   */
  @Override
  @Transactional(readOnly = true)
  public Board getBoardByName(
    @Nonnull String name) {

    BoardEntity boardEntity = boardJpaRepository
      .findByName(name)
      .orElseGet(BoardEntity::new);

    return Board
      .fromBuilder(
        Board
          .builder()
          .id(boardEntity.getId())
          .name(boardEntity.getName())
      );
  }

}
