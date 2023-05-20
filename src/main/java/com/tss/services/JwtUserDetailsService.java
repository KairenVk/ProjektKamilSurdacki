package com.tss.services;

import com.tss.entities.credentials.Credentials;
import com.tss.repositories.credentials.CredentialsRepository;
import com.tss.repositories.credentials.Users_rolesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    private CredentialsRepository credentialsRepository;

    @Autowired
    Users_rolesRepository usersRolesRepository;

    @Override
    public UserDetails loadUserByUsername(String login) {
        Credentials credentials = credentialsRepository.findByLogin(login);
        if (credentials != null) {
            return new User(credentials.getLogin(), credentials.getPassword(), new ArrayList<>());
        }
        else
            throw new UsernameNotFoundException("User not found with login: " + login);
    }
}
