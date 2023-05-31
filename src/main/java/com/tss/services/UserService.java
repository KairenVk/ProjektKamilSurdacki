package com.tss.services;

import com.tss.assemblers.UserModelAssembler;
import com.tss.to.UserDTO;
import com.tss.entities.data.Board;
import com.tss.entities.data.Board_members;
import com.tss.entities.data.User;
import com.tss.exceptions.EntityNotFoundException;
import com.tss.repositories.data.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CredentialsService credentialsService;

    @Autowired
    private BoardService boardService;

    @Autowired
    private UserModelAssembler userModelAssembler;

    public User addUser(UserDTO newUser) {
        User user = new User();
        user.setUsername(newUser.getUsername());
        if (newUser.getOwned_boards() != null) {
            for (Board board:newUser.getOwned_boards()) {
                boardService.addBoard(board);
                user.addBoard(board);
            }
        }
        userRepository.save(user);
        if (newUser.getBoards_member() != null) {
            for (Board_members board_member:newUser.getBoards_member()) {
                boardService.addMemberToBoard(board_member);
            }
        }
        userRepository.save(user);
        credentialsService.addCredentials(newUser);
        return user;
    }

    public User editUser(@RequestBody UserDTO editForm, Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(User.class.getSimpleName(),id));
        user.setUsername(editForm.getUsername());
        credentialsService.editCredentials(editForm,id);
        return user;
    }

    public void addOwnedBoardToUser(Board board) {
        User user = board.getOwner();
        user.addBoard(board);
    }
}
