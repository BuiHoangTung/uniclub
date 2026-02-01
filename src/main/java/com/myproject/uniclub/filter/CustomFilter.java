package com.myproject.uniclub.filter;

import com.myproject.uniclub.dto.AuthorityDTO;
import com.myproject.uniclub.utils.JwtHelper;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;


import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class CustomFilter extends OncePerRequestFilter {

    @Value("${jwts.key}")
    private String strKey;

    @Autowired
    private JwtHelper jwtHelper;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String authorHeader = request.getHeader("Authorization");

        if(authorHeader != null && authorHeader.startsWith("Bearer ") && authorHeader.length() > 7) {
            String token = authorHeader.substring(7);
            String data = this.jwtHelper.deCodeToken(token);

            if(data != null) {
                // Cách 1: Lưu ý không được chuyền vào một interface vì thư viện sẽ không biết được constructor để tạo ra một instance?
//                List<SimpleGrantedAuthority> authorities = this.objectMapper.readValue(data, new TypeReference<List<SimpleGrantedAuthority>>() {
//                });

                // Cách 2:
                List<AuthorityDTO> authorityDTOS = this.objectMapper.readValue(data, new TypeReference<List<AuthorityDTO>>() {});
                List<GrantedAuthority> authorities = new ArrayList<>();

                authorityDTOS.forEach(a -> {
                    SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(a.getAuthority());
                    authorities.add(simpleGrantedAuthority);
                });

                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                        new UsernamePasswordAuthenticationToken("", "", authorities);

                SecurityContext securityContext = SecurityContextHolder.getContext();
                securityContext.setAuthentication(usernamePasswordAuthenticationToken);

                System.out.println("Check authorities: " + authorities);
            }

            System.out.println("Check data: " + data);
        }

        filterChain.doFilter(request, response);
    }
}
