package com.tss.controllers;

import com.tss.entities.credentials.Credentials;
import com.tss.entities.data.Board;
import com.tss.entities.data.User;
import com.tss.exceptions.EntityNotFoundException;
import com.tss.repositories.credentials.CredentialsRepository;
import com.tss.repositories.data.BoardRepository;
import com.tss.repositories.data.UserRepository;
import com.tss.services.UserService;
import com.tss.to.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class WebController {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CredentialsRepository credentialsRepository;



    @GetMapping("/board/{boardId}")
    public String getBoard(Model model, @PathVariable Long boardId) {
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new EntityNotFoundException(Board.class.getSimpleName(), boardId));
        model.addAttribute("board", board);
        return "board";
    }

    @GetMapping("/registerForm")
    public String showRegisterForm(@ModelAttribute("userDTO") UserDTO userDTO) {
        return "register";
    }

    @GetMapping("editUserForm/{id}")
    public String showEditUserForm(@ModelAttribute("userDTO") UserDTO userDTO, Model model, @PathVariable long id) {
        User userInfo = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(User.class.getSimpleName(), id));
        Credentials credentialsInfo = credentialsRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(Credentials.class.getSimpleName(), id));
        userDTO.setUsername(userInfo.getUsername());
        userDTO.setPassword(credentialsInfo.getPassword());
        userDTO.setEmail(credentialsInfo.getEmail());
        return "editUserForm";
    }

    @PostMapping("editUser/{id}")
    public String editUser(UserDTO userDTO, @PathVariable Long id) {
        userService.editUser(userDTO, id);
        return "redirect:/tss_users";
    }

    @GetMapping("/home")
    public String showBoards(Model model) {
        return "home";
    }
}
