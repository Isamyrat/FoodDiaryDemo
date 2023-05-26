package com.food.diary.service.impl;

import com.food.diary.DTO.UserDTO;
import com.food.diary.entity.CuratorUserEntity;
import com.food.diary.entity.UserEntity;
import com.food.diary.exception.BadRequestException;
import com.food.diary.repository.CuratorUserRepository;
import com.food.diary.response.user.UserListResponse;
import com.food.diary.service.CuratorUserService;
import com.food.diary.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CuratorUserServiceImpl implements CuratorUserService {
    private final CuratorUserRepository curatorUserRepository;
    private final UserService userService;

    @Override
    public UserListResponse getAllUsersByCurator(final Long id,final int pageNumber, final int pageSize, final String data) {
        int offset = (pageSize * pageNumber) - pageSize;
        List<CuratorUserEntity> userList = curatorUserRepository.getAllUsersByCurator(id, pageSize + 1, offset, data);
        List<UserDTO> userDTOList = new ArrayList<>();
        for (CuratorUserEntity curatorUserEntity : userList) {
            userDTOList.add(userService.fillingToUserDTO(curatorUserEntity.getUser()));
        }
        return userService.doPager(userDTOList, pageSize);
    }

    @Override
    @Transactional
    public void addUserToCurator(final Long curatorId, final Long userId) {
        UserEntity curator = userService.getById(curatorId);
        UserEntity user = userService.getById(userId);
        Optional<CuratorUserEntity> curatorUserEntityOptional =
            curatorUserRepository.isUserInCuratorUserList(curator.getId(), user.getId());

        if (curatorUserEntityOptional.isPresent()) {
            log.error("IN addUserToCurator: Curator already have this user");
            throw new BadRequestException("You already have this user:" + user.getUsername());
        }

        CuratorUserEntity curatorUserEntity = new CuratorUserEntity();
        curatorUserEntity.setCurator(curator);
        curatorUserEntity.setUser(user);
        curatorUserRepository.save(curatorUserEntity);
    }

    @Override
    @Transactional
    public void removeUserFromCuratorUserList(final Long curatorId, final Long userId) {
        UserEntity curator = userService.getById(curatorId);
        UserEntity user = userService.getById(userId);
        Optional<CuratorUserEntity> curatorUserEntityOptional =
            curatorUserRepository.isUserInCuratorUserList(curator.getId(), user.getId());

        if (curatorUserEntityOptional.isEmpty()) {
            log.error("IN removeUserFromCuratorUserList: Curator you do not have this user");
            throw new BadRequestException("You you do not have this user:" + user.getUsername());
        }

        curatorUserRepository.delete(curatorUserEntityOptional.get());
    }
}
