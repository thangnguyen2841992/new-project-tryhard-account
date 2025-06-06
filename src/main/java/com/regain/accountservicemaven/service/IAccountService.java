package com.regain.accountservicemaven.service;

import com.regain.accountservicemaven.model.Account;
import com.regain.accountservicemaven.model.dto.AccountDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


public interface IAccountService {
    Optional<Account> findById(Long id);

    List<Account> findAll();

    Account save(Account account);

    Account update(Account account);

    String register(AccountDTO accountDTO);

    void delete(Long id);

}
