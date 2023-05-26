package com.food.diary.DTO;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class UserDTO {
    private Long id;
    private String username;
    private String password;
    private String role;
}
