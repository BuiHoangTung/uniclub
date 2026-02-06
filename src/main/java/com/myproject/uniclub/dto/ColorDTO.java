package com.myproject.uniclub.dto;

import lombok.Data;

import java.util.List;

@Data
public class ColorDTO {
    private int id;
    private String name;
    private String images;
    private List<SizeDTO> sizes;
}
