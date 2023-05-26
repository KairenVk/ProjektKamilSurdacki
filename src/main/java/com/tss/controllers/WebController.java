package com.tss.controllers;

import com.tss.entities.data.Board;
import com.tss.exceptions.EntityNotFoundException;
import com.tss.repositories.data.BoardRepository;
import com.tss.to.RegisterTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.info.BuildProperties;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.Instant;

@Controller
public class WebController {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    BuildProperties buildProperties;

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
    public String showRegisterForm(RegisterTO registerTO) {
        return "register";
    }

    @GetMapping("/home")
    public String showBoards(Model model) {
        return "home";
    }
}
