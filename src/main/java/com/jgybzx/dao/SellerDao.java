package com.jgybzx.dao;

import com.jgybzx.domain.Seller;

/**
 * @author: guojy
 * @date: 2019/12/17 19:50
 * @Description:
 * @version:
 */
public interface SellerDao {
    //根据id获得数据
    Seller getBySid(String id);
}
