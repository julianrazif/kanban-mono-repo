package com.julian.razif.kanban.infrastructure.persistence.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "boards")
public class BoardEntity {

  @Setter
  @Getter
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Setter
  @Getter
  @Column(name = "board_name", nullable = false, unique = true)
  private String name;

  @Setter
  @Getter
  @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<ColumnEntity> columns = new ArrayList<>();

  @Column(name = "created_date")
  private LocalDateTime createdDate;

  @Column(name = "modified_date")
  private LocalDateTime modifiedDate;

  @PrePersist
  void onCreate() {
    LocalDateTime now = LocalDateTime.now();
    this.createdDate = now;
    this.modifiedDate = now;
  }

  @PreUpdate
  void onUpdate() {
    this.modifiedDate = LocalDateTime.now();
  }

}
