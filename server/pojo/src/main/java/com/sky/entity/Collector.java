package com.sky.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 菜谱收藏 存储类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Collector implements Serializable {
    private static final long serialVersionUID = 1L;
    //主键
    private Long id;
    //用户id
    private Long employeeId;
    //菜谱id
    private Long setmealId;
    //创建时间
    private LocalDateTime createTime;
}
