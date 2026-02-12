package com.myproject.uniclub.service.imp;

import com.myproject.uniclub.entity.SizeEntity;
import com.myproject.uniclub.repository.SizeRepository;
import com.myproject.uniclub.service.SizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SizeServiceImp implements SizeService {

    @Autowired
    private SizeRepository sizeRepository;

    @Override
    public List<SizeEntity> getAllSize() {
        return this.sizeRepository.findAll();
    }
}
