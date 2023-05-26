package com.food.diary.controller;

import com.food.diary.exception.NotFoundException;
import com.food.diary.response.BaseResponse;
import com.food.diary.response.user.UserListResponse;
import com.food.diary.security.CustomUserDetails;
import com.food.diary.service.CuratorUserService;
import com.food.diary.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/user/")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final CuratorUserService curatorUserService;

    @GetMapping(value = "/show/all")
    public ResponseEntity<UserListResponse> showListAll(@RequestParam(defaultValue = "1") int page,
                                                        @RequestParam(defaultValue = "10") int size,
                                                        @RequestParam(value = "data",
                                                            defaultValue = "") String data) {
        UserListResponse listResponse = userService.getAllUsers(page, size, data);
        return ResponseEntity.ok(fillingPageable(listResponse, page, size, data));
    }

    @GetMapping(value = "/show/all/users")
    public ResponseEntity<UserListResponse> showListUsers(@RequestParam(defaultValue = "1") int page,
                                                          @RequestParam(defaultValue = "10") int size,
                                                          @RequestParam(value = "data",
                                                              defaultValue = "") String data) {
        UserListResponse listResponse = userService.getAllUsersWithRoleUser(page, size, data);
        return ResponseEntity.ok(fillingPageable(listResponse, page, size, data));
    }

    @GetMapping(value = "/show/all/curators")
    public ResponseEntity<UserListResponse> showListCurators(@RequestParam(defaultValue = "1") int page,
                                                             @RequestParam(defaultValue = "10") int size,
                                                             @RequestParam(value = "data",
                                                                 defaultValue = "") String data) {
        UserListResponse listResponse = userService.getAllUsersWithRoleCurator(page, size, data);
        return ResponseEntity.ok(fillingPageable(listResponse, page, size, data));
    }

    @GetMapping(value = "/show/all/curator/users")
    public ResponseEntity<UserListResponse> showListCuratorUsers(
        @RequestParam(defaultValue = "1") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(value = "data",
            defaultValue = "") String data) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

        UserListResponse listResponse =
            curatorUserService.getAllUsersByCurator(customUserDetails.getId(), page, size, data);
        return ResponseEntity.ok(fillingPageable(listResponse, page, size, data));
    }

    @GetMapping(value = "/show/all/not/curator/users")
    public ResponseEntity<UserListResponse> showListNotCuratorUsers(
        @RequestParam(defaultValue = "1") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(value = "data",
            defaultValue = "") String data) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

        UserListResponse listResponse =
            userService.getAllUsersWithRoleUserNotInCurator(customUserDetails.getId(), page, size, data);
        return ResponseEntity.ok(fillingPageable(listResponse, page, size, data));
    }

    @PostMapping(value = "/addUserToCurator/{userId}")
    public ResponseEntity<BaseResponse> addUserToCurator(
        @PathVariable Long userId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        BaseResponse baseResponse;

        Long curatorId = customUserDetails.getId();

        if (curatorId == null || userId == null) {
            baseResponse = new BaseResponse(false, "Curator or User id null", "Error");
            return new ResponseEntity<>(baseResponse, HttpStatus.BAD_REQUEST);
        }

        try {
            curatorUserService.addUserToCurator(curatorId, userId);
            baseResponse = new BaseResponse(true, "", "");
            return new ResponseEntity<>(baseResponse, HttpStatus.OK);
        } catch (NotFoundException notFoundException) {
            baseResponse = new BaseResponse(false, notFoundException.getMessage(), "Error");
            return new ResponseEntity<>(baseResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/removeUserFromCurator/{userId}")
    public ResponseEntity<BaseResponse> removeUserFromCurator(
        @PathVariable Long userId) {

        BaseResponse baseResponse;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

        Long curatorId = customUserDetails.getId();

        if (curatorId == null || userId == null) {
            baseResponse = new BaseResponse(false, "Curator or User id null", "Error");
            return new ResponseEntity<>(baseResponse, HttpStatus.BAD_REQUEST);
        }

        try {
            curatorUserService.removeUserFromCuratorUserList(curatorId, userId);
            baseResponse = new BaseResponse(true, "", "");
            return new ResponseEntity<>(baseResponse, HttpStatus.OK);
        } catch (NotFoundException notFoundException) {
            baseResponse = new BaseResponse(false, notFoundException.getMessage(), "Error");
            return new ResponseEntity<>(baseResponse, HttpStatus.BAD_REQUEST);
        }
    }

    private UserListResponse fillingPageable(UserListResponse listResponse, int page, int size, String data) {
        boolean hasPrev = page > 1;
        listResponse.setHasPrev(hasPrev);
        listResponse.setPrev(page - 1);
        listResponse.setNext(page + 1);
        listResponse.setSize(size);
        listResponse.setData(data);
        return listResponse;
    }
}
