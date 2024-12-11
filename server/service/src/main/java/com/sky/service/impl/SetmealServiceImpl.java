package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.exception.BaseException;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetmealService;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * PROJECT_NAME: sky-take-out
 * NAME: SetmealServiceImpl
 * DESCRIPTION : B端-菜谱信息服务层接口实现类
 */
@Service
public class SetmealServiceImpl implements SetmealService {

    //注入菜谱持久层接口实现Bean
    @Resource
    private SetmealMapper setmealMapper;
    //注入食材持久层接口实现Bean
    @Resource
    private DishMapper dishMapper;
    //注入菜谱食材持久层接口实现Bean
    @Resource
    private SetmealDishMapper setmealDishMapper;

    /**
     * 分页查询菜谱信息
     *
     * @param setmealPageQueryDTO 菜谱分页查询信息封装Bean
     * @return 全局通用返回信息Bean(分页结果封装Bean)
     */
    @Override
    public PageResult selectByPage(SetmealPageQueryDTO setmealPageQueryDTO) {
        //开启分页查询拦截
        PageHelper.startPage(setmealPageQueryDTO.getPage(), setmealPageQueryDTO.getPageSize());
        Page<SetmealVO> page = setmealMapper.selectByPage(setmealPageQueryDTO);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 添加菜谱信息
     *
     * @param setmealDTO 菜谱信息封装Bean(包含菜谱基本信息与菜谱内食材信息)
     */
    @Override
    @CacheEvict(cacheNames = "SETMEAL_CACHE", allEntries = true) //删除所有前缀是SETMEAL_CACHE的缓存数据
    public void insertSetmeal(SetmealDTO setmealDTO) {
        //创建菜谱基本信息完成信息获取转换后添加
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        setmealMapper.insertSetmeal(setmeal);
        //获取菜谱信息中的具体食材组成信息List后添加菜谱Id后批量添加
        List<SetmealDish> setmealDishList = setmealDTO.getSetmealDishes();
        setmealDishList.forEach(setmealDish -> setmealDish.setSetmealId(setmeal.getId()));
        setmealDishMapper.batchInsertSetmealDish(setmealDishList);
    }

    /**
     * 删除菜谱信息
     *
     * @param ids 要删除的菜谱Id组成的List集合
     */
    @Transactional
    @Override
    @CacheEvict(cacheNames = "SETMEAL_CACHE", allEntries = true) //删除所有前缀是SETMEAL_CACHE的缓存数据
    public void batchDeleteSetmeal(List<Long> ids) {
        for (Long deleteSetmealId : ids) {
            //查询当前菜谱Id的状态(如果是启售状态则无法删除)
            Integer setmealStatus = setmealMapper.selectStatusById(deleteSetmealId);
            if (setmealStatus.equals(StatusConstant.ENABLE))
                throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
            //删除菜谱信息与菜谱食材表信息
            setmealMapper.deleteById(deleteSetmealId);
            setmealMapper.deleteSetmealDishBySetmealId(deleteSetmealId);
        }
    }

    /**
     * 基于菜谱Id查询菜谱信息
     *
     * @param setmealId 菜谱Id
     * @return 菜谱与菜谱食材关联VO
     */
    @Override
    public SetmealVO selectById(Long setmealId) {
        SetmealVO setmealVO = new SetmealVO();
        Setmeal setmeal = setmealMapper.selectById(setmealId);
        List<SetmealDish> setmealDishList = setmealMapper.selectSetmealDishBySetmealId(setmealId);
        //将查询出的基本信息封装到VO中并将查询出的对应菜谱食材关联信息保存到VO中
        BeanUtils.copyProperties(setmeal, setmealVO);
        setmealVO.setSetmealDishes(setmealDishList);
        return setmealVO;
    }

    /**
     * 更新菜谱信息
     *
     * @param setmealDTO 菜谱信息封装Bean(包含菜谱基本信息与菜谱内食材信息【包含SetmealId】)
     */
    @Transactional
    @Override
    @CacheEvict(cacheNames = "SETMEAL_CACHE", allEntries = true) //删除所有前缀是SETMEAL_CACHE的缓存数据
    public void updateSetmeal(SetmealDTO setmealDTO) {
        //创建Setmeal从SetmealDTO获取更新后的信息完成更新
        Setmeal newSetmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, newSetmeal);
        setmealMapper.updateSetmeal(newSetmeal);
        //删除当前Setmeal的Id在菜谱信息表中对应的所有食材信息后重新添加
        setmealMapper.deleteSetmealDishBySetmealId(newSetmeal.getId());
        if (Objects.nonNull(setmealDTO.getSetmealDishes()) && !setmealDTO.getSetmealDishes().isEmpty()) {
            setmealDTO.getSetmealDishes().forEach(setmealDish -> setmealDish.setSetmealId(newSetmeal.getId()));
            setmealDishMapper.batchInsertSetmealDish(setmealDTO.getSetmealDishes());
        }
    }

    /**
     * 更新菜谱状态(出售/禁售)
     *
     * @param setmealId 菜谱Id
     * @param status    更新后的状态
     */
    @Override
    @CacheEvict(cacheNames = "SETMEAL_CACHE", allEntries = true) //删除所有前缀是SETMEAL_CACHE的缓存数据
    public void modifyStatus(Long setmealId, Integer status) {
        //将状态信息与菜谱Id封装为Setmeal
        Setmeal newSetmeal = Setmeal.builder().id(setmealId).status(status).build();
        if (newSetmeal.getStatus() == 0) {
            //如果修改为禁售则无需查询食材状态
            setmealMapper.updateSetmeal(newSetmeal);
        } else if (newSetmeal.getStatus() == 1) {
            //如果要进行菜谱启售,必须先查询当前菜谱关联的所有食材是否启售
            List<SetmealDish> setmealDishList = setmealMapper.selectSetmealDishBySetmealId(setmealId);
            setmealDishList.forEach(setmealDish -> {
                Dish dish = dishMapper.selectDishById(setmealDish.getDishId());
                if (dish.getStatus().equals(StatusConstant.DISABLE))
                    throw new BaseException(MessageConstant.SETMEAL_ENABLE_FAILED);
            });
            //如果所有食材都是启售状态则菜谱可以启售
            setmealMapper.updateSetmeal(newSetmeal);
        }
    }

    /**
     * 基于分类Id查询菜谱信息
     *
     * @param categoryId 分类Id
     * @return 指定分类菜谱List集合
     */
    @Override
    public List<Setmeal> selectSetmealByCategoryId(Long categoryId) {
        //封装用于查询菜谱信息的Setmeal条件
        Setmeal conditionSetmeal = Setmeal.builder().status(1).categoryId(categoryId).build();
        //调用持久层传递保存了查询参数的条件对象进行查询
        List<Setmeal> setmealList = setmealMapper.selectByCondition(conditionSetmeal);
        return setmealList;
    }

    /**
     * 查询菜谱中包含的食材信息
     *
     * @param setmealId 菜谱Id
     * @return 菜谱中食材的基本信息
     */
    @Override
    public List<DishItemVO> selectSetmealDishBySetmealId(Long setmealId) {
        //调用菜谱食材关联持久层查询菜谱中食材的基本信息
        List<DishItemVO> dishItemVOList = setmealDishMapper.selectSetmealDishBySetmealId(setmealId);
        return dishItemVOList;
    }

    @Override
    public List<Setmeal> getListByNotId(List<Long> ids) {
        return setmealMapper.getListByNotId(ids);
    }

    @Override
    public Map<String, Object> selectSetmealByName(String name) {
        Map<String, Object> map = new HashMap<>();
        Setmeal setmeal = Setmeal.builder().status(1).name(name).build();

        List<Setmeal> setmealNew = setmealMapper.selectByCondition(setmeal);
        List<Dish> dish = dishMapper.selectDishByName(name);
        map.put("setmeal",setmealNew);
        map.put("dish",dish);
        return map;
    }
}
