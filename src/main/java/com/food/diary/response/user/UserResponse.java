package com.food.diary.response.user;

import com.food.diary.DTO.UserDTO;
import com.food.diary.response.BaseResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponse extends BaseResponse {
    UserDTO userDTO;

    public UserResponse(boolean successful, String errorMessage, String errorMessageCode, UserDTO userDTO) {
        super(successful, errorMessage, errorMessageCode);
        this.userDTO = userDTO;
    }
}
