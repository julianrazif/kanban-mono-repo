package com.julian.razif.kanban.application.board;

import com.julian.razif.kanban.application.board.dto.BoardResponse;
import com.julian.razif.kanban.application.board.dto.BoardRequest;
import com.julian.razif.kanban.common.exception.BadRequestException;
import com.julian.razif.kanban.common.util.ValidationUtils;
import com.julian.razif.kanban.domain.model.Board;
import com.julian.razif.kanban.domain.port.BoardPort;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class BoardService implements CreateBoardUseCase, UpdateBoardUseCase {

  private final BoardPort boardPort;

  public BoardService(BoardPort boardPort) {
    this.boardPort = boardPort;
  }

  /**
   * Creates board; returns response or throws exception
   */
  @Override
  public BoardResponse createBoard(
    @Nonnull BoardRequest request) {

    var name = ValidationUtils.requireNonBlank(request.name(), () -> new BadRequestException("Name required"));

    Board board = Board.withName(name);
    board = boardPort.createBoard(board);

    return new BoardResponse(board.getId(), board.getName());
  }

  /**
   * Updates board if found and the name is available
   */
  @Override
  public BoardResponse updateBoard(
    Long id,
    @Nonnull BoardRequest request) {

    var ID = ValidationUtils.requireNonNull(id, () -> new BadRequestException("Board id required"));
    var name = ValidationUtils.requireNonBlank(request.name(), () -> new BadRequestException("Name required"));

    Board board = boardPort.getBoardById(ID);

    ID = ValidationUtils.requireNonNull(board.getId(), () -> new BadRequestException("Board not found"));

    if (name.equals(board.getName())) {
      return new BoardResponse(ID, board.getName());
    }

    Board boardByName = boardPort.getBoardByName(name);

    // Throws exception if the name conflicts with another board
    if (name.equals(boardByName.getName()) && !ID.equals(boardByName.getId())) {
      throw new BadRequestException("Board with same name already exists");
    }

    board = boardPort.updateBoard(board, name);

    return new BoardResponse(board.getId(), board.getName());
  }

}
