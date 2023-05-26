package com.food.diary.repository;

import com.food.diary.entity.FoodDiaryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FoodDiaryRepository extends JpaRepository<FoodDiaryEntity, Long> {

    @Query(value = "select * from food_diary where user_id=:userId order by created_date desc limit :pageSize offset :offset",
    nativeQuery = true)
    List<FoodDiaryEntity> getAllByUserId(final Long userId, final int pageSize, final int offset);
}
