package com.myproject.uniclub.dto;

import lombok.Data;

import java.util.List;

@Data
public class ProductDTO {
    private int id;
    private String name;
    private String imageUrl;
    private double price;
    private String overview;
    private List<ColorDTO> priceColorSize;
    private List<String> categories;
    private List<String> tags;
    private List<SizeDTO> sizes;
    private List<ColorDTO> colors;
}
