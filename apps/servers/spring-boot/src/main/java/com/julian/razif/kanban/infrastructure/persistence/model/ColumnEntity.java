package com.julian.razif.kanban.infrastructure.persistence.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "columns")
public class ColumnEntity {

  @Setter
  @Getter
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Setter
  @Getter
  @Column(name = "column_name", nullable = false)
  private String name;

  @Setter
  @Getter
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "board_id", nullable = false)
  private BoardEntity board;

  @Setter
  @Getter
  @OneToMany(mappedBy = "column", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<CardEntity> cards = new ArrayList<>();

  @Setter
  @Getter
  @Column(name = "created_date")
  private LocalDateTime createdDate;

  @Setter
  @Getter
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
