package com.sky.dto;

import com.sky.entity.DishFlavor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 食材添加数据传输类
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DishDTO implements Serializable {
    //食材Id
    private Long id;
    //食材名称
    private String name;
    //食材分类id
    private Long categoryId;
    //食材价格
    private BigDecimal price;
    //食材图片
    private String image;
    //食材描述信息
    private String description;
    //食材状态 0:停售 1:起售
    private Integer status;

}
