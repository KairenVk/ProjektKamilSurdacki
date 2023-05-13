package com.tss.services;

import com.tss.entities.RestForm;
import com.tss.entities.credentials.Credentials;
import com.tss.repositories.credentials.CredentialsRepository;
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
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public void addCredentials(@RequestBody RestForm newCredentials, Long userId) {
        Credentials credentials = new Credentials();
        credentials.setLogin(newCredentials.getLogin());
        credentials.setPassword(bCryptPasswordEncoder.encode(newCredentials.getPassword()));
        credentials.setUser_id(userId);
        credentials.setEmail(newCredentials.getEmail());
        credentials.setActive(true);
        credentialsRepository.save(credentials);
    }

    public Credentials editCredentials(@RequestBody RestForm editForm, Long userId) {
        Credentials credentials = credentialsRepository.getReferenceById(userId);
        credentials.setPassword(editForm.getPassword());
        credentials.setEmail(editForm.getEmail());
        return credentials;
    }
}
