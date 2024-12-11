package com.sky.service;

import com.sky.entity.AddressBook;

import java.util.List;

/**
 * PROJECT_NAME: sky-take-out
 * NAME: AddressBookService
 * DESCRIPTION : C端-地址簿信息服务层接口
 */
public interface AddressBookService {

    /**
     * 查询用户所有地址信息
     *
     * @return 用户地址List
     */
    List<AddressBook> selectAddressBookList();

    /**
     * 添加用户地址簿信息
     *
     * @param addressBook 地址簿信息
     */
    void insertAddressBook(AddressBook addressBook);

    /**
     * 指定默认地址簿
     *
     * @param addressBook 默认地址簿信息(Id)
     */
    void modifyDefaultAddressBook(AddressBook addressBook);

    /**
     * 查询指定Id的地址簿
     *
     * @param addressBookId 地址簿Id
     */
    AddressBook selectAddressBookById(Long addressBookId);

    /**
     * 更新地址簿
     *
     * @param addressBook 更新后的地址簿信息(包含Id)
     */
    void updateAddressBook(AddressBook addressBook);

    /**
     * 根据Id删除地址簿
     *
     * @param id 要删除的地址簿Id
     */
    void deleteAddressBook(Long id);

    /**
     * 查询当前用户默认地址
     *
     * @return 用户默认地址
     */
    AddressBook selectDefaultAddressBook();
}
