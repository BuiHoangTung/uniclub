package com.myproject.uniclub.service;

import com.myproject.uniclub.dto.ProductDTO;
import com.myproject.uniclub.request.AddProductRequest;

import java.util.List;

public interface ProductService {
    void addProduct(AddProductRequest request);
    List<ProductDTO> getProducts();
}
