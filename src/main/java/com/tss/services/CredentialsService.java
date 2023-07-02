package com.tss.services;

import com.tss.dto.UserDTO;
import com.tss.entities.credentials.Credentials;
import com.tss.entities.credentials.UsersRoles;
import com.tss.repositories.credentials.CredentialsRepository;
import com.tss.repositories.credentials.UsersRolesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@Transactional(transactionManager = "credentialsTransactionManager")
public class CredentialsService {

    @Autowired
    private CredentialsRepository credentialsRepository;

    @Autowired
    private UsersRolesRepository usersRolesRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public void addCredentials(@RequestBody UserDTO newCredentials) {
        Credentials credentials = new Credentials();
        credentials.setUsername(newCredentials.getUsername());
        credentials.setPassword(bCryptPasswordEncoder.encode(newCredentials.getPassword()));
        credentials.setEmail(newCredentials.getEmail());
        credentials.setActive(true);
        credentialsRepository.save(credentials);
        UsersRoles userRoles = new UsersRoles();
        userRoles.setUserRole("ROLE_MEMBER");
        userRoles.setUsername(newCredentials.getUsername());
        usersRolesRepository.save(userRoles);
    }

    public Credentials editCredentials(@RequestBody UserDTO editForm, Long userId) {
        Credentials credentials = credentialsRepository.findById(userId).orElseThrow();
        if (editForm.getUsername() != null)
            credentials.setUsername(editForm.getUsername());
        if (editForm.getPassword() != null)
            credentials.setPassword(bCryptPasswordEncoder.encode(editForm.getPassword()));
        if (editForm.getEmail() != null)
            credentials.setEmail(editForm.getEmail());
        return credentials;
    }

    public void deleteCredentials(Long id) {
        credentialsRepository.deleteById(id);
    }
}
