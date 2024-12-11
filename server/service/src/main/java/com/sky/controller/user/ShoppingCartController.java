package com.sky.controller.user;

import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import com.sky.result.Result;
import com.sky.service.ShoppingCartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * PROJECT_NAME: sky-take-out
 * NAME: ShoppingCartController
 * DESCRIPTION : C端-购物车信息表现层接口
 */
@Slf4j
@Api(tags = "C端-购物车信息表现层接口")
@RequestMapping("/user/shoppingCart")
@RestController
public class ShoppingCartController {

    //注入购物车服务层接口实现类
    @Resource
    private ShoppingCartService shoppingCartService;

    /**
     * 添加数据到购物车
     *
     * @param shoppingCartDTO 要添加到购物车的数据(3种) 食材Id(口味) / 食材Id(不带口味) / 菜谱Id
     * @return 全局通用返回信息Bean
     */
    @ApiOperation("C端-添加数据到购物车")
    @PostMapping("/add")
    public Result insertShoppingCart(@RequestBody ShoppingCartDTO shoppingCartDTO) {
        log.info("本次添加到购物车的数据信息是 : {}", shoppingCartDTO);
        //调用服务层传递shoppingCartDTO完成购物车数据的添加
        shoppingCartService.insertShoppingCart(shoppingCartDTO);
        return Result.success();
    }

    /**
     * 查看购物车
     *
     * @return 全局通用返回信息Bean(当前登录用户的所有购物车信息)
     */
    @ApiOperation("C端-查看购物车")
    @GetMapping("/list")
    public Result<List<ShoppingCart>> selectShoppingCart() {
        log.info("查看当前用户的购物车信息,用户Id : {}", BaseContext.getCurrentId());
        //调用服务层查询
        List<ShoppingCart> shoppingCartList = shoppingCartService.selectShoppingCart();
        return Result.success(shoppingCartList);
    }

    /**
     * 清空购物车
     *
     * @return 全局通用返回Bean
     */
    @ApiOperation("C端-清空购物车")
    @DeleteMapping("/clean")
    public Result cleanShoppingCart() {
        log.info("清空当前用户的购物车信息,用户Id : {}", BaseContext.getCurrentId());
        //调用服务层清空当前用户的购物车信息(把表中的数据都删除掉)
        shoppingCartService.cleanShoppingCart();
        return Result.success();
    }

    @PostMapping("/sub")
    @ApiOperation("减少购物车数量")
    public Result substanceShoppingCart(@RequestBody ShoppingCartDTO shoppingCartDTO){
        log.info("减少购物车货物数量");
        shoppingCartService.substanceShoppingCart(shoppingCartDTO);
        return Result.success();
    }
}
