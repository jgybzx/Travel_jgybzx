package com.jgybzx.dao;

import com.jgybzx.domain.Address;

/**
 * @author: guojy
 * @date: 2019/12/18 20:52
 * @Description: 收货人地址
 * @version:
 */
public interface AddressDao {
    //根据id获得地址信息
    Address findAddressByid(int aid);


}
