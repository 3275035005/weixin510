package com.sky.vo;

import com.sky.entity.DishFlavor;
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
 * 食材详细信息(响应数据)存储类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DishVO implements Serializable {
    //食材Id
    private Long id;
    //食材名称
    private String name;
    //食材分类Id
    private Long categoryId;
    //食材价格
    private BigDecimal price;
    //食材图片
    private String image;
    //食材描述信息
    private String description;
    //食材状态 0:停售 1:起售
    private Integer status;
    //食材更新时间
    private LocalDateTime updateTime;
    //食材分类名称
    private String categoryName;
    //食材关联的口味
    private List<DishFlavor> flavors = new ArrayList<>();
}
