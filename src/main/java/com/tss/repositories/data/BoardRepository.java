package com.tss.repositories.data;

import com.tss.entities.data.Board;
import com.tss.entities.data.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {

    List<Board> findAllByOwner(User owner);
}