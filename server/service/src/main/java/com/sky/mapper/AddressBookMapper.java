package com.sky.mapper;

import com.sky.entity.AddressBook;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * PROJECT_NAME: sky-take-out
 * NAME: AddressBookMapper

 * DESCRIPTION : C端-地址簿信息持久层接口
 */
@Mapper
public interface AddressBookMapper {

    /**
     * 查询用户所有地址信息
     *
     * @return 用户地址List
     */
    List<AddressBook> selectAddressBookList(@Param("userId") Long userId);

    /**
     * 添加用户地址簿信息
     *
     * @param addressBook 地址簿信息
     */
    void insertAddressBook(@Param("addressBook") AddressBook addressBook);

    /**
     * 将用户的所有地址簿的默认修改非默认地址
     *
     * @param userId 用户Id
     */
    void updateAddressBookDefault0(@Param("userId") Long userId);

    /**
     * 更新用户地址簿信息
     *
     * @param addressBook 更新后的地址簿信息(基于Id)
     */
    void update(@Param("addressBook") AddressBook addressBook);


    /**
     * 查询指定Id的地址簿
     *
     * @param addressBookId 地址簿Id
     * @return 地址簿信息
     */
    AddressBook selectAddressBookById(@Param("addressBookId") Long addressBookId);

    /**
     * 根据Id删除地址簿
     *
     * @param addressBookId 要删除的地址簿Id
     */
    void deleteAddressBook(@Param("addressBookId") Long addressBookId);

    /**
     * 查询用户默认地址
     *
     * @param userId 用户Id
     * @return 用户默认地址
     */
    AddressBook selectDefaultAddressBook(Long userId);
}
