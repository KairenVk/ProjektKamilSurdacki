package com.tss.controllers;

import com.tss.entities.data.Board;
import com.tss.entities.data.Task;
import com.tss.entities.data.TaskList;
import com.tss.exceptions.EntityNotFoundException;
import com.tss.repositories.data.BoardRepository;
import com.tss.repositories.data.TaskListRepository;
import com.tss.repositories.data.UserRepository;
import com.tss.services.BoardService;
import com.tss.services.TaskListService;
import com.tss.services.TaskService;
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
    private TaskListRepository taskListRepository;

    @Autowired
    BuildProperties buildProperties;

    @Autowired
    private BoardService boardService;

    @Autowired
    private TaskListService taskListService;

    @Autowired
    private TaskService taskService;

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

    @GetMapping("/boards")
    public String showBoards(Model model) {
        return "home";
    }

    @GetMapping("/addBoardForm")
    public String addBoardForm(Model model, @ModelAttribute Board board) {
        return "addBoardForm";
    }

    @PostMapping("/addBoard")
    public String addBoard(Board board, String ownerUsername) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Board newBoard = boardService.webAddBoard(board, authentication.getName());
        return "redirect:/board/"+newBoard.getId();
    }

    @GetMapping("/board/{boardId}/addListForm")
    public String addListForm(Model model, @ModelAttribute TaskList list, @PathVariable Long boardId) {
        model.addAttribute("board", boardRepository.findById(boardId).orElseThrow(() -> new EntityNotFoundException(Board.class.getSimpleName(), boardId)));
        return "addListForm";
    }

    @PostMapping("/board/{boardId}/addList")
    public String addList(TaskList list, @PathVariable Long boardId) {
        list.setBoard(boardRepository.findById(boardId).orElseThrow(() -> new EntityNotFoundException(Board.class.getSimpleName(), boardId)));
        taskListService.addList(list);
        return "redirect:/board/{boardId}";
    }

    @GetMapping("/list/{listId}/addTaskForm")
    public String addTaskForm(Model model, @ModelAttribute Task task,@PathVariable Long listId) {
        model.addAttribute("list", taskListRepository.findById(listId).orElseThrow(() -> new EntityNotFoundException(TaskList.class.getSimpleName(), listId)));
        return "addTaskForm";
    }

    @PostMapping("/list/{listId}/addTask")
    public String addTask(Task task, @PathVariable Long listId) {
        task.setTaskList(taskListRepository.findById(listId).orElseThrow(() -> new EntityNotFoundException(TaskList.class.getSimpleName(), listId)));
        taskService.addTask(task);
        return "redirect:/board/"+task.getTaskList().getBoard().getId();
    }
}
