package com.jgybzx.service;

import com.jgybzx.domain.Address;

/**
 * @author: guojy
 * @date: 2019/12/18 20:53
 * @Description:
 * @version:
 */
public interface AddressService {
    //根据id获取地址
    Address findAddressByid(int aid);

}
