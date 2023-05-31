package com.tss.controllers;

import com.tss.entities.data.Board;
import com.tss.entities.data.User;
import com.tss.exceptions.EntityNotFoundException;
import com.tss.repositories.data.BoardRepository;
import com.tss.repositories.data.UserRepository;
import com.tss.services.BoardService;
import com.tss.to.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.info.BuildProperties;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.Instant;

@Controller
@Transactional
public class WebController {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    BuildProperties buildProperties;

    @Autowired
    private BoardService boardService;

    @Value("${myparams.jdkversion}")
    String myjdkversion;

    @Value("${myparams.springbootversion}")
    String springbootversion;

    @Value("${application.name}")
    String applicationName;

    @Value("${build.version}")
    String buildVersion;

    @GetMapping({"/"})
    public String getHome(Model model) {
        Instant buildTimestamp = buildProperties.getTime();
        model.addAttribute("jdkVersion",myjdkversion);
        model.addAttribute("springBootVersion",springbootversion);
        model.addAttribute("applicationName",applicationName);
        model.addAttribute("buildVersion",buildVersion);
        model.addAttribute("buildTimestamp",buildTimestamp);
        return "index";
    }

    @GetMapping("/board/{boardId}")
    public String getBoard(Model model, @PathVariable Long boardId) {
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new EntityNotFoundException(Board.class.getSimpleName(), boardId));
        model.addAttribute("board", board);
        return "board";
    }

    @GetMapping("/register")
    public String showRegisterForm(UserDTO userDTO) {
        return "register";
    }

    @GetMapping("/home")
    public String showBoards(Model model) {
        return "home";
    }

    @GetMapping("/addBoardForm")
    public String addBoardForm(Model model, @ModelAttribute Board board) {
        return "addBoardForm";
    }

    @PostMapping("/addBoard")
    public String addBoard(Model model, @ModelAttribute Board board) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User owner = userRepository.findByUsername(authentication.getName()).orElseThrow();
        board.setOwner(owner);
        Board newBoard = boardService.addBoard(board);
        return "redirect:/board/"+newBoard.getId();
    }
}
