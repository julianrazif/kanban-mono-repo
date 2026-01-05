package com.julian.razif.kanban.infrastructure.persistence.repository;

import com.julian.razif.kanban.infrastructure.persistence.model.BoardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BoardJpaRepository extends JpaRepository<BoardEntity, Long> {

  @Query("select distinct b from BoardEntity b left join fetch b.columns c left join fetch c.cards where b.id = :id")
  Optional<BoardEntity> findByIdWithColumnsAndCards(Long id);

  Optional<BoardEntity> findByName(String name);

}
