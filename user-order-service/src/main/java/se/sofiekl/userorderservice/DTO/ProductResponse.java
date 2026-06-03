package se.sofiekl.userorderservice.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.math.BigDecimal;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductResponse {
    private Integer id;
    private String title;
    private BigDecimal price;
}