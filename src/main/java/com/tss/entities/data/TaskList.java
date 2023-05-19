package com.tss.entities.data;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Table(name = "tasklist")
public class TaskList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "time_created", nullable = false)
    private Timestamp time_created;

    @Column(name = "time_modified")
    private Timestamp time_modified;

    @ManyToOne(cascade = CascadeType.MERGE, optional = false)
    @JoinColumn(name = "board_id", nullable = false)
    @JsonBackReference(value="taskLists")
    private Board board;

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "taskList", cascade = CascadeType.MERGE, orphanRemoval = true)
    @JsonManagedReference
    private Collection<Task> tasks = new ArrayList<>();

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

    public Timestamp getTime_modified() {
        return time_modified;
    }

    public void setTime_modified(Timestamp time_modified) {
        this.time_modified = time_modified;
    }

    public Timestamp getTime_created() {
        return time_created;
    }

    public void setTime_created(Timestamp time_created) {
        this.time_created = time_created;
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

}