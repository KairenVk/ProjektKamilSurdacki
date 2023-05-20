package com.tss.repositories.credentials;

import com.tss.entities.credentials.Users_roles;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface Users_rolesRepository extends JpaRepository<Users_roles,Long> {
    List<String> getUser_rolesByLogin(String login);
}
