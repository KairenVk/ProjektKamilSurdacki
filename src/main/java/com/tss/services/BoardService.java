package com.tss.services;

import com.tss.entities.data.Board;
import com.tss.entities.data.Board_members;
import com.tss.entities.data.TaskList;
import com.tss.entities.data.User;
import com.tss.exceptions.EntityNotFoundException;
import com.tss.exceptions.MissingParameterException;
import com.tss.exceptions.UnauthorizedException;
import com.tss.repositories.data.BoardRepository;
import com.tss.repositories.data.Board_membersRepository;
import com.tss.repositories.data.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Collection;

@Service
@Transactional
public class BoardService {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Board_membersRepository boardMembersRepository;

    @Autowired
    private TaskListService taskListService;

    public Board restAddBoard(Board board) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        board.setOwner(userRepository.findByUsername(currentPrincipalName)
                .orElseThrow(UnauthorizedException::new));
        return addBoard(board);
    }

    public Board webAddBoard(Board board, String ownerUsername) {
        board.setOwner(userRepository.findByUsername(ownerUsername)
                .orElseThrow(() -> new EntityNotFoundException(User.class.getSimpleName(),board.getOwner().getId())));
        return addBoard(board);
    }

    private Board addBoard(Board board) {
        if(board.getTitle() == null || board.getTitle().isEmpty())
            throw new MissingParameterException("title");
        board.setTimeCreated(Timestamp.from(Instant.now()));
        board.setTitle(HtmlUtils.htmlEscape(board.getTitle()));
        board.setBoardOrder(boardRepository.findAllByOwner(board.getOwner()).size());
        board.getOwner().addBoard(board);
        boardRepository.save(board);
        if (board.getBoardMembers() != null) {
            for(Board_members member:board.getBoardMembers()) {
                addMemberToBoard(member, board);
            }
        }
        if (board.getTaskLists() != null) {
            for(TaskList taskList:board.getTaskLists()) {
                taskListService.addList(taskList);
            }
        }
        return board;
    }

    public Board editBoard(Board board, Board editParams) {
        if(editParams.getOwner() != null) {
            board.setOwner(editParams.getOwner());
        }
        if(editParams.getTitle() != null && !editParams.getTitle().isEmpty()) {
            board.setTitle(HtmlUtils.htmlEscape(editParams.getTitle()));
        }
        board.setTimeModified(Timestamp.from(Instant.now()));
        return board;
    }

    public void deleteBoard(Board board) {
        boardRepository.delete(board);
    }

    public Board addBoardMembersByUsernames(Collection<String> usernames, Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new EntityNotFoundException(Board.class.getSimpleName(), boardId));
        for(String username:usernames) {
            User user = (userRepository.findByUsername(username)
                    .orElseThrow(() -> new EntityNotFoundException(Board.class.getSimpleName(), boardId)));
            Board_members boardMember = new Board_members();
            boardMember.setMember(user);
            boardMember.setBoard(board);
            boardMembersRepository.save(boardMember);
        }
        return board;
    }

    public void addMemberToBoard(Board_members boardMember, Board board) {
        boardMember.setMember(userRepository.findById(boardMember.getMember().getId())
                .orElseThrow(() -> new EntityNotFoundException(User.class.getSimpleName(), boardMember.getMember().getId())));
        boardMember.setBoard(board);
        boardMembersRepository.save(boardMember);
    }

    public void addMemberToBoard(Board_members boardMember) {
        boardMembersRepository.save(boardMember);
    }
}
