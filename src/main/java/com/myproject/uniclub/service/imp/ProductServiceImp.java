package com.myproject.uniclub.service.imp;

import com.myproject.uniclub.dto.ProductDTO;
import com.myproject.uniclub.entity.*;
import com.myproject.uniclub.repository.ProductRepository;
import com.myproject.uniclub.repository.VariantRepository;
import com.myproject.uniclub.request.AddProductRequest;
import com.myproject.uniclub.service.FileService;
import com.myproject.uniclub.service.ProductService;
import jakarta.servlet.ServletContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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

    @Override
    public List<ProductDTO> getProducts() {
        return this.productRepository.findAll().stream()
                .map(item -> {
                    ProductDTO productDTO = new ProductDTO();

                    productDTO.setName(item.getName());
                    productDTO.setPrice(item.getPrice());
                    // http://localhost:8080/file/ +
                    // Tìm hiểu cách lấy động
                    if(!item.getVariantEntities().isEmpty()) {
                        productDTO.setImageUrl("http://localhost:8080/file/" + item.getVariantEntities().getFirst().getImages());
                    } else {
                        productDTO.setImageUrl("");
                    }

                    return productDTO;
                }).toList();
    }

}
