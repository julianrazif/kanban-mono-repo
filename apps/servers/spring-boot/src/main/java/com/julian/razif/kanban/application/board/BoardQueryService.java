package com.julian.razif.kanban.application.board;

import com.julian.razif.kanban.application.board.dto.BoardDetailResponse;
import com.julian.razif.kanban.application.board.dto.BoardResponse;
import com.julian.razif.kanban.application.card.dto.CardResponse;
import com.julian.razif.kanban.application.column.dto.ColumnWithCardsResponse;
import com.julian.razif.kanban.common.exception.NotFoundException;
import com.julian.razif.kanban.domain.port.BoardQueryPort;
import com.julian.razif.kanban.infrastructure.persistence.model.BoardEntity;
import com.julian.razif.kanban.infrastructure.persistence.model.CardEntity;
import com.julian.razif.kanban.infrastructure.persistence.model.ColumnEntity;
import com.julian.razif.kanban.infrastructure.persistence.repository.BoardJpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class BoardQueryService implements QueryBoardUseCase {

  private final BoardJpaRepository boardJpaRepository;
  private final BoardQueryPort boardQueryPort;

  public BoardQueryService(
    BoardJpaRepository boardJpaRepository,
    BoardQueryPort boardQueryPort) {

    this.boardJpaRepository = boardJpaRepository;
    this.boardQueryPort = boardQueryPort;
  }

  /**
   * Retrieves all boards as a list of board responses
   */
  @Transactional(readOnly = true)
  public List<BoardResponse> listBoards() {
    return boardQueryPort
      .listBoards()
      .stream()
      .map(b -> new BoardResponse(b.getId(), b.getName()))
      .toList();
  }

  /**
   * Retrieves board detail with columns and cards
   */
  @Transactional(readOnly = true)
  public BoardDetailResponse getBoardDetail(
    Long id) {

    BoardEntity board = boardJpaRepository
      .findByIdWithColumnsAndCards(id)
      .orElseThrow(() -> new NotFoundException("Board not found"));

    List<ColumnWithCardsResponse> columns = board
      .getColumns()
      .stream()
      .map(this::toColumnWithCards)
      .toList();

    return new BoardDetailResponse(board.getId(), board.getName(), columns);
  }

  /**
   * Maps column entity to column with cards response
   */
  private ColumnWithCardsResponse toColumnWithCards(
    ColumnEntity column) {

    List<CardResponse> cards = column
      .getCards()
      .stream()
      .map(this::toCardResponse)
      .toList();

    return new ColumnWithCardsResponse(column.getId(), column.getName(), column.getBoard().getId(), cards);
  }

  /**
   * Maps card entity to card response
   */
  private CardResponse toCardResponse(
    CardEntity card) {

    Long userId = card.getUser() != null ? card.getUser().getId() : null;

    // Maps entity fields to card response
    return new CardResponse(
      card.getId(),
      card.getTitle(),
      card.getColumn().getId(),
      card.getDescription(),
      userId,
      card.getBoard().getId(),
      card.getCreatedDate(),
      card.getModifiedDate());
  }

}
