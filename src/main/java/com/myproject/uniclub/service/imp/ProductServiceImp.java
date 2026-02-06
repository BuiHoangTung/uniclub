package com.myproject.uniclub.service.imp;

import com.myproject.uniclub.dto.ColorDTO;
import com.myproject.uniclub.dto.ProductDTO;
import com.myproject.uniclub.dto.SizeDTO;
import com.myproject.uniclub.entity.*;
import com.myproject.uniclub.repository.ProductRepository;
import com.myproject.uniclub.repository.VariantRepository;
import com.myproject.uniclub.request.AddProductRequest;
import com.myproject.uniclub.service.FileService;
import com.myproject.uniclub.service.ProductService;
import jakarta.servlet.ServletContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

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
    public List<ProductDTO> getProducts(int page) {
        Pageable pageable = PageRequest.of(page, 4); // (int pageNumber, int pageSize);

        return this.productRepository.findAll(pageable).stream()
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

    @Override
    public ProductDTO getDetailProduct(int id) {
        Optional<ProductEntity> productEntityOptional = this.productRepository.findById(id);
//                .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại"));

        return productEntityOptional.map(item -> {
            ProductDTO productDTO = new ProductDTO();

            productDTO.setId(item.getId());
            productDTO.setName(item.getName());
            productDTO.setCategories(item.getProductCategoryEntities().stream().map(
                    productCategory -> productCategory.getCategory().getName()
            ).toList());

            productDTO.setSizes(item.getVariantEntities().stream().map(variant -> {
                SizeDTO sizeDTO = new SizeDTO();

                sizeDTO.setId(variant.getSize().getId());
                sizeDTO.setName(variant.getSize().getName());

                return sizeDTO;
            }).toList());

            productDTO.setColors(item.getVariantEntities().stream().map(variant -> {
                ColorDTO colorDTO = new ColorDTO();

                colorDTO.setImages(variant.getImages());
                colorDTO.setSizes(item.getVariantEntities().stream().map(variant1 -> {
                    SizeDTO sizeDTO = new SizeDTO();

                    sizeDTO.setId(variant1.getSize().getId());
                    sizeDTO.setName((variant1.getSize().getName()));
                    sizeDTO.setQuantity(variant1.getQuantity());
                    sizeDTO.setPrice(variant1.getPrice());

                    return sizeDTO;
                }).toList());

                return colorDTO;
            }).toList());

            return productDTO;
        }).orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại"));
    }

}
