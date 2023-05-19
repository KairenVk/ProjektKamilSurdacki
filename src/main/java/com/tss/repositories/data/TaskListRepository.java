package com.tss.repositories.data;

import com.tss.entities.data.Board;
import com.tss.entities.data.TaskList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskListRepository extends JpaRepository<TaskList, Long> {

    List<TaskList> findAllByBoard(Board board);
}