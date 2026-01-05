package com.julian.razif.kanban.infrastructure.persistence.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class UserEntity {

  @Setter
  @Getter
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Setter
  @Getter
  @Column(nullable = false, unique = true)
  private String email;

  @Setter
  @Getter
  @Column(nullable = false)
  private String password;

  @Setter
  @Getter
  @Column(nullable = false)
  private String organization = "Hacktiv8";

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
