package com.regain.accountservicemaven.repository;

import com.regain.accountservicemaven.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IAccountRepository extends JpaRepository<Account, Long> {

  @Query(value = "SELECT * FROM account WHERE email = :email", nativeQuery = true)
  Optional<Account> findByEmail(@Param("email") String email);

  boolean existsByEmail(String email);
  boolean existsByPhone(String phone);
}
