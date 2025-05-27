package com.regain.accountservicemaven.service;

import com.regain.accountservicemaven.model.Account;
import com.regain.accountservicemaven.model.Role;
import com.regain.accountservicemaven.model.dto.AccountDTO;
import com.regain.accountservicemaven.repository.IAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class AccountServiceImpl implements IAccountService {
    @Autowired
    private IAccountRepository accountRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;


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
    public String register(AccountDTO accountDTO) {
        if (!accountDTO.getConfirmPassword().equals(accountDTO.getPassword())) {
            return "Password do not match";
        }else {
            Account account = new Account();
            account.setFirstName(accountDTO.getFirstName());
            account.setLastName(accountDTO.getLastName());
            account.setEmail(accountDTO.getEmail());
            account.setPassword(passwordEncoder.encode(accountDTO.getPassword()));
            account.setCity(accountDTO.getCity());
            account.setCountry(accountDTO.getCountry());
            account.setAvatar(accountDTO.getAvatar());
            account.setPhone(accountDTO.getPhone());
            account.setAddress(accountDTO.getAddress());
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            try {
                Date birthDate = formatter.parse(accountDTO.getBirthDate());
                account.setBirthDate(birthDate);
            } catch (ParseException e) {
                return "Invalid birthday";
            }
            account.setJobTitle(accountDTO.getJobTitle());
            account.setGender(accountDTO.getGender());
            account.setFullName(accountDTO.getFirstName() + " " + accountDTO.getLastName());

            Set<Role> roles = new HashSet<>();
            for (int i = 0; i < accountDTO.getRoles().length; i++) {
                Optional<Role> roleOptional = this.roleRepository.findByName(userDTO.getRole()[i]);
                roleOptional.ifPresent(role -> roles.add(new Role(role.getRoleId(), role.getRoleName())));
            }
            account.setRoles(roles);
        }
        return null;
    }

    @Override
    public void delete(Long id) {
        this.accountRepository.deleteById(id);
    }
}
