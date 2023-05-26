package com.food.diary.response.product;

import com.food.diary.DTO.ProductDTO;
import com.food.diary.response.ListResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProductListResponse extends ListResponse {
    private List<ProductDTO> productDTOList;

    public ProductListResponse(boolean successful, boolean hasMore, List<ProductDTO> productDTOList) {
        super(successful, hasMore);
        this.productDTOList = productDTOList;
    }

    public ProductListResponse(List<ProductDTO> productDTOList) {
        this.productDTOList = productDTOList;
    }
}
