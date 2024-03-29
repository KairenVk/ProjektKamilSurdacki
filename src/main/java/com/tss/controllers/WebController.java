package com.tss.controllers;

import com.tss.entities.credentials.Credentials;
import com.tss.entities.data.Board;
import com.tss.entities.data.Task;
import com.tss.entities.data.TaskList;
import com.tss.entities.data.User;
import com.tss.exceptions.EntityNotFoundException;
import com.tss.repositories.credentials.CredentialsRepository;
import com.tss.repositories.data.BoardRepository;
import com.tss.repositories.data.TaskListRepository;
import com.tss.repositories.data.TaskRepository;
import com.tss.repositories.data.UserRepository;
import com.tss.services.*;
import com.tss.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@Transactional
public class WebController {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private TaskListRepository taskListRepository;

    @Autowired
    private BoardService boardService;

    @Autowired
    private TaskListService taskListService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private CredentialsRepository credentialsRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private AuthorizationService authorizationService;

    
    @GetMapping("/")
    public String index() {
        return "redirect:/boards";
    }
    
    @GetMapping("/board/{boardId}")
    public String getBoard(Model model, @PathVariable Long boardId, @ModelAttribute TaskList taskList, @ModelAttribute Task task) {
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new EntityNotFoundException(Board.class.getSimpleName(), boardId));
        model.addAttribute("board", board);
        if(authorizationService.isWebAuthorized(board.getOwner().getId()))
            return "board";
        else
            return "redirect:/boards";
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

    @PostMapping("addUser")
    public String addUser(UserDTO userDTO) {
        userService.addUser(userDTO);
        return "boards";
    }

    @GetMapping("/boards")
    public String showBoards(Model model, @ModelAttribute Board board) {
        return "boards";
    }

    @GetMapping("/addBoardForm")
    public String addBoardForm(Model model) {
        return "addBoardForm";
    }

    @PostMapping("/addBoard")
    public String addBoard(Board board, String ownerUsername) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Board newBoard = boardService.webAddBoard(board, authentication.getName());
        return "redirect:/board/"+newBoard.getId();
    }

    @GetMapping("/board/{boardId}/addListForm")
    public String addListForm(Model model, @PathVariable Long boardId) {
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

    @PostMapping("/addTask")
    public String addTask(Task task, @RequestParam Long listId) {
        taskService.addTaskListId(task, listId);
        return "redirect:/board/"+task.getTaskList().getBoard().getId();
    }
    @GetMapping("/deleteList/{listId}")
    public String deleteList(@PathVariable Long listId) {
        TaskList list = taskListRepository.findById(listId).orElseThrow(() -> new EntityNotFoundException(TaskList.class.getSimpleName(), listId));
        Long boardId = list.getBoard().getId();
        taskListService.deleteList(list);
        return "redirect:/board/"+boardId;
    }

    @GetMapping("/deleteTask/{taskId}")
    public String deleteTask(@PathVariable Long taskId) {
        Task taskToDelete = taskRepository.findById(taskId).orElseThrow(() -> new EntityNotFoundException(Task.class.getSimpleName(), taskId));
        Long boardId = taskToDelete.getTaskList().getBoard().getId();
        taskService.deleteTask(taskToDelete);
        return "redirect:/board/"+boardId;
    }
}
