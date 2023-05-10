package com.tss.repositories.data;

import com.tss.entities.data.Action;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActionRepository extends JpaRepository<Action, Long> {
}