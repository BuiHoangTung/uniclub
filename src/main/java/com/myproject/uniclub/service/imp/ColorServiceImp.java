package com.myproject.uniclub.service.imp;

import com.myproject.uniclub.entity.ColorEntity;
import com.myproject.uniclub.repository.ColorRepository;
import com.myproject.uniclub.service.ColorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ColorServiceImp implements ColorService {

    @Autowired
    private ColorRepository colorRepository;

    @Override
    public List<ColorEntity> getAllColors() {
        return this.colorRepository.findAll();
    }
}
