package com.sky.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 商品销售查询数据传输类
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GoodsSalesDTO implements Serializable {
    //商品名称
    private String name;
    //销量
    private Integer number;
}
