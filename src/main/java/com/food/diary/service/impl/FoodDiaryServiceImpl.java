package com.food.diary.service.impl;

import com.food.diary.DTO.FoodDiaryDTO;
import com.food.diary.DTO.ProductDTO;
import com.food.diary.entity.FoodDiaryEntity;
import com.food.diary.entity.UserEntity;
import com.food.diary.exception.NotFoundException;
import com.food.diary.repository.FoodDiaryRepository;
import com.food.diary.response.foodDiary.FoodDiaryListResponse;
import com.food.diary.service.FoodDiaryService;
import com.food.diary.service.ProductService;
import com.food.diary.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FoodDiaryServiceImpl implements FoodDiaryService {
    public final  static SimpleDateFormat FOOD_DIARY_DATE_FORMATTER =new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private final ProductService productService;
    private final FoodDiaryRepository foodDiaryRepository;
    private final UserService userService;
    @Override
    public FoodDiaryListResponse getByUserId(Long userId, int pageNumber, int pageSize) {
        int offset = (pageSize * pageNumber) - pageSize;
        List<FoodDiaryEntity> foodDiaryEntityList = foodDiaryRepository.getAllByUserId(userId, pageSize + 1, offset);
        List<FoodDiaryDTO> foodDiaryDTOList = foodDiaryEntityList.stream()
            .map(this::fillingDTOByEntity)
            .toList();

        return doPager(foodDiaryDTOList, pageSize);
    }

    @Override
    public FoodDiaryDTO addFoodDiary(final Long userId, final FoodDiaryDTO foodDiaryDTO) {

        UserEntity userEntity = userService.getById(userId);
        FoodDiaryEntity foodDiaryEntity = new FoodDiaryEntity();
        foodDiaryEntity.setUser(userEntity);
        foodDiaryEntity.setType(foodDiaryDTO.getType());
        foodDiaryEntity.setWater(foodDiaryDTO.getWater());
        foodDiaryEntity.setCreatedDate(getDate(foodDiaryDTO, FOOD_DIARY_DATE_FORMATTER));
        foodDiaryEntity.setEmotionalCondition(foodDiaryDTO.getEmotionalCondition());
        foodDiaryEntity.setPhysicalState(foodDiaryDTO.getPhysicalState());
        Long foodDiaryId = foodDiaryRepository.save(foodDiaryEntity).getId();
        ProductDTO productDTO = new ProductDTO();
        productDTO.setProduct(foodDiaryDTO.getProduct());
        productDTO.setWidth(foodDiaryDTO.getWidth());
        productService.addProduct(foodDiaryId, productDTO);

        FoodDiaryEntity foodDiaryEntity1 = getById(foodDiaryId);
        return fillingDTOByEntity(foodDiaryEntity1);
    }

    private Date getDate(FoodDiaryDTO foodDiaryDTO, SimpleDateFormat simpleDateFormat) {
        Date date1 = null;
        try {
            date1 = simpleDateFormat.parse(foodDiaryDTO.getDate());
        }catch (ParseException parseException){
            System.out.println("Parse error");
        }
        return date1;
    }

    @Override
    public FoodDiaryDTO getFoodDiaryDTOById(Long id) {
        return fillingDTOByEntity(getById(id));
    }

    @Override
    public FoodDiaryDTO editFoodDiary(Long foodDiaryId, FoodDiaryDTO foodDiaryDTO) {
        FoodDiaryEntity foodDiaryEntity = getById(foodDiaryId);
        foodDiaryEntity.setType(foodDiaryDTO.getType());

        Date date = getDate(foodDiaryDTO, FOOD_DIARY_DATE_FORMATTER);
        if(!foodDiaryEntity.getCreatedDate().equals(date)){
            foodDiaryEntity.setCreatedDate(date);
        }

        foodDiaryEntity.setWater(foodDiaryDTO.getWater());
        foodDiaryEntity.setEmotionalCondition(foodDiaryDTO.getEmotionalCondition());
        foodDiaryEntity.setPhysicalState(foodDiaryDTO.getPhysicalState());
        foodDiaryRepository.save(foodDiaryEntity);

        if(!foodDiaryDTO.getProduct().equals("") && !foodDiaryDTO.getWidth().equals(0L)){
            productService.addProduct(foodDiaryId, new ProductDTO(foodDiaryDTO.getProduct(), foodDiaryDTO.getWidth()));
        }

        for(ProductDTO productDTO : foodDiaryDTO.getProductDTOList()){
            productService.editProduct(foodDiaryId, productDTO);
        }

        return fillingDTOByEntity(foodDiaryEntity);
    }

    private FoodDiaryListResponse doPager(List<FoodDiaryDTO> foodDiaryDTOList, final int pageSize) {
        boolean hasMore = false;
        if (foodDiaryDTOList.size() == pageSize + 1) {
            foodDiaryDTOList = foodDiaryDTOList.stream()
                .limit(pageSize)
                .collect(Collectors.toList());
            hasMore = true;
        }
        return new FoodDiaryListResponse(true, hasMore, foodDiaryDTOList);
    }

    private FoodDiaryEntity getById(Long id){
        return foodDiaryRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Not found"));
    }

    private FoodDiaryDTO fillingDTOByEntity(final FoodDiaryEntity foodDiaryEntity) {
        List<ProductDTO> productDTOList = productService.getAllByFoodDiaryId(foodDiaryEntity.getId());
        return FoodDiaryDTO.builder()
            .id(foodDiaryEntity.getId())
            .type(foodDiaryEntity.getType())
            .water(foodDiaryEntity.getWater())
            .createdDate(foodDiaryEntity.getCreatedDate())
            .emotionalCondition(foodDiaryEntity.getEmotionalCondition())
            .physicalState(foodDiaryEntity.getPhysicalState())
            .productDTOList(productDTOList)
            .build();
    }
}
