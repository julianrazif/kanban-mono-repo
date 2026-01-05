package com.julian.razif.kanban.infrastructure.web;

import com.julian.razif.kanban.application.board.BoardQueryService;
import com.julian.razif.kanban.application.board.CreateBoardUseCase;
import com.julian.razif.kanban.application.board.UpdateBoardUseCase;
import com.julian.razif.kanban.application.board.dto.BoardDetailResponse;
import com.julian.razif.kanban.application.board.dto.BoardResponse;
import com.julian.razif.kanban.application.board.dto.BoardRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController(value = "api/v1/boards")
class BoardController {

  private final BoardQueryService boardQueryService;
  private final CreateBoardUseCase createBoardUseCase;
  private final UpdateBoardUseCase updateBoardUseCase;

  BoardController(
    BoardQueryService boardQueryService,
    CreateBoardUseCase createBoardUseCase, 
    UpdateBoardUseCase updateBoardUseCase) {

    this.boardQueryService = boardQueryService;
    this.createBoardUseCase = createBoardUseCase;
    this.updateBoardUseCase = updateBoardUseCase;
  }

  @PostMapping("/")
  ResponseEntity<BoardResponse> createBoard(
    @Valid @RequestBody BoardRequest request) {

    return ResponseEntity
      .status(HttpStatus.CREATED)
      .body(createBoardUseCase.createBoard(request));
  }

  @PutMapping("/{id}")
  ResponseEntity<BoardResponse> updateBoard(
    @PathVariable @Min(value = 1, message = "Board ID must be greater than 0") Long id,
    @Valid @RequestBody BoardRequest request) {

    return ResponseEntity
      .status(HttpStatus.OK)
      .body(updateBoardUseCase.updateBoard(id, request));
  }

  @GetMapping("/")
  ResponseEntity<Map<String, List<BoardResponse>>> listBoards() {
    return ResponseEntity
      .status(HttpStatus.OK)
      .body(Map.of("boards", boardQueryService.listBoards()));
  }

  @GetMapping("/{id}")
  ResponseEntity<Map<String, BoardDetailResponse>> getBoard(
    @PathVariable Long id) {

    return ResponseEntity
      .status(HttpStatus.OK)
      .body(Map.of("board", boardQueryService.getBoardDetail(id)));
  }

}
