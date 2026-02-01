package com.myproject.uniclub.controller;

import com.myproject.uniclub.request.AuthenRequest;
import com.myproject.uniclub.response.BaseResponse;
import com.myproject.uniclub.service.AuthenService;
import com.myproject.uniclub.utils.JwtHelper;
import io.jsonwebtoken.io.Encoders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tools.jackson.databind.ObjectMapper;

import javax.crypto.SecretKey;
import java.util.List;

@RestController
@RequestMapping("/authen")
public class AuthController {

    @Autowired
    private AuthenService authenService;

    @Autowired
    private JwtHelper jwtHelper;

    @Autowired
    private AuthenticationManager authenticationManager;

    private ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping
    public ResponseEntity<?> authen(@RequestBody AuthenRequest authenRequest) {
//        // Tạo key:
//        SecretKey secretKey = Jwts.SIG.HS256.key().build();
//
//        // Biến key thành chuỗi để lưu trữ:
//        String key = Encoders.BASE64.encode(secretKey.getEncoded());

//        boolean isSuccess = this.authenService.checkLogin(authenRequest);
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                = new UsernamePasswordAuthenticationToken(authenRequest.email(), authenRequest.password());

        Authentication authentication = this.authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        /*
        * Đoạn code trên sẽ trả về UsernamePasswordAuthenticationToken trong CustomAuthenProvider.java
        * và sau đó đoạn code bên dưới (sinh token jwt) sẽ chạy còn nếu đoạn trên trả về null
        * Spring Security sẽ auto throw exception
        */

        List<GrantedAuthority> roleList = (List<GrantedAuthority>) authentication.getAuthorities();
        System.out.println(roleList);
        String data = this.objectMapper.writeValueAsString(roleList);

        String token = this.jwtHelper.generateToken(data);

        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setData(token);

        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }

}
