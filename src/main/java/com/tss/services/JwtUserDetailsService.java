package com.tss.services;

import com.tss.entities.credentials.Credentials;
import com.tss.entities.credentials.UsersRoles;
import com.tss.repositories.credentials.CredentialsRepository;
import com.tss.repositories.credentials.UsersRolesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    private CredentialsRepository credentialsRepository;

    @Autowired
    UsersRolesRepository usersRolesRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        Credentials credentials = credentialsRepository.findByUsername(username);
        if (credentials != null) {
            List<GrantedAuthority> authorities = new ArrayList<>();
            for (UsersRoles role: usersRolesRepository.findUserRoleByUsername(credentials.getUsername())) {
                authorities.add(new SimpleGrantedAuthority(role.getUserRole()));
            }
            return new User(credentials.getUsername(), credentials.getPassword(), authorities);
        }
        else
            throw new UsernameNotFoundException("User not found with login: " + username);
    }
}
