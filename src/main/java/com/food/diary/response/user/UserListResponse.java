package com.food.diary.response.user;

import com.food.diary.DTO.UserDTO;
import com.food.diary.response.ListResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserListResponse extends ListResponse {
    List<UserDTO> userList;

    public UserListResponse(boolean successful, boolean hasMore, List<UserDTO> userList) {
        super(successful, hasMore);
        this.userList = userList;
    }
}
