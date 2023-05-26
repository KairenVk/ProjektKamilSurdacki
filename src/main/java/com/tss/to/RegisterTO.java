package com.tss.to;

import com.tss.entities.credentials.Credentials;
import com.tss.entities.data.Board;
import com.tss.entities.data.Board_members;

import java.util.Collection;

public class RegisterTO extends Credentials {

    private String username;

    private Collection<Board_members> boards_member;

    private Collection<Board> owned_boards;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Collection<Board_members> getBoards_member() {
        return boards_member;
    }

    public void setBoards_member(Collection<Board_members> boards_member) {
        this.boards_member = boards_member;
    }

    public Collection<Board> getOwned_boards() {
        return owned_boards;
    }

    public void setOwned_boards(Collection<Board> owned_boards) {
        this.owned_boards = owned_boards;
    }
}
