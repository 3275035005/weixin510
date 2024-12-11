package com.sky.service;

import com.github.pagehelper.Page;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.dto.SharePageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.entity.Share;
import com.sky.result.PageResult;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * PROJECT_NAME: sky-take-out
 * NAME: SetmealService

 * DESCRIPTION : B端-美食社区服务层接口
 */
public interface ShareService {

    /**
     * 添加美食社区信息
     *
     * @param share 美食社区信息Bean
     */
    void insertShare( Share share);


    /**
     * 按照条件查询数据
     *
     * @param share
     * @return 分页结果封装Bean
     */
    List<Share> selectList(@Param("share") Share share);
    /**
     * 分页查询美食社区功能
     *
     * @param sharePageQueryDTO 分页参数Bean(查询页数/每页显示条数/美食社区名称/美食社区类型)
     * @return 分页结果封装Bean
     */
    PageResult selectByPage( SharePageQueryDTO sharePageQueryDTO);

    /**
     * 基于美食社区Id删除美食社区
     *
     * @param shareId 要删除的美食社区Id
     */
    void deleteShareById( Long shareId);

    /**
     * 更新美食社区信息
     *
     * @param share 美食社区信息Bean 更新后的美食社区信息(基于Id)
     */
    void updateShareStatus(Long id, String status);

    /**
     * 基于美食社区Id查询美食社区名称
     * @param shareId 美食社区Id
     * @return 美食社区名称
     */
    Share selectShareById( Long shareId);
}
