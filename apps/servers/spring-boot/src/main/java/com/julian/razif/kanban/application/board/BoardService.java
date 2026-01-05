package com.julian.razif.kanban.application.board;

import com.julian.razif.kanban.application.board.dto.BoardResponse;
import com.julian.razif.kanban.application.board.dto.BoardRequest;
import com.julian.razif.kanban.common.exception.BadRequestException;
import com.julian.razif.kanban.domain.model.Board;
import com.julian.razif.kanban.domain.port.BoardPort;
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
  public BoardResponse createBoard(BoardRequest request) {
    if (request.name() == null || request.name().isBlank()) {
      throw new BadRequestException("Name required");
    }

    Board board = Board.withName(request.name());
    board = boardPort.createBoard(board);

    return new BoardResponse(board.getId(), board.getName());
  }

  /**
   * Updates board if found and the name is available
   */
  @Override
  public BoardResponse updateBoard(Long id, BoardRequest request) {
    if (id == null) {
      throw new BadRequestException("Board id required");
    }

    if (request.name() == null || request.name().isBlank()) {
      throw new BadRequestException("Name required");
    }

    Board board = boardPort.getBoardById(id);

    if (board.getId() == null) {
      throw new BadRequestException("Board not found");
    }

    if (request.name().equals(board.getName())) {
      return new BoardResponse(board.getId(), board.getName());
    }

    Board boardByName = boardPort.getBoardByName(request.name());

    // Throws exception if the name conflicts with another board
    if (request.name().equals(boardByName.getName()) && !board.getId().equals(boardByName.getId())) {
      throw new BadRequestException("Board with same name already exists");
    }

    board = boardPort.updateBoard(board, request.name());

    return new BoardResponse(board.getId(), board.getName());
  }

}
