package com.sky.service;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;

import java.util.List;

/**
 * PROJECT_NAME: sky-take-out
 * NAME: ShoppingCartService
 * DESCRIPTION : C端-购物车信息服务层接口
 */
public interface ShoppingCartService {

    /**
     * 添加数据到购物车
     *
     * @param shoppingCartDTO 要添加到购物车的数据(3种) 食材Id(口味) / 食材Id(不带口味) / 菜谱Id
     */
    void insertShoppingCart(ShoppingCartDTO shoppingCartDTO);

    /**
     * 查看购物车
     *
     * @return 当前登录用户的所有购物车信息
     */
    List<ShoppingCart> selectShoppingCart();

    /**
     * 清空购物车
     */
    void cleanShoppingCart();

    void substanceShoppingCart(ShoppingCartDTO shoppingCartDTO);
}
