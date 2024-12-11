package com.sky.result;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 封装分页查询结果
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PageResult implements Serializable {
    private long total; //总记录数 ▲nginx部署/小程序的前台代码会自动解析叫做total的数据项
    private List records; //当前页数据集合 ▲nginx部署/小程序的前台代码会自动解析叫做records的数据项
}
