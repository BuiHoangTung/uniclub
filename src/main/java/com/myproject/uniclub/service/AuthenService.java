package com.myproject.uniclub.service;

import com.myproject.uniclub.dto.RoleDTO;
import com.myproject.uniclub.request.AuthenRequest;

import java.util.List;

public interface AuthenService {
    List<RoleDTO> checkLogin(AuthenRequest authenRequest);
}
