package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.context.BaseContext;
import com.sky.dto.SharePageQueryDTO;
import com.sky.entity.Category;
import com.sky.entity.Collector;
import com.sky.entity.Share;
import com.sky.mapper.CollectorMapper;
import com.sky.mapper.ShareMapper;
import com.sky.result.PageResult;
import com.sky.service.CollectorService;
import com.sky.service.ShareService;
import com.sky.vo.SetmealVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * PROJECT_NAME: sky-take-out
 * NAME: CollectorServiceImpl
 * DESCRIPTION : B端-美食社区服务层接口实现类
 */
@Service
public class ShareServiceImpl implements ShareService {

    @Resource
    private ShareMapper shareMapper;


    @Override
    public void insertShare(Share share) {
        share.setStatus("0");
        share.setCreateTime(LocalDateTime.now());
        share.setEmployeeId(BaseContext.getCurrentId());
        shareMapper.insertShare(share);
    }

    @Override
    public List<Share> selectList(Share share) {
        return    shareMapper.selectList(share);
    }

    @Override
    public PageResult selectByPage(SharePageQueryDTO sharePageQueryDTO) {
        //开启分页查询拦截
        PageHelper.startPage(sharePageQueryDTO.getPage(), sharePageQueryDTO.getPageSize());
        Page<Share> selectByPage = shareMapper.selectByPage(sharePageQueryDTO);
        return new PageResult(selectByPage.getTotal(), selectByPage.getResult());
    }

    @Override
    public void deleteShareById(Long shareId) {
        shareMapper.deleteShareById(shareId);
    }

    @Override
    public void updateShareStatus(Long id, String status) {
        shareMapper.updateShareStatus(id, status);
    }

    @Override
    public Share selectShareById(Long shareId) {
        return shareMapper.selectShareById(shareId);
    }
}
