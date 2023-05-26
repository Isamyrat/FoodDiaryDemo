package com.food.diary.response.foodDiary;

import com.food.diary.DTO.FoodDiaryDTO;
import com.food.diary.response.BaseResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FoodDiaryResponse extends BaseResponse {
    private FoodDiaryDTO foodDiaryDTO;

    public FoodDiaryResponse(boolean successful,
                             String errorMessage,
                             String errorMessageCode,
                             FoodDiaryDTO foodDiaryDTO) {
        super(successful, errorMessage, errorMessageCode);
        this.foodDiaryDTO = foodDiaryDTO;
    }

    public FoodDiaryResponse(boolean successful, String errorMessage, String errorMessageCode) {
        super(successful, errorMessage, errorMessageCode);
    }
}
