package com.tss.repositories.data;

import com.tss.entities.data.Board;
import com.tss.entities.data.TaskList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskListRepository extends JpaRepository<TaskList, Long> {

    List<TaskList> findAllByBoard(Board board);
    List<TaskList> findAllByListOrderGreaterThanAndListOrderLessThanEqualAndBoard(Integer order1, Integer order2, Board board);
    List<TaskList> findAllByListOrderLessThanAndListOrderGreaterThanEqualAndBoard(Integer order1, Integer order2, Board board);

    List<TaskList> findAllByListOrderGreaterThanAndBoard(Integer order, Board board);
}