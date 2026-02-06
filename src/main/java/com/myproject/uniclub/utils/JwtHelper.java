package com.myproject.uniclub.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Component
public class JwtHelper {

    @Value("${jwts.key}")
    private String strKey;

    private final int EXPIRATION_TIME = 24 * 60 * 60 * 1000;

    // Dùng LocalDateTime để thao tác với thời gian như: So sánh thời gian, ...

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(strKey));
    }

    public String generateToken(String data) {
        // Biến key kiểu String đã lưu trữ trước đó thành SecretKey
        SecretKey secretKey = this.getSecretKey();
        Date currentDate = new Date();
        long millisecondFutureTime = currentDate.getTime() + this.EXPIRATION_TIME;

        String token = Jwts.builder()
                .signWith(secretKey)
                .subject(data)
                .expiration(new Date(millisecondFutureTime))
                .compact(); // Add expiration time

        return token;
    }

    public String deCodeToken(String token) {
        SecretKey secretKey = this.getSecretKey();
        return Jwts.parser()
                .verifyWith(secretKey).build()
                .parseSignedClaims(token)
                .getPayload().getSubject();
    }

}
