package com.hirix.repository;

import com.hirix.domain.Employee;
import com.hirix.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findRoleByRoleName(String roleName);

    List<Role> findRolesByRoleNameLike(String roleName);
}
