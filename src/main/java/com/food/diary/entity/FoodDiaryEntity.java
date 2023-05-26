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

import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "FOOD_DIARY")
public class FoodDiaryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "type")
    private String type;

    @Column(name = "created_date")
    private Date createdDate;

    @Column(name = "water")
    private Long water;

    @Column(name = "emotional_condition")
    private String emotionalCondition;

    @Column(name = "physical_state")
    private String physicalState;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private UserEntity user;
}
