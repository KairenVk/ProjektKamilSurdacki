package com.tss.controllers;

import com.tss.entities.data.User;
import com.tss.repositories.data.UserRepository;
import com.tss.services.UserService;
import com.tss.to.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.info.BuildProperties;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.Instant;
import java.util.Collection;

@Controller
public class TSSController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    BuildProperties buildProperties;

    @Autowired
    private UserService userService;

    @Value("${myparams.jdkversion}")
    String myjdkversion;

    @Value("${myparams.springbootversion}")
    String springbootversion;

    @Value("${application.name}")
    String applicationName;

    @Value("${build.version}")
    String buildVersion;

    @GetMapping({"/tss"})
    public String getHome(Model model) {
        Instant buildTimestamp = buildProperties.getTime();
        model.addAttribute("jdkVersion",myjdkversion);
        model.addAttribute("springBootVersion",springbootversion);
        model.addAttribute("applicationName",applicationName);
        model.addAttribute("buildVersion",buildVersion);
        model.addAttribute("buildTimestamp",buildTimestamp);
        return "tss";
    }

    @GetMapping({"tss_users"})
    public String tssGetUsers(Model model) {
        Collection<User> users = userRepository.findAll();
        model.addAttribute("users", users);
        return "tss_users";
    }

    @PostMapping({"addUser"})
    public String tssAddUser(Model model, UserDTO newUser) {
        userService.addUser(newUser);
        return "redirect:/tss_users";
    }

    @GetMapping({"tss_deleteUser/{id}"})
    public String tssDeleteUser(Model model, @PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:/tss_users";
    }

    @GetMapping({"tss_websocket"})
    public String tssWebsocket() {
        return "tss_websocket";
    }

    @GetMapping({"tss_websocketNoJSON"})
    public String tssWebsocketNoJSON(){
        return "tss_websocketNoJSON";
    }
}
