package com.julian.razif.kanban.infrastructure.persistence.repository;

import com.julian.razif.kanban.infrastructure.persistence.model.ColumnEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ColumnJpaRepository extends JpaRepository<ColumnEntity, Long> {
}
