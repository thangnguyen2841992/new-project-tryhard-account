package com.regain.accountservicemaven.service;

import com.regain.accountservicemaven.model.Account;
import com.regain.accountservicemaven.repository.IAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AccountServiceImpl implements IAccountService {
    @Autowired
    private IAccountRepository accountRepository;


    @Override
    public Optional<Account> findById(Long id) {
        return this.accountRepository.findById(id);
    }

    @Override
    public List<Account> findAll() {
        return this.accountRepository.findAll();
    }

    @Override
    public Account save(Account account) {
        return this.accountRepository.save(account);
    }

    @Override
    public Account update(Account account) {
        return this.accountRepository.save(account);
    }

    @Override
    public void delete(Long id) {
        this.accountRepository.deleteById(id);
    }
}
