package com.regain.accountservicemaven.controller;

import com.regain.accountservicemaven.client.INotificationClient;
import com.regain.accountservicemaven.model.Account;
import com.regain.accountservicemaven.model.dto.AccountDTO;
import com.regain.accountservicemaven.model.dto.LoginForm;
import com.regain.accountservicemaven.model.dto.MessageDTO;
import com.regain.accountservicemaven.model.dto.RegisterForm;
import com.regain.accountservicemaven.service.IAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/account")
public class AccountRestController {

    @Autowired
    private IAccountService accountService;

    @Autowired
    private INotificationClient  notificationClient;

    @GetMapping("/findAllAccount")
    public ResponseEntity<List<Account>> findAllAccount() {
        List<Account> accounts = accountService.findAll();
        return new ResponseEntity<>(accounts, HttpStatus.OK);
    }

    @GetMapping("/findAccountById")
    public ResponseEntity<Account> findAccountById(Long id) {
        Optional<Account> account = accountService.findById(id);
        return account.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/createNewAccount")
    public ResponseEntity<String> createAccount(@RequestBody RegisterForm registerForm) {
        String resultRegister = this.accountService.register(registerForm);
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setFrom("nguyenthang29tbdl@gmail.com");
        messageDTO.setTo(registerForm.getEmail()); //username is Email
        messageDTO.setToName(registerForm.getFirstName() + " " + registerForm.getLastName());
        messageDTO.setSubject("Chào Mừng Bạn Đến Với Website");
        messageDTO.setContent("Bạn hãy click vào link dưới để xác thực tài khoản nhé:");

        notificationClient.sendNotificationEmail(messageDTO);
        return new ResponseEntity<>(resultRegister, HttpStatus.CREATED);
    }

    @PostMapping("/activeAccount")
    public ResponseEntity<Account> activeAccount(@RequestBody LoginForm loginForm) {
        Account account =  this.accountService.activeAccount(loginForm);
        return new ResponseEntity<>(account, HttpStatus.OK);
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
