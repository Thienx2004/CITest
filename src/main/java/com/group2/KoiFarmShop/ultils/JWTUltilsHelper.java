package com.group2.KoiFarmShop.ultils;

import com.group2.KoiFarmShop.entity.Account;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JWTUltilsHelper {
    @Value("${jwt.secret}")
    private String jwtSecret;

    public String generateToken(Account data){
        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
        String jws = Jwts.builder()
                .subject(data.getEmail())
                .issuedAt(new Date())
                .claim("accountId", data.getAccountID()) // Account ID ở đây là int hoặc Integer
                .claim("scope",data.getRole().getRoleName())
                .signWith(key)
                .compact();

        return jws;
    }

    public String generateTokenForOTP(Account data){
        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
        String jws = Jwts.builder().subject(data.getEmail()).issuedAt(new Date()).claim("scope","true").signWith(key).compact();

        return jws;
    }

}
