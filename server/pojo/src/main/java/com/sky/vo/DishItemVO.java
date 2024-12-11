package com.sky.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 菜谱包含食材基础信息(响应数据)存储类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DishItemVO implements Serializable {
    //食材名称
    private String name;
    //份数
    private Integer copies;
    //食材图片
    private String image;
    //食材描述
    private String description;

    // 辣度 0不辣 1微辣 2中辣 3重辣
    private String pungencyType;

    // 甜度 0无糖 1少糖 2半糖 3多糖 4全糖
    private String sweetType;




}
