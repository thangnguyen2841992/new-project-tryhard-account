package com.regain.accountservicemaven.repository;

import com.regain.accountservicemaven.model.RoleAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IRoleRepository extends JpaRepository<RoleAccount, Long> {
    Optional<RoleAccount> findRoleAccountByRoleNameContaining(String roleName);
}
