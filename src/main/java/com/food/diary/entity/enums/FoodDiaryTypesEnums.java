package com.food.diary.entity.enums;

public enum FoodDiaryTypesEnums {
    BREAKFAST("BREAKFAST"),
    FIRST_SNACK("FIRST_SNACK"),
    LUNCH("LUNCH"),
    SECOND_SNACK("SECOND_SNACK"),
    DINNER("DINNER");

    private final String str;
    FoodDiaryTypesEnums(String str) {
        this.str = str;
    }

    @Override
    public String toString() {
        return str;
    }
}
