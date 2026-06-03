package se.sofiekl.productservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.math.BigDecimal;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductDTO {
    private Integer id;
    private String title;
    private BigDecimal price;
    private String category;
    private String description;
    private String image;
}