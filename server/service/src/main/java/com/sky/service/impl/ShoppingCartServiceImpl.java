package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.ShoppingCart;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.ShoppingCartService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * PROJECT_NAME: sky-take-out
 * NAME: ShoppingCartServiceImpl
 * DESCRIPTION : C端-购物车信息服务层接口实现类
 */
@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    //注入购物车/食材/菜谱持久层接口实现类Bean
    @Resource
    private ShoppingCartMapper shoppingCartMapper;
    @Resource
    private DishMapper dishMapper;
    @Resource
    private SetmealMapper setmealMapper;

    /**
     * 添加数据到购物车
     *
     * @param shoppingCartDTO 要添加到购物车的数据(3种) 食材Id(口味) / 食材Id(不带口味) / 菜谱Id
     */
    @Override
    public void insertShoppingCart(ShoppingCartDTO shoppingCartDTO) {
        //将ShoppingCartDTO中的基本信息拷贝到ShoppingCart中
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);
        shoppingCart.setUserId(BaseContext.getCurrentId());
        //调用持久层方法将shoppingCart作为查询参数传递,将查询到的结果以List的方式返回【复用性】
        List<ShoppingCart> shoppingCartList = shoppingCartMapper.selectShoppingCart(shoppingCart);
        if (Objects.nonNull(shoppingCartList) && shoppingCartList.size() == 1) {
            //本次添加的购物车信息已经存在 => 更新数量
            ShoppingCart existsShoppingCart = shoppingCartList.get(0);
            existsShoppingCart.setNumber(existsShoppingCart.getNumber() + 1);
            shoppingCartMapper.updateShoppingCartNumber(existsShoppingCart);
        } else {
            //判断本次添加的是食材/菜谱
            if (Objects.nonNull(shoppingCartDTO.getDishId())) {
                //如果本次添加的是食材数据 查询食材信息
                Dish dish = dishMapper.selectDishById(shoppingCartDTO.getDishId());
                shoppingCart.setName(dish.getName());
                shoppingCart.setAmount(dish.getPrice());
                shoppingCart.setImage(dish.getImage());
            } else {
                //如果本次添加的是菜谱数据 查询菜谱信息
                Setmeal setmeal = setmealMapper.selectById(shoppingCartDTO.getSetmealId());
                shoppingCart.setName(setmeal.getName());
                shoppingCart.setAmount(setmeal.getPrice());
                shoppingCart.setImage(setmeal.getImage());
            }
            //将购物车中的剩余内容完成封装调用持久层完成添加
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartMapper.insertShoppingCart(shoppingCart);
        }
    }

    /**
     * 查看购物车
     *
     * @return 当前登录用户的所有购物车信息
     */
    @Override
    public List<ShoppingCart> selectShoppingCart() {
        //封装一个作为查询条件的ShoppingCart对象(用户Id)
        ShoppingCart conditionShoppingCart = ShoppingCart.builder().userId(BaseContext.getCurrentId()).build();
        List<ShoppingCart> shoppingCartList = shoppingCartMapper.selectShoppingCart(conditionShoppingCart);
        return shoppingCartList;
    }

    @Override
    public void substanceShoppingCart(ShoppingCartDTO shoppingCartDTO) {
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO,shoppingCart);
        shoppingCart.setUserId(BaseContext.getCurrentId());
        List<ShoppingCart> shoppingCartList = shoppingCartMapper.selectShoppingCart(shoppingCart);
        ShoppingCart shoppingCartChecked = shoppingCartList.get(0);
        if (shoppingCartChecked.getNumber()>1){
            shoppingCartChecked.setNumber(shoppingCartChecked.getNumber() - 1);
            shoppingCartMapper.updateShoppingCartNumber(shoppingCartChecked);
        }else shoppingCartMapper.deleteShoppingCart(shoppingCartChecked);
    }

    /**
     * 清空购物车
     */
    @Override
    public void cleanShoppingCart() {
        //调用持久层将要清空的购物车信息的用户Id作为参数传递
        shoppingCartMapper.cleanShoppingCart(BaseContext.getCurrentId());
    }
}
