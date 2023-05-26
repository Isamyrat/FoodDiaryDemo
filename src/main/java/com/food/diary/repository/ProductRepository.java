package com.food.diary.repository;

import com.food.diary.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

    @Query(value = "select * from product where food_diary_id=:id and active = true",
        nativeQuery = true)
    List<ProductEntity> getAllByFoodDiaryId(final Long id);

    @Query(value = "select DISTINCT(p.product) from product p join food_diary fd on fd.id = p.food_diary_id where fd.user_id=:userId and p.active= true and p.product LIKE %:data%",
    nativeQuery = true)
    List<String> getAllLikeProductName(final Long userId,  final String data);
}
