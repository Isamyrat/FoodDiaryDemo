package com.food.diary.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@Getter
@NoArgsConstructor
@ToString
public class FoodDiaryDTO {
    private Long id;
    private String type;
    private Date createdDate;
    private Long water;
    private String emotionalCondition;
    private String physicalState;
    private String date;
    private String product = "";
    private Long width = 0L;
    List<ProductDTO> productDTOList;
}
