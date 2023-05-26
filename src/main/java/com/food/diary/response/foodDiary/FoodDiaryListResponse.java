package com.food.diary.response.foodDiary;

import com.food.diary.DTO.FoodDiaryDTO;
import com.food.diary.response.ListResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FoodDiaryListResponse extends ListResponse {
    List<FoodDiaryDTO> foodDiaryDTOList;

    public FoodDiaryListResponse(boolean successful, boolean hasMore, List<FoodDiaryDTO> foodDiaryDTOList) {
        super(successful, hasMore);
        this.foodDiaryDTOList = foodDiaryDTOList;
    }
}
