package com.sky.dto;

import com.sky.entity.SetmealDish;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 菜谱信息添加数据传输类
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SetmealDTO implements Serializable {
    //菜谱Id
    private Long id;
    //分类Id
    private Long categoryId;
    //菜谱名称
    private String name;
    //菜谱价格
    private BigDecimal price;
    //状态 0:停用 1:启用
    private Integer status;
    //描述信息
    private String description;
    //图片
    private String image;

    // 辣度 0不辣 1微辣 2中辣 3重辣
    private String pungencyType;

    // 甜度 0无糖 1少糖 2半糖 3多糖 4全糖
    private String sweetType;

    //菜谱食材关系
    private List<SetmealDish> setmealDishes = new ArrayList<>();
}
