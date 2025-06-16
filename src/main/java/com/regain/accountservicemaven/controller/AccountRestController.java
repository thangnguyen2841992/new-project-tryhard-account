package com.regain.accountservicemaven.controller;

import com.regain.accountservicemaven.model.Account;
import com.regain.accountservicemaven.model.dto.AccountDTO;
import com.regain.accountservicemaven.model.dto.LoginForm;
import com.regain.accountservicemaven.model.dto.MessageDTO;
import com.regain.accountservicemaven.model.dto.RegisterForm;
import com.regain.accountservicemaven.repository.IAccountRepository;
import com.regain.accountservicemaven.service.IAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/account")
public class AccountRestController {

    @Autowired
    private IAccountService accountService;

    @Autowired
    KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private IAccountRepository accountRepository;

    @GetMapping("/findAllAccount")
    public ResponseEntity<List<Account>> findAllAccount() {
        List<Account> accounts = accountService.findAll();
        return new ResponseEntity<>(accounts, HttpStatus.OK);
    }

    @GetMapping("/findAccountByAccountId/{accountId}")
    public ResponseEntity<AccountDTO> findAccountById(@PathVariable Long accountId) {
        AccountDTO accountDTO = this.accountService.findAccountByAccountId(accountId);
        return new ResponseEntity<>(accountDTO, HttpStatus.OK);
    }

    @PostMapping("/createNewAccount")
    public ResponseEntity<Account> createAccount(@RequestBody RegisterForm registerForm) throws IOException {
        Account account = this.accountService.register(registerForm);
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setFrom("nguyenthang29tbdl@gmail.com");
        messageDTO.setTo(registerForm.getEmail()); //username is Email
        messageDTO.setActiveCode(account.getCodeActive());
        messageDTO.setToName(registerForm.getFirstName() + " " +registerForm.getLastName());
        kafkaTemplate.send("send-email-active", messageDTO);
        return new ResponseEntity<>(account, HttpStatus.CREATED);
    }

    @PostMapping("/activeAccount")
    public void activeAccount(@RequestParam(name = "email") String email, @RequestParam(name = "activeCode") String activeCode) {
        this.accountService.activeAccount(email, activeCode);
    }

    @GetMapping("/checkExistEmail")
    public ResponseEntity<Boolean> checkExistEmail(@RequestParam(name = "email") String email) {
        boolean isExistEmail = this.accountRepository.existsByEmail(email);
        return new ResponseEntity<>(isExistEmail, HttpStatus.OK);
    }

    @GetMapping("/checkExistPhone")
    public ResponseEntity<Boolean> checkExistPhone(@RequestParam(name = "phone") String phone) {
        boolean isExistPhone= this.accountRepository.existsByPhone(phone);
        return new ResponseEntity<>(isExistPhone, HttpStatus.OK);
    }

    @PostMapping("/loginAccount")
    public ResponseEntity<?> loginAccount(@RequestBody LoginForm loginForm) {
        return this.accountService.login(loginForm);
    }

    @PutMapping("/updateAccount")
    public ResponseEntity<Account> updateAccount(@RequestBody Account account) {
        Optional<Account> accountOptional = this.accountService.findById(account.getId());
        if (accountOptional.isPresent()) {
            Account accountUpdate = this.accountService.save(account);
            return new ResponseEntity<>(accountUpdate, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping
    public ResponseEntity<Account> deleteAccount(Long id) {
        Optional<Account> account = accountService.findById(id);
        if (account.isPresent()) {
            this.accountService.delete(id);
            return new ResponseEntity<>(account.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
