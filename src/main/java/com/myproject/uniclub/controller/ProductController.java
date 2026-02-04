package com.myproject.uniclub.controller;

import com.myproject.uniclub.request.AddProductRequest;
import com.myproject.uniclub.service.FileService;
import com.myproject.uniclub.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping
    public ResponseEntity<?> addProduct(AddProductRequest request) {
        this.productService.addProduct(request);

        return new ResponseEntity<>("Hello add product", HttpStatus.OK);
    }

}
