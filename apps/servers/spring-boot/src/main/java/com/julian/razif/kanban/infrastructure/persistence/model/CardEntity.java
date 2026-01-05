package com.julian.razif.kanban.infrastructure.persistence.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "cards")
public class CardEntity {

  @Setter
  @Getter
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Setter
  @Getter
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "column_id", nullable = false)
  private ColumnEntity column;

  @Setter
  @Getter
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private UserEntity user;

  @Setter
  @Getter
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "board_id", nullable = false)
  private BoardEntity board;

  @Setter
  @Getter
  private String title;

  @Setter
  @Getter
  private String description;

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
