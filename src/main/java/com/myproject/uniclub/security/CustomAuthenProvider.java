package com.myproject.uniclub.security;

import com.myproject.uniclub.dto.RoleDTO;
import com.myproject.uniclub.request.AuthenRequest;
import com.myproject.uniclub.service.AuthenService;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CustomAuthenProvider implements AuthenticationProvider {

    @Autowired
    private AuthenService authenService;

    @Override
    public @Nullable Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();
        AuthenRequest authenRequest = new AuthenRequest(username, password);
        List<RoleDTO> roleDTOS = authenService.checkLogin(authenRequest);

        if(roleDTOS != null && !roleDTOS.isEmpty()) {
            List<SimpleGrantedAuthority> simpleGrantedAuthorities = roleDTOS.stream().map(roleDTO -> {
                return new SimpleGrantedAuthority(roleDTO.getName());
            }).toList();

            return new UsernamePasswordAuthenticationToken("", "", simpleGrantedAuthorities);
        } else {
            return null;
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {

        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
