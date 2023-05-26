package com.food.diary.service;

import com.food.diary.DTO.FoodDiaryDTO;
import com.food.diary.response.foodDiary.FoodDiaryListResponse;

public interface FoodDiaryService {
    FoodDiaryListResponse getByUserId(final Long userId, final int pageNumber, final int pageSize);
    FoodDiaryDTO addFoodDiary(final Long userId,final FoodDiaryDTO foodDiaryDTO);
    FoodDiaryDTO getFoodDiaryDTOById(final Long id);

    FoodDiaryDTO editFoodDiary(final Long foodDiaryId, FoodDiaryDTO foodDiaryDTO);
}
