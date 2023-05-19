package com.tss.entities.data;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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
    private Collection<Board_members> boards_member = new ArrayList<>();

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("ownerToBoard")
    private Collection<Board> owned_boards = new ArrayList<>();

    public Collection<Board> getOwned_boards() {
        return owned_boards;
    }

    public void setOwned_boards(Collection<Board> owned_boards) {
        this.owned_boards = owned_boards;
    }

    public Collection<Board_members> getBoards_member() {
        return boards_member;
    }

    public void setBoards_member(Collection<Board_members> boards_member) {
        this.boards_member = boards_member;
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
        this.owned_boards.add(board);
    }

}