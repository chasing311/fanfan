package com.chasing.fan.entity;

import lombok.Data;

import java.util.List;

@Data
public class SetmealDTO extends Setmeal {
    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
