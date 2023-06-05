package com.tss.entities.data;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name = "board")
public class Board {
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

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "board", cascade = CascadeType.MERGE, orphanRemoval = true)
    @JsonIgnore
    private Collection<Board_members> boardMembers = new ArrayList<>();

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "board", cascade = CascadeType.MERGE, orphanRemoval = true)
    @JsonManagedReference(value = "taskLists")
    private Collection<TaskList> taskLists = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference(value="ownerToBoard")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User owner;

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public void setTaskLists(Collection<TaskList> taskLists) {
        this.taskLists = taskLists;
    }

    public Collection<TaskList> getTaskLists() {
        return taskLists;
    }

    public Collection<Board_members> getBoardMembers() {
        return boardMembers;
    }

    public void setBoardMembers(Collection<Board_members> boardMembers) {
        this.boardMembers = boardMembers;
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

    public void addTaskList(TaskList taskList) {
        this.taskLists.add(taskList);
    }
}