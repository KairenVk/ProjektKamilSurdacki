package com.tss.repositories.credentials;

import com.tss.entities.credentials.Credentials;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CredentialsRepository extends JpaRepository<Credentials,Long> {
    Credentials findByUsername(String username);

    void deleteByUsername(String username);
}
