package com.regain.accountservicemaven.service;

import com.regain.accountservicemaven.model.Account;
import com.regain.accountservicemaven.model.RoleAccount;
import com.regain.accountservicemaven.repository.IAccountRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;

@Service
public class JwtService {
    public static final String SECRET = "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";

    @Autowired
    private IAccountRepository repository;

    // Tạo JWT dựa trên tên đang nhập
    public String generateToken(String email) {
        Map<String, Object> claims = new HashMap<>();
        Account account = repository.findByEmail(email).orElseThrow(() -> new RuntimeException("Không tìm thấy tài khoản với email: " + email));

        boolean isAdmin = false;
        boolean isUser = false;

        String fullName = account.getFirstName() + " " + account.getLastName();

        if (!account.getRoleAccounts().isEmpty()) {
            Set<RoleAccount> roles = account.getRoleAccounts();
            for (RoleAccount role : roles) {
                if (role.getRoleName().equals("ADMIN")) {
                    isAdmin = true;
                } else if (role.getRoleName().equals("USER")) {
                    isUser = true;
                }
            }
        }
        claims.put("accountId", account.getId());
        claims.put("avatar", account.getAvatar());
        claims.put("isAdmin", isAdmin);
        claims.put("isUser", isUser);
        claims.put("fullName", fullName);

        return createToken(claims, email);
    }

    // Tạo JWT với các claim đã chọn
    private String createToken(Map<String, Object> claims, String email) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 60 * 60 * 1000)) // JWT hết hạn sau 30 phút
                .signWith(SignatureAlgorithm.HS256, getSigneKey())
                .compact();
    }

    // Lấy serect key
    private byte[] getSigneKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes).getEncoded();
    }

    // Trích xuất thông tin
    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(getSigneKey()).parseClaimsJws(token).getBody();
    }

    // Trích xuất thông tin cho 1 claim
    public <T> T extractClaim(String token, Function<Claims, T> claimsTFunction) {
        final Claims claims = extractAllClaims(token);
        return claimsTFunction.apply(claims);
    }

    // Kiểm tra tời gian hết hạn từ JWT
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Kiểm tra tời gian hết hạn từ JWT
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Kiểm tra cái JWT đã hết hạn
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Kiểm tra tính hợp lệ
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        System.out.println(username);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
