package com.myproject.uniclub.service.imp;

import com.myproject.uniclub.dto.RoleDTO;
import com.myproject.uniclub.entity.RoleEntity;
import com.myproject.uniclub.entity.UserEntity;
import com.myproject.uniclub.repository.UserRepository;
import com.myproject.uniclub.request.AuthenRequest;
import com.myproject.uniclub.service.AuthenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AuthenServiceImp implements AuthenService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public List<RoleDTO> checkLogin(AuthenRequest request) {
        List<RoleDTO> roleDTOS = new ArrayList<>();

        UserEntity user = this.userRepository
                .findUserEntityByEmail(request.email());

        if(user != null && this.passwordEncoder.matches(request.password(), user.getPassword())) {
            RoleEntity roleEntity = user.getRoleEntity();
            RoleDTO roleDTO = new RoleDTO();

            roleDTO.setId(roleEntity.getId());
            roleDTO.setName(roleEntity.getName());

            roleDTOS.add(roleDTO);
        }

        return roleDTOS;
    }

}
