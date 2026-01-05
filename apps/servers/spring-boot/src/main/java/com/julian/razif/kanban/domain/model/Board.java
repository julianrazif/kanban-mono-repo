package com.julian.razif.kanban.domain.model;

public class Board {

  private final Long id;
  private final String name;

  private Board(Long id, String name) {
    this.id = id;
    this.name = name;
  }

  public static Board withName(String name) {
    Board.Builder builder = builder().id(null).name(name);
    return fromBuilder(builder);
  }

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public static Board.Builder builder() {
    return new Board.Builder();
  }

  public static Board.Builder toBuilder(Board board) {
    return builder().id(board.getId()).name(board.getName());
  }

  public static Board fromBuilder(Board.Builder builder) {
    return builder.build();
  }

  public static class Builder {
    private Long id;
    private String name;

    public Builder id(Long id) {
      this.id = id;
      return this;
    }

    public Builder name(String name) {
      this.name = name;
      return this;
    }

    public Board build() {
      return new Board(id, name);
    }
  }

}
