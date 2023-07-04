package com.tss.services;

import com.tss.entities.data.User;
import com.tss.exceptions.UnauthorizedException;
import com.tss.repositories.data.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AuthorizationService {

    @Autowired
    private UserRepository userRepository;

    public void isAuthorized(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        User user = userRepository.findByUsername(currentPrincipalName).orElseThrow(UnauthorizedException::new);
        if ( !user.getId().equals(id)) {
            throw new UnauthorizedException();
        }
    }

    public boolean isWebAuthorized(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        User user = userRepository.findByUsername(currentPrincipalName).orElseThrow(UnauthorizedException::new);
        return user.getId().equals(id);
    }
}
