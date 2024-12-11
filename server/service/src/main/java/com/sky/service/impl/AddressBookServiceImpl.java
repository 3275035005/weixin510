package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.entity.AddressBook;
import com.sky.mapper.AddressBookMapper;
import com.sky.service.AddressBookService;
import com.sky.mapper.AddressBookMapper;
import com.sky.service.AddressBookService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * PROJECT_NAME: sky-take-out
 * NAME: AddressBookServiceImpl
 * DESCRIPTION : C端-地址簿信息服务层接口实现类
 */
@Service
public class AddressBookServiceImpl implements AddressBookService {

    //注入地址簿持久层接口实现类Bean
    @Resource
    private AddressBookMapper addressBookMapper;

    /**
     * 查询用户所有地址信息
     *
     * @return 用户地址List
     */
    @Override
    public List<AddressBook> selectAddressBookList() {
        //查询用户地址时传递当前登录用户Id
        return addressBookMapper.selectAddressBookList(BaseContext.getCurrentId());
    }

    /**
     * 添加用户地址簿信息
     *
     * @param addressBook 地址簿信息
     */
    @Override
    public void insertAddressBook(AddressBook addressBook) {
        //将当前用户信息与当前非默认地址添加到地址簿信息中
        addressBook.setUserId(BaseContext.getCurrentId());
        addressBook.setIsDefault(0);
        addressBookMapper.insertAddressBook(addressBook);
    }

    /**
     * 指定默认地址簿
     *
     * @param addressBook 默认地址簿信息(Id)
     */
    @Override
    public void modifyDefaultAddressBook(AddressBook addressBook) {
        //将当前用户的所有地址的是否默认修改为非默认
        addressBookMapper.updateAddressBookDefault0(BaseContext.getCurrentId());
        //将当前参数地址簿是否默认修改为默认1
        addressBook.setIsDefault(1);
        addressBookMapper.update(addressBook);
    }

    /**
     * 查询指定Id的地址簿
     *
     * @param addressBookId 地址簿Id
     * @return 地址簿信息
     */
    @Override
    public AddressBook selectAddressBookById(Long addressBookId) {
        //调用持久层查询指定Id的地址簿信息并返回
        return addressBookMapper.selectAddressBookById(addressBookId);
    }

    /**
     * 更新地址簿
     *
     * @param addressBook 更新后的地址簿信息(包含Id)
     */
    @Override
    public void updateAddressBook(AddressBook addressBook) {
        //调用持久层更新地址簿信息
        addressBookMapper.update(addressBook);
    }

    /**
     * 根据Id删除地址簿
     *
     * @param id 要删除的地址簿Id
     */
    @Override
    public void deleteAddressBook(Long id) {
        //调用持久层删除指定Id的地址簿
        addressBookMapper.deleteAddressBook(id);
    }

    /**
     * 查询当前用户默认地址
     *
     * @return 用户默认地址
     */
    @Override
    public AddressBook selectDefaultAddressBook() {
        //调用持久层查询当前用户的默认地址簿信息
        return addressBookMapper.selectDefaultAddressBook(BaseContext.getCurrentId());
    }
}
