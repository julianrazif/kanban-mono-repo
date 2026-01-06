package com.julian.razif.kanban.infrastructure.persistence.repository;

import com.julian.razif.kanban.infrastructure.persistence.model.CardEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardJpaRepository extends JpaRepository<CardEntity, Long> {
}
