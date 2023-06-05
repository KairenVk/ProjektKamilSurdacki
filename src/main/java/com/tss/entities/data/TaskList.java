package com.tss.entities.data;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.tss.repositories.data.TaskListRepository;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Table(name = "tasklist")
public class TaskList {

    @Autowired
    @Transient
    private TaskListRepository taskListRepository;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "time_created", nullable = false)
    private Timestamp timeCreated;

    @Column(name = "time_modified")
    private Timestamp timeModified;

    @ManyToOne(cascade = CascadeType.MERGE, optional = false)
    @JoinColumn(name = "board_id", nullable = false)
    @JsonBackReference(value="taskLists")
    private Board board;

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "taskList", cascade = CascadeType.MERGE, orphanRemoval = true)
    @JsonManagedReference
    private Collection<Task> tasks = new ArrayList<>();

    @Column(name = "list_order", nullable = false)
    private Integer listOrder;

    public Collection<Task> getTasks() {
        return tasks;
    }

    public void setTasks(Collection<Task> tasks) {
        this.tasks = tasks;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public Timestamp getTimeModified() {
        return timeModified;
    }

    public void setTimeModified(Timestamp timeModified) {
        this.timeModified = timeModified;
    }

    public Timestamp getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(Timestamp timeCreated) {
        this.timeCreated = timeCreated;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getListOrder() {
        return listOrder;
    }

    public void setListOrder(Integer order) {
        this.listOrder = order;
    }

    public void incrementList_order() {
        this.listOrder++;
    }

    public void decrementList_order() {
        this.listOrder--;
    }
}