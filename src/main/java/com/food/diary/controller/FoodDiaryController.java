package com.food.diary.controller;

import com.food.diary.DTO.FoodDiaryDTO;
import com.food.diary.DTO.ProductDTO;
import com.food.diary.response.foodDiary.FoodDiaryListResponse;
import com.food.diary.response.foodDiary.FoodDiaryResponse;
import com.food.diary.response.product.ProductListResponse;
import com.food.diary.security.CustomUserDetails;
import com.food.diary.service.FoodDiaryService;
import com.food.diary.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/api/v1/food/diary/")
@RequiredArgsConstructor
public class FoodDiaryController {
    private final FoodDiaryService foodDiaryService;
    private final ProductService productService;

    @GetMapping(value = "/show/all/by-user/{userId}")
    public ResponseEntity<FoodDiaryListResponse> showAllByUser(
        @PathVariable Long userId,
        @RequestParam(defaultValue = "1") int page,
        @RequestParam(defaultValue = "10") int size) {
        FoodDiaryListResponse listResponse = foodDiaryService.getByUserId(userId, page, size);
        boolean hasPrev = page > 1;
        listResponse.setHasPrev(hasPrev);
        listResponse.setPrev(page - 1);
        listResponse.setNext(page + 1);
        listResponse.setSize(size);
        return ResponseEntity.ok(listResponse);
    }

    @GetMapping(value = "/show/all")
    public ResponseEntity<FoodDiaryListResponse> showAllByUser(
        @RequestParam(defaultValue = "1") int page,
        @RequestParam(defaultValue = "10") int size) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        FoodDiaryListResponse listResponse = foodDiaryService.getByUserId(customUserDetails.getId(), page, size);
        boolean hasPrev = page > 1;
        listResponse.setHasPrev(hasPrev);
        listResponse.setPrev(page - 1);
        listResponse.setNext(page + 1);
        listResponse.setSize(size);
        return ResponseEntity.ok(listResponse);
    }

    @PostMapping(value = "/create")
    public ResponseEntity<FoodDiaryResponse> createFoodDiary(@RequestBody FoodDiaryDTO foodDiaryDTO) {
        FoodDiaryResponse baseResponse;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        FoodDiaryDTO foodDiaryDTO1 = foodDiaryService.addFoodDiary(customUserDetails.getId(), foodDiaryDTO);
        baseResponse = new FoodDiaryResponse(true, "", "", foodDiaryDTO1);
        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }

    @GetMapping(value = "/getById/{foodDiaryId}")
    public ResponseEntity<FoodDiaryResponse> getById(
        @PathVariable Long foodDiaryId) {
        FoodDiaryResponse foodDiaryResponse;
        FoodDiaryDTO foodDiaryDTO = foodDiaryService.getFoodDiaryDTOById(foodDiaryId);
        foodDiaryResponse = new FoodDiaryResponse(true, "", "", foodDiaryDTO);
        return new ResponseEntity<>(foodDiaryResponse, HttpStatus.OK);
    }

    @PostMapping(value = "/edit/{foodDiaryId}")
    public ResponseEntity<FoodDiaryResponse> editFoodDiary(
        @PathVariable Long foodDiaryId,
        @RequestBody FoodDiaryDTO foodDiaryDTO) {
        FoodDiaryResponse foodDiaryResponse;
        FoodDiaryDTO foodDiaryDTOSecond = foodDiaryService.editFoodDiary(foodDiaryId, foodDiaryDTO);
        foodDiaryResponse = new FoodDiaryResponse(true, "", "", foodDiaryDTOSecond);
        return new ResponseEntity<>(foodDiaryResponse, HttpStatus.OK);
    }


    @GetMapping(value = "/get/same/product/name")
    public ResponseEntity<ProductListResponse> getSameProductName(
        @RequestParam String productName) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        List<ProductDTO> productDTOList = productService.getAllLikeProductName(customUserDetails.getId(), productName);
        ProductListResponse productListResponse = new ProductListResponse(productDTOList);
        return ResponseEntity.ok(productListResponse);
    }
}
