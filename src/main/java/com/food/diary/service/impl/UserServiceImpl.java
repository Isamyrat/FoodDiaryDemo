package com.food.diary.service.impl;

import com.food.diary.DTO.LoginDTO;
import com.food.diary.DTO.UserDTO;
import com.food.diary.entity.UserEntity;
import com.food.diary.entity.enums.RoleEnums;
import com.food.diary.exception.NotFoundException;
import com.food.diary.repository.UserRepository;
import com.food.diary.response.user.UserListResponse;
import com.food.diary.security.CustomUserDetails;
import com.food.diary.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void createCurator(LoginDTO loginDTO) {
        UserEntity user = new UserEntity();
        user.setUsername(loginDTO.getUsername());
        user.setPassword(passwordEncoder.encode(loginDTO.getPassword()));
        user.setRole(RoleEnums.ROLE_CURATOR.toString());
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void createUser(LoginDTO loginDTO) {
        UserEntity user = new UserEntity();
        user.setUsername(loginDTO.getUsername());
        user.setPassword(passwordEncoder.encode(loginDTO.getPassword()));
        user.setRole(RoleEnums.ROLE_USER.toString());
        userRepository.save(user);
    }


    @Override
    public UserEntity getById(final Long id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("User with ID " + id + " not found"));
    }

    @Override
    public UserListResponse getAllUsers(final int pageNumber, final int pageSize, final String data) {
        int offset = (pageSize * pageNumber) - pageSize;
        List<UserEntity> userList = userRepository.getAllUsers(pageSize + 1, offset, data);
        List<UserDTO> userDTOList = userList.stream()
            .map(this::fillingToUserDTO)
            .toList();

        return doPager(userDTOList, pageSize);
    }

    @Override
    public UserListResponse getAllUsersWithRoleUser(final int pageNumber, final int pageSize, final String data) {
        int offset = (pageSize * pageNumber) - pageSize;
        List<UserEntity> userList =
            userRepository.getAllUserByRole(RoleEnums.ROLE_USER.toString(), pageSize + 1, offset, data);
        List<UserDTO> userDTOList = userList.stream()
            .map(this::fillingToUserDTO)
            .toList();

        return doPager(userDTOList, pageSize);
    }

    @Override
    public UserListResponse getAllUsersWithRoleCurator(final int pageNumber, final int pageSize, final String data) {
        int offset = (pageSize * pageNumber) - pageSize;
        List<UserEntity> userList =
            userRepository.getAllUserByRole(RoleEnums.ROLE_CURATOR.toString(), pageSize + 1, offset, data);
        List<UserDTO> userDTOList = userList.stream()
            .map(this::fillingToUserDTO)
            .toList();

        return doPager(userDTOList, pageSize);
    }

    @Override
    public UserListResponse doPager(List<UserDTO> userDTOList, final int pageSize) {
        boolean hasMore = false;
        if (userDTOList.size() == pageSize + 1) {
            userDTOList = userDTOList.stream()
                .limit(pageSize)
                .collect(Collectors.toList());
            hasMore = true;
        }
        return new UserListResponse(true, hasMore, userDTOList);
    }

    @Override
    public UserDTO fillingToUserDTO(final UserEntity userEntity) {
        return UserDTO.builder()
            .id(userEntity.getId())
            .username(userEntity.getUsername())
            .password(userEntity.getPassword())
            .role(userEntity.getRole())
            .build();
    }

    @Override
    public Optional<UserEntity> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public boolean hasUserWithUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public UserListResponse getAllUsersWithRoleUserNotInCurator(final long curatorId,
                                                                int pageNumber,
                                                                int pageSize,
                                                                String data) {
        int offset = (pageSize * pageNumber) - pageSize;
        List<UserEntity> userList =
            userRepository.getAllUsersByNotInCurator(curatorId, pageSize + 1, offset, data);
        List<UserDTO> userDTOList = userList.stream()
            .map(this::fillingToUserDTO)
            .toList();

        return doPager(userDTOList, pageSize);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = getUserByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException(String.format("Username %s not found", username)));
        List<SimpleGrantedAuthority> authorities =
            Collections.singletonList(new SimpleGrantedAuthority(user.getRole()));
        return mapUserToCustomUserDetails(user, authorities);
    }

    private CustomUserDetails mapUserToCustomUserDetails(UserEntity user, List<SimpleGrantedAuthority> authorities) {
        CustomUserDetails customUserDetails = new CustomUserDetails();
        customUserDetails.setId(user.getId());
        customUserDetails.setUsername(user.getUsername());
        customUserDetails.setPassword(user.getPassword());
        customUserDetails.setAuthorities(authorities);
        return customUserDetails;
    }
}
