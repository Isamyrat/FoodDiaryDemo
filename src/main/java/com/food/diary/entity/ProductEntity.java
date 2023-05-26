package com.food.diary.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "PRODUCT")
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "product")
    private String product;

    @Column(name = "width")
    private Long width;

    @Column(name = "active")
    private boolean active;

    @ManyToOne
    @JoinColumn(name = "FOOD_DIARY_ID")
    private FoodDiaryEntity productDiaryEntity;
}
