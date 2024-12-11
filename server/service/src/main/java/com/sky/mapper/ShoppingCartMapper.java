package com.sky.mapper;

import com.sky.entity.ShoppingCart;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * PROJECT_NAME: sky-take-out
 * NAME: ShoppingCartMapper
 * DESCRIPTION : C端-购物车信息持久层接口
 */
@Mapper
public interface ShoppingCartMapper {
    /**
     * 添加购物车信息
     *
     * @param shoppingCart 购物车数据
     */
    void insertShoppingCart(@Param("shoppingCart") ShoppingCart shoppingCart);

    /**
     * 基于UserId + [dishId/setmealId/dishId+dishFlavor]进行数据查询
     *
     * @param conditionShoppingCart 用于保存查询条件的购物车对象
     * @return 满足查询条件的购物车数据集合
     */
    List<ShoppingCart> selectShoppingCart(@Param("conditionShoppingCart") ShoppingCart conditionShoppingCart);

    /**
     * 更新购物车信息
     *
     * @param existsShoppingCart 更新后的购物车信息(只需要更新数量)
     */
    void updateShoppingCartNumber(@Param("existsShoppingCart") ShoppingCart existsShoppingCart);

    /**
     * 清空购物车
     *
     * @param userId 当前登录用户Id
     */
    void cleanShoppingCart(@Param("userId") Long userId);

    void deleteShoppingCart(@Param("shoppingCart") ShoppingCart shoppingCartChecked);
}
