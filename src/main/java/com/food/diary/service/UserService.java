package com.food.diary.service;

import com.food.diary.DTO.LoginDTO;
import com.food.diary.DTO.UserDTO;
import com.food.diary.entity.UserEntity;
import com.food.diary.response.user.UserListResponse;

import java.util.List;
import java.util.Optional;

public interface UserService {
    void createCurator(final LoginDTO loginDTO);

    void createUser(final LoginDTO loginDTO);

    UserEntity getById(final Long id);

    UserListResponse getAllUsers(final int pageNumber, final int pageSize, final String data);

    UserListResponse getAllUsersWithRoleUser(final int pageNumber, final int pageSize, final String data);

    UserListResponse getAllUsersWithRoleCurator(final int pageNumber, final int pageSize, final String data);
    UserDTO fillingToUserDTO(final UserEntity userEntity);
    UserListResponse doPager(List<UserDTO> userDTOList, final int pageSize);
    Optional<UserEntity> getUserByUsername(String username);
    boolean hasUserWithUsername(String username);

    UserListResponse getAllUsersWithRoleUserNotInCurator(final long curatorId, final int pageNumber, final int pageSize, final String data);
}
