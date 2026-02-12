package com.myproject.uniclub.service.imp;

import com.myproject.uniclub.dto.ColorDTO;
import com.myproject.uniclub.dto.ProductDTO;
import com.myproject.uniclub.dto.SizeDTO;
import com.myproject.uniclub.entity.*;
import com.myproject.uniclub.repository.ProductRepository;
import com.myproject.uniclub.repository.VariantRepository;
import com.myproject.uniclub.request.AddProductRequest;
import com.myproject.uniclub.service.ColorService;
import com.myproject.uniclub.service.FileService;
import com.myproject.uniclub.service.ProductService;
import com.myproject.uniclub.service.SizeService;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

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

    @Autowired
    private SizeService sizeService;

    @Autowired
    private ColorService colorService;

    @Autowired
    private RedisTemplate redisTemplate;

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

                    productDTO.setId(item.getId());
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
//    @Cacheable("productDetail")
    public ProductDTO getDetailProduct(int id) {
//        System.out.println("Check detail page");
        ObjectMapper objectMapper = new ObjectMapper();
        ProductDTO productDto = new ProductDTO();

        if(redisTemplate.hasKey(String.valueOf(id))) {
            String data = redisTemplate.opsForValue().get(String.valueOf(id)).toString();
            productDto = objectMapper.readValue(data, ProductDTO.class);

            System.out.println("Check has key");
        } else {
            System.out.println("Chek no key");

            Optional<ProductEntity> productEntityOptional = this.productRepository.findById(id);
//                .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại"));

            productDto = productEntityOptional.map(item -> {
                ProductDTO productDTO = new ProductDTO();

                productDTO.setId(item.getId());
                productDTO.setName(item.getName());
                productDTO.setPrice(item.getPrice());
                productDTO.setOverview(item.getDescription());
                productDTO.setCategories(item.getProductCategoryEntities().stream().map(
                        productCategory -> productCategory.getCategory().getName()
                ).toList());

//            productDTO.setSizes(item.getVariantEntities().stream().map(variant -> {
//                SizeDTO sizeDTO = new SizeDTO();
//
//                sizeDTO.setId(variant.getSize().getId());
//                sizeDTO.setName(variant.getSize().getName());
//
//                return sizeDTO;
//            }).toList());

                productDTO.setSizes(this.sizeService.getAllSize().stream().map(size -> {
                    SizeDTO sizeDTO = new SizeDTO();

                    sizeDTO.setId(size.getId());
                    sizeDTO.setName(size.getName());

                    return sizeDTO;
                }).toList());

                productDTO.setPriceColorSize(item.getVariantEntities().stream().map(variant -> {
                    ColorDTO colorDTO = new ColorDTO();

                    colorDTO.setId(variant.getColor().getId());
                    colorDTO.setImages(variant.getImages());
                    colorDTO.setSizes(item.getVariantEntities().stream().map(variant1 -> {
                        SizeDTO sizeDTO = new SizeDTO();

                        sizeDTO.setId(variant1.getSize().getId());
                        sizeDTO.setName((variant1.getSize().getName()));
                        sizeDTO.setQuantity(variant1.getQuantity());
                        sizeDTO.setPrice(variant.getPrice());

                        return sizeDTO;
                    }).toList());

                    return colorDTO;
                }).toList());

                productDTO.setColors(this.colorService.getAllColors().stream().map((color) -> {
                    ColorDTO colorDTO = new ColorDTO();

                    colorDTO.setId(color.getId());
                    colorDTO.setName(color.getName());

                    return colorDTO;
                }).toList());

                return productDTO;
            }).orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại"));

            try {
                String jsonProduct = objectMapper.writeValueAsString(productDto);
                redisTemplate.opsForValue().set(String.valueOf(id), jsonProduct);
            } catch(JacksonException e) {
                throw new RuntimeException("Lỗi parse json");
            }
        }

        return productDto;
    }

}
