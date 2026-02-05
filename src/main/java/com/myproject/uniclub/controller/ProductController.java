package com.myproject.uniclub.controller;

import com.myproject.uniclub.dto.ProductDTO;
import com.myproject.uniclub.request.AddProductRequest;
import com.myproject.uniclub.response.BaseResponse;
import com.myproject.uniclub.service.FileService;
import com.myproject.uniclub.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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

    @GetMapping
    public ResponseEntity<?> getProduct() {
        BaseResponse response = new BaseResponse();

        response.setStatusCode(200);
        response.setMessage("Success.");
        response.setData(this.productService.getProducts());

        return ResponseEntity.ok(response);
    }

}
