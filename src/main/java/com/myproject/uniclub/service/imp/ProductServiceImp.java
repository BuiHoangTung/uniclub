package com.myproject.uniclub.service.imp;

import com.myproject.uniclub.entity.*;
import com.myproject.uniclub.repository.ProductRepository;
import com.myproject.uniclub.repository.VariantRepository;
import com.myproject.uniclub.request.AddProductRequest;
import com.myproject.uniclub.service.FileService;
import com.myproject.uniclub.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ProductServiceImp implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private VariantRepository variantRepository;

    @Autowired
    private FileService fileService;

    @Transactional
    @Override
    public void addProduct(AddProductRequest request) {
        ProductEntity productEntity = new ProductEntity();
        BrandEntity brand = new BrandEntity();
        ColorEntity colorEntity = new ColorEntity();
        SizeEntity sizeEntity = new SizeEntity();
        MultipartFile file = request.files();

        brand.setId(request.idBrand());
        colorEntity.setId(request.idColor());
        sizeEntity.setId(request.idSize());

        productEntity.setName(request.name());
        productEntity.setDescription(request.desc());
        productEntity.setInformation(request.information());
        productEntity.setPrice(request.price());
        productEntity.setBrand(brand);

        ProductEntity createdProduct =  this.productRepository.save(productEntity);
        VariantEntity variantEntity = new VariantEntity();

        variantEntity.setProduct(createdProduct);
        variantEntity.setColor(colorEntity);
        variantEntity.setSize(sizeEntity);
        variantEntity.setQuantity(request.quantity());
        variantEntity.setImages(file.getOriginalFilename());

        this.fileService.saveFile(file);
        this.variantRepository.save(variantEntity);
    }

}
