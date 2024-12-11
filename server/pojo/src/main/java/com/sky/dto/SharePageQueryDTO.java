package com.sky.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 美食社区(条件与分页)查询数据传输类
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SharePageQueryDTO implements Serializable {
    //查询页码
    private int page;
    //每页记录数
    private int pageSize;
    //菜谱名称
    private String setmealName;
    //审核状态（0审核中 1通过 2拒绝）
    private Integer status;

}
