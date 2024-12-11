package com.sky.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 菜谱信息存储类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Setmeal implements Serializable {

    private static final long serialVersionUID = 1L;
    //菜谱Id
    private Long id;
    //菜谱关联分类Id
    private Long categoryId;
    //菜谱名称
    private String name;
    //菜谱价格
    private BigDecimal price;
    //状态 0:停用 1:启用
    private Integer status;
    //菜谱描述信息
    private String description;
    //菜谱图片
    private String image;

    // 辣度 0不辣 1微辣 2中辣 3重辣
    private String pungencyType;

    // 甜度 0无糖 1少糖 2半糖 3多糖 4全糖
    private String sweetType;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private Long createUser;

    private Long updateUser;


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
