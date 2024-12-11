package com.sky.vo;

import com.sky.entity.SetmealDish;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 菜谱详情(响应数据)存储类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SetmealVO implements Serializable {
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

    // 辣度 0不辣 1微辣 2中辣 3重辣
    private String pungencyType;

    // 甜度 0无糖 1少糖 2半糖 3多糖 4全糖
    private String sweetType;

    //图片
    private String image;
    //更新时间
    private LocalDateTime updateTime;
    //分类名称
    private String categoryName;
    //菜谱和食材的关联关系
    private List<SetmealDish> setmealDishes = new ArrayList<>();

    private String pungencyTypeName;



    public void setPungencyType(String pungencyType) {
        if ("0".equals(pungencyType)) {
            this.pungencyTypeName = "不辣";
        }else if ("1".equals(pungencyType)) {
            this.pungencyTypeName = "微辣";
        }else if ("2".equals(pungencyType)) {
            this.pungencyTypeName = "中辣";
        }else if ("3".equals(pungencyType)) {
            this.pungencyTypeName = "重辣";
        }
        this.pungencyType = pungencyType;
    }

    public void setSweetType(String sweetType) {
        if ("0".equals(pungencyType)) {
            this.sweetTypeName = "无糖";
        }else if ("1".equals(pungencyType)) {
            this.sweetTypeName = "少糖";
        }else if ("2".equals(pungencyType)) {
            this.sweetTypeName = "半糖";
        }else if ("3".equals(pungencyType)) {
            this.sweetTypeName = "多糖";
        }else if ("4".equals(pungencyType)) {
            this.sweetTypeName = "全糖";
        }
        this.sweetType = sweetType;
    }

    private String sweetTypeName;
}
