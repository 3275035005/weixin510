package com.sky.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 地址簿信息存储类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressBook implements Serializable {
    private static final long serialVersionUID = 1L;
    //地址簿Id
    private Long id;
    //用户Id
    private Long userId;
    //收货人
    private String consignee;
    //手机号
    private String phone;
    //性别 0:女 1:男
    private String sex;
    //省级区划编号
    private String provinceCode;
    //省级名称
    private String provinceName;
    //市级区划编号
    private String cityCode;
    //市级名称
    private String cityName;
    //区级区划编号
    private String districtCode;
    //区级名称
    private String districtName;
    //详细地址
    private String detail;
    //标签
    private String label;
    //是否默认 0:否 1:是
    private Integer isDefault;

    /**
     * 获取完整地址(省+市+区+具体地址)
     *
     * @return 完整地址
     */
    public String getFullAddress() {
        return this.provinceName + this.cityName + this.districtName + this.detail;
    }
}
