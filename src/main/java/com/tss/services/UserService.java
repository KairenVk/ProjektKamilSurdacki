package com.tss.services;

import com.tss.dto.UserDTO;
import com.tss.entities.data.Board;
import com.tss.entities.data.Board_members;
import com.tss.entities.data.User;
import com.tss.exceptions.EntityNotFoundException;
import com.tss.exceptions.MissingParameterException;
import com.tss.exceptions.UserExistsException;
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
    private AuthorizationService authorizationService;

    public User addUser(UserDTO newUser) {
        validateInput(newUser);
        User user = new User();
        user.setUsername(newUser.getUsername());
        user = userRepository.save(user);
        if (newUser.getOwnedBoards() != null) {
            for (Board board:newUser.getOwnedBoards()) {
                boardService.webAddBoard(board, user.getUsername());
            }
        }
        if (newUser.getBoardsJoined() != null) {
            for (Board_members board_member:newUser.getBoardsJoined()) {
                boardService.addMemberToBoard(board_member);
            }
        }
        userRepository.save(user);
        credentialsService.addCredentials(newUser);
        return user;
    }

    public User editUser(@RequestBody UserDTO editForm, Long id) {
        authorizationService.isAuthorized(id);
        User user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(User.class.getSimpleName(),id));

        if (editForm.getUsername() != null && !editForm.getUsername().isEmpty()) {
            if (userRepository.existsByUsername(editForm.getUsername()))
                throw new UserExistsException();
            user.setUsername(editForm.getUsername());
        }
        credentialsService.editCredentials(editForm,id);
        return user;
    }

    public void deleteUser(Long id) {
        authorizationService.isAuthorized(id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(User.class.getSimpleName(), id));
        String username = user.getUsername();
        userRepository.delete(user);
        credentialsService.deleteCredentials(username);
    }

    private void validateInput(UserDTO user) {
        if (userRepository.existsByUsername(user.getUsername()))
            throw new UserExistsException();
        if (user.getUsername() == null || user.getUsername().isEmpty()) {
            throw new MissingParameterException("username");
        }
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            throw new MissingParameterException("password");
        }
        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            throw new MissingParameterException("email");
        }
    }
}
