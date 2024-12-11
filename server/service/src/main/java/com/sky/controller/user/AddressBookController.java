package com.sky.controller.user;

import com.sky.entity.AddressBook;
import com.sky.result.Result;
import com.sky.service.AddressBookService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * PROJECT_NAME: sky-take-out
 * NAME: AddressBookController
 * DESCRIPTION : C端-地址簿信息表现层接口
 */
@Slf4j
@Api(tags = "C端-地址簿信息表现层接口")
@RestController
@RequestMapping("/user/addressBook")
public class AddressBookController {

    //注入地址簿服务层接口实现类Bean
    @Resource
    private AddressBookService addressBookService;

    /**
     * 查询当前用户所有地址信息
     *
     * @return 全局通用返回信息Bean(用户地址List)
     */
    @ApiOperation("C端-查询当前用户所有地址信息")
    @GetMapping("/list")
    public Result<List<AddressBook>> selectAddressBookList() {
        log.info("查询当前用户所有地址簿信息");
        //调用服务层查询当前登录用户所有地址信息List并返回
        List<AddressBook> addressBookList = addressBookService.selectAddressBookList();
        return Result.success(addressBookList);
    }

    /**
     * 添加地址簿信息
     *
     * @param addressBook 地址簿信息
     * @return 全局通用返回信息Bean
     */
    @ApiOperation("C端-添加地址簿信息")
    @PostMapping
    public Result insertAddressBook(@RequestBody AddressBook addressBook) {
        log.info("添加地址簿信息的参数信息 : {}", addressBook);
        //调用服务层添加地址簿信息
        addressBookService.insertAddressBook(addressBook);
        return Result.success();
    }

    /**
     * 指定默认地址簿
     *
     * @param addressBook 默认地址簿信息(Id) C端提交的虽然只有一个Id但是是按照JSON方式提交的 所以必须基于一个Bean接收数据
     * @return 全局通用返回结果Bean
     */
    @ApiOperation("C端-指定默认地址簿")
    @PutMapping("/default")
    public Result modifyDefaultAddressBook(@RequestBody AddressBook addressBook) {
        log.info("指定默认地址簿信息的参数信息 : {}", addressBook);
        //调用服务层将指定Id的地址簿声明为默认地址
        addressBookService.modifyDefaultAddressBook(addressBook);
        return Result.success();
    }

    /**
     * 查询指定Id地址簿
     *
     * @param addressBookId 地址簿Id
     * @return 全局通用返回信息Bean(地址簿信息)
     */
    @ApiOperation("C端-查询指定Id地址簿")
    @GetMapping("/{addressBookId}")
    public Result<AddressBook> selectAddressBookById(@PathVariable Long addressBookId) {
        log.info("基于Id查询地址簿的参数信息 : {}", addressBookId);
        //调用服务层查询指定Id的地址簿信息并返回
        AddressBook addressBook = addressBookService.selectAddressBookById(addressBookId);
        return Result.success(addressBook);
    }

    /**
     * 更新地址簿
     *
     * @param addressBook 更新后的地址簿信息(包含Id)
     * @return 全局通用返回信息Bean
     */
    @ApiOperation("C端-更新地址簿")
    @PutMapping
    public Result updateAddressBook(@RequestBody AddressBook addressBook) {
        log.info("更新地址簿的参数信息 : {}", addressBook);
        //调用服务端完成地址簿信息的更新
        addressBookService.updateAddressBook(addressBook);
        return Result.success();
    }

    /**
     * 根据Id删除地址簿
     *
     * @param id 要删除的地址簿Id
     * @return 全局通用返回信息Bean
     */
    @ApiOperation("C端-根据Id删除地址簿")
    @DeleteMapping
    public Result deleteAddressBook(Long id) {
        log.info("删除地址簿的参数信息 : {}", id);
        //调用服务层删除地址簿信息
        addressBookService.deleteAddressBook(id);
        return Result.success();
    }

    /**
     * 查询当前用户默认地址
     *
     * @return 全局通用返回信息Bean(用户默认地址)
     */
    @ApiOperation("C端-查询当前用户默认地址")
    @GetMapping("/default")
    public Result<AddressBook> selectDefaultAddressBook() {
        log.info("查询当前用户默认地址簿信息");
        //调用服务层查询默认地址簿is_default为1的地址簿信息
        AddressBook defaultAddressBook = addressBookService.selectDefaultAddressBook();
        return Result.success(defaultAddressBook);
    }
}
