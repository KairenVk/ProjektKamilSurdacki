package com.tss.repositories.data;

import com.tss.entities.data.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {
}