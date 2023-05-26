package com.food.diary.entity.enums;

public enum RoleEnums {
    ROLE_ADMIN("ROLE_ADMIN"),
    ROLE_CURATOR("ROLE_CURATOR"),
    ROLE_USER("ROLE_USER");

    private final String str;
    RoleEnums(String str) {
        this.str = str;
    }

    @Override
    public String toString() {
        return str;
    }
}
