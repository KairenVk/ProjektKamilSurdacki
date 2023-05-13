package com.tss.services;

import com.tss.entities.RestForm;
import com.tss.entities.data.User;
import com.tss.exceptions.UserNotFoundException;
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

    public User addUser(@RequestBody RestForm newUser) {
        User user = new User();
        user.setUsername(newUser.getUsername());
        userRepository.save(user);
        return user;
    }

    public User editUser(@RequestBody RestForm editForm, Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        user.setUsername(editForm.getUsername());
        return user;
    }
}
