package com.regain.accountservicemaven.service;

import com.regain.accountservicemaven.model.Account;
import com.regain.accountservicemaven.model.dto.LoginForm;
import com.regain.accountservicemaven.model.dto.RegisterForm;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Optional;


public interface IAccountService extends UserDetailsService {
    Optional<Account> findById(Long id);

    List<Account> findAll();

    Account save(Account account);

    Account update(Account account);

    String register(RegisterForm registerForm);
    ResponseEntity<?> login(LoginForm loginForm);

    void delete(Long id);

    Account activeAccount(LoginForm email);

}
