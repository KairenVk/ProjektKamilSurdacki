package com.tss.entities.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Table(name = "user_table")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "username", nullable = false, length = 20)
    private String username;

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "member", cascade = CascadeType.MERGE, orphanRemoval = true)
    @JsonIgnore
    private Collection<Board_members> boardsJoined = new ArrayList<>();

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("ownerToBoard")
    private Collection<Board> ownedBoards = new ArrayList<>();

    public Collection<Board> getOwnedBoards() {
        return ownedBoards;
    }

    public void setOwnedBoards(Collection<Board> ownedBoards) {
        this.ownedBoards = ownedBoards;
    }

    public Collection<Board_members> getBoardsJoined() {
        return boardsJoined;
    }

    public void setBoardsJoined(Collection<Board_members> boardsJoined) {
        this.boardsJoined = boardsJoined;
    }


    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getId() {
        return id;
    }

    public void addBoard(Board board) {
        this.ownedBoards.add(board);
    }

}