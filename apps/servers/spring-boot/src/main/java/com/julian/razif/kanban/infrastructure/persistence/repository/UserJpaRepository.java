package com.julian.razif.kanban.infrastructure.persistence.repository;

import com.julian.razif.kanban.infrastructure.persistence.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {

  Optional<UserEntity> findByEmail(String email);

}
