package com.food.diary.service.impl;

import com.food.diary.DTO.FoodDiaryDTO;
import com.food.diary.DTO.ProductDTO;
import com.food.diary.entity.FoodDiaryEntity;
import com.food.diary.entity.ProductEntity;
import com.food.diary.exception.NotFoundException;
import com.food.diary.repository.FoodDiaryRepository;
import com.food.diary.repository.ProductRepository;
import com.food.diary.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final FoodDiaryRepository foodDiaryRepository;

    @Override
    public void addProduct(Long foodDiaryId, ProductDTO productDTO) {
        ProductEntity productEntity = new ProductEntity();
        productEntity.setProduct(productDTO.getProduct());
        productEntity.setWidth(productDTO.getWidth());
        productEntity.setActive(true);
        productEntity.setProductDiaryEntity(getById(foodDiaryId));
        productRepository.save(productEntity);
    }

    @Override
    public void editProduct(Long foodDiaryId, ProductDTO productDTO) {
        ProductEntity productEntity = getProductById(productDTO.getId());

        if (!productEntity.getProduct().equals(productDTO.getProduct()) ||
            !productEntity.getWidth().equals(productDTO.getWidth())) {
            productEntity.setActive(false);
            productRepository.save(productEntity);

            ProductEntity productEntitySecond = new ProductEntity();
            productEntitySecond.setProduct(productDTO.getProduct());
            productEntitySecond.setWidth(productDTO.getWidth());
            productEntitySecond.setActive(true);
            productEntitySecond.setProductDiaryEntity(getById(foodDiaryId));
            productRepository.save(productEntitySecond);
        }
    }

    @Override
    public List<ProductDTO> getAllLikeProductName(Long userId, String productName) {
        List<String> stringList = productRepository.getAllLikeProductName(userId, productName);
        List<ProductDTO> productDTOList = new ArrayList<>();
        for (String string : stringList) {
            ProductDTO productDTO = new ProductDTO();
            productDTO.setProduct(string);
            productDTOList.add(productDTO);
        }

        return
            productDTOList;
    }

    @Override
    public List<ProductDTO> getAllByFoodDiaryId(final Long id) {
        return
            productRepository.getAllByFoodDiaryId(id)
                .stream()
                .map(this::fillingDTOByEntity)
                .collect(Collectors.toList());
    }

    private ProductDTO fillingDTOByEntity(final ProductEntity productEntity) {
        return ProductDTO.builder()
            .id(productEntity.getId())
            .product(productEntity.getProduct())
            .width(productEntity.getWidth())
            .build();
    }

    private FoodDiaryEntity getById(final Long id) {
        return foodDiaryRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Food Diary with ID " + id + " not found"));
    }

    private ProductEntity getProductById(final Long id) {
        return productRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Product with ID " + id + " not found"));
    }

    private FoodDiaryDTO fillingDTOByEntity(final FoodDiaryEntity foodDiaryEntity) {
        return FoodDiaryDTO.builder()
            .id(foodDiaryEntity.getId())
            .type(foodDiaryEntity.getType())
            .water(foodDiaryEntity.getWater())
            .createdDate(foodDiaryEntity.getCreatedDate())
            .emotionalCondition(foodDiaryEntity.getEmotionalCondition())
            .physicalState(foodDiaryEntity.getPhysicalState())
            .productDTOList(getAllByFoodDiaryId(foodDiaryEntity.getId()))
            .build();
    }
}
