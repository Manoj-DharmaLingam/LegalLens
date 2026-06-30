package com.LegalLens.backend.security;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {
    
    private static final String key = "lfefnsjhgfhduignfkdgfkdigfribbfjbhgkubfdjb";
    private static final SecretKey mykey = Keys.hmacShaKeyFor(key.getBytes());

    public String GenerateToken(UserDetails userDetails){
        Map<String,Object> claims = new HashMap<>();
        claims.put("role",userDetails.getAuthorities());
        return createToken(claims,userDetails.getUsername());
        
    }

     private String createToken(Map<String,Object> claims , String username){
        return Jwts.builder()
            .claims(claims)
            .subject(username)
            .issuedAt(new Date())
            .expiration(new Date(System.currentTimeMillis()+1000*60*60))
            .signWith(mykey)
            .compact();
     }

}
