package com.regain.accountservicemaven.service;

import com.regain.accountservicemaven.model.Account;
import com.regain.accountservicemaven.model.RoleAccount;
import com.regain.accountservicemaven.model.dto.AccountDTO;
import com.regain.accountservicemaven.model.dto.JwtResponse;
import com.regain.accountservicemaven.model.dto.LoginForm;
import com.regain.accountservicemaven.model.dto.RegisterForm;
import com.regain.accountservicemaven.repository.IAccountRepository;
import com.regain.accountservicemaven.repository.IRoleRepository;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl implements IAccountService {
    @Autowired
    private IAccountRepository accountRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private IRoleRepository roleRepository;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtService jwtService;


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
    public Account register(RegisterForm registerForm) throws IOException {
        if (!registerForm.getConfirmPassword().equals(registerForm.getPassword())) {
            throw new RuntimeException("Password do not match");
        } else {
            if (registerForm.getRoles().length == 0) {
                throw new RuntimeException("Roles is not Empty");
            }
            Account account = new Account();
//            SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date birthDate = formatter.parse(registerForm.getBirthDate());
                account.setBirthDate(birthDate);
            } catch (ParseException e) {
                throw new RuntimeException("Invalid birthday");
            }
            account.setFirstName(registerForm.getFirstName());
            account.setLastName(registerForm.getLastName());
            String username = getUserNameByEmail(registerForm.getEmail());
            account.setUsername(username);
            account.setEmail(registerForm.getEmail());
            account.setPassword(passwordEncoder.encode(registerForm.getPassword()));
            account.setCity(registerForm.getCity());
            account.setCountry(registerForm.getCountry());
            account.setAvatar(registerForm.getAvatar());
            account.setPhone(registerForm.getPhone());
            account.setAddress(registerForm.getAddress());
            account.setCodeActive(createActiveCode());
            account.setAvatar("https://firebasestorage.googleapis.com/v0/b/thangdeptrai-9efec.appspot.com/o/images%2F293152416_586744886309092_690567344764305395_n.jpg?alt=media&token=1c6ef82a-ae9a-4950-93c2-a20fbc0ff7c7");
            account.setActive(false);

            account.setJobTitle(registerForm.getJobTitle());
            account.setGender(registerForm.getGender());
            account.setFullName(registerForm.getFirstName() + " " + registerForm.getLastName());


            Set<RoleAccount> roleAccounts = new HashSet<>();
            for (int i = 0; i < registerForm.getRoles().length; i++) {
                Optional<RoleAccount> roleOptional = this.roleRepository.findRoleAccountByRoleNameContaining(registerForm.getRoles()[i]);
                roleOptional.ifPresent(roleAccount -> {
                    roleAccounts.add(new RoleAccount(roleAccount.getRoleId(), roleAccount.getRoleName()));
                });
            }
            account.setRoleAccounts(roleAccounts);
            return this.accountRepository.save(account);
        }
    }

    private String createActiveCode() {
        return UUID.randomUUID().toString();
    }


    public ResponseEntity<?> login(LoginForm form) {
        // Xác thực người dùng bằng tên đăng nhập và mật khẩu
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(form.getEmail(), form.getPassword())
            );
            // Nếu xác thực thành công, tạo token JWT
            if (authentication.isAuthenticated()) {
                final String jwt = jwtService.generateToken(form.getEmail());
                return ResponseEntity.ok(new JwtResponse(jwt));
            }
        } catch (AuthenticationException e) {
            // Xác thực không thành công, trả về lỗi hoặc thông báo
            return ResponseEntity.badRequest().body("Tên đăng nhập hặc mật khẩu không chính xác.");
        }
        return ResponseEntity.badRequest().body("Xác thực không thành công.");
    }


    @Override
    public void delete(Long id) {
        this.accountRepository.deleteById(id);
    }

    @Override
    public void activeAccount(String email, String activeCode) {
        Optional<Account> account = accountRepository.findByEmail(email);
        if (account.isPresent()) {
            if (account.get().isActive()) {
                new ResponseEntity<>("account activated!", HttpStatus.BAD_REQUEST);
            } else {
                if (account.get().getCodeActive().equals(activeCode)) {
                    account.get().setActive(true);
                    accountRepository.save(account.get());
                    new ResponseEntity<>("account active successes!", HttpStatus.OK);
                } else {
                    new ResponseEntity<>("active code is not corrected!", HttpStatus.BAD_REQUEST);
                }
            }
        } else {
            new ResponseEntity<>("Not found account", HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public AccountDTO findAccountByAccountId(Long accountId) {
        Account account = this.accountRepository.findById(accountId).orElseThrow(() -> new RuntimeException("Not found account: " + accountId));
       AccountDTO accountDTO = new AccountDTO();
       accountDTO.setId(account.getId());
       accountDTO.setFirstName(account.getFirstName());
       accountDTO.setLastName(account.getLastName());
       accountDTO.setFullName(account.getFullName());
       accountDTO.setEmail(account.getEmail());
       accountDTO.setPassword(account.getPassword());
       accountDTO.setCity(account.getCity());
       accountDTO.setCountry(account.getCountry());
       accountDTO.setAvatar(account.getAvatar());
       accountDTO.setPhone(account.getPhone());
       accountDTO.setUsername(account.getUsername());
       accountDTO.setAddress(account.getAddress());
       return accountDTO;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Account> accountOptional = this.accountRepository.findByEmail(email);
        Account account = accountOptional.get();
        return new User(account.getEmail(), account.getPassword(), rolesToAuthorities(account.getRoleAccounts()));
    }

    private Collection<? extends GrantedAuthority> rolesToAuthorities(Collection<RoleAccount> roles) {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getRoleName())).collect(Collectors.toList());
    }

    private String getUserNameByEmail(String email) {
        int index = email.indexOf("@");
        if (index != -1) {
            return email.substring(0, index);
        } else {
            return "Email invalid";
        }
    }

    public String imageToBase64(String imagePath) throws IOException {
        File f = new File(imagePath);
        FileInputStream fin = new FileInputStream(f);
        byte[] imageByteArr = new byte[(int) f.length()];
        fin.read(imageByteArr);
        String imageToBase64 = "data:image/jpeg;base64,";
        imageToBase64 += Base64.encodeBase64String(imageByteArr);

        fin.close();
        return imageToBase64;
    }
}
