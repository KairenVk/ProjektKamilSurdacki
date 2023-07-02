package com.tss.repositories.credentials;

import com.tss.entities.credentials.UsersRoles;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface UsersRolesRepository extends JpaRepository<UsersRoles,Long> {
    Collection<UsersRoles> findUserRoleByUsername(String username);
}
