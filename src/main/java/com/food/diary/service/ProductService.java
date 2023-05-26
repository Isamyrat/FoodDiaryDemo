package com.food.diary.service;

import com.food.diary.DTO.ProductDTO;

import java.util.List;

public interface ProductService {
    List<ProductDTO> getAllByFoodDiaryId(final Long id);
    void addProduct(final Long foodDiaryId, final ProductDTO productDTO);
    void editProduct(Long foodDiaryId, final ProductDTO productDTO);
    List<ProductDTO> getAllLikeProductName(final Long userId, final String productName);
}
