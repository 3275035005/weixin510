package com.sky.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 美食社区 存储类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Share implements Serializable {
    private static final long serialVersionUID = 1L;
    //主键
    private Long id;
    //菜谱名称
    private String setmealName;
    //食材
    private String dishName;
    //用户id
    private Long employeeId;
    //制作步骤
    private String working;
    //心得体会
    private String content;
    //图片
    private String image;
    //视频
    private String video;
    //审核状态（0审核中 1通过 2拒绝）
    private String status;
    //创建时间
    private LocalDateTime createTime;

    //用户姓名
    private String employeeName;
}
