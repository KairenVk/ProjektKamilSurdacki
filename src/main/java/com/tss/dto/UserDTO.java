package com.tss.dto;

import com.tss.entities.credentials.Credentials;
import com.tss.entities.data.Board;
import com.tss.entities.data.Board_members;

import java.util.Collection;

public class UserDTO extends Credentials {

    private String username;

    private Collection<Board_members> boardsJoined;

    private Collection<Board> ownedBoards;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Collection<Board_members> getBoardsJoined() {
        return boardsJoined;
    }

    public void setBoardsJoined(Collection<Board_members> boardsJoined) {
        this.boardsJoined = boardsJoined;
    }

    public Collection<Board> getOwnedBoards() {
        return ownedBoards;
    }

    public void setOwnedBoards(Collection<Board> ownedBoards) {
        this.ownedBoards = ownedBoards;
    }
}
