package com.myproject.uniclub.service;

import com.myproject.uniclub.dto.ProductDTO;
import com.myproject.uniclub.request.AddProductRequest;

import java.util.List;
import java.util.Optional;

public interface ProductService {

    void addProduct(AddProductRequest request);

    List<ProductDTO> getProducts(int page);

    ProductDTO getDetailProduct(int id);

}
