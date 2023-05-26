package com.food.diary.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {
    private Long id;
    private String product;
    private Long width;
    private boolean flag = false;
    private String lastProductName = "";

    public ProductDTO(String product, Long width) {
        this.product = product;
        this.width = width;
    }
}
