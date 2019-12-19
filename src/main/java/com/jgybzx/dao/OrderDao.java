package com.jgybzx.dao;

import com.jgybzx.domain.Order;

/**
 * @author: guojy
 * @date: 2019/12/18 22:01
 * @Description:
 * @version:
 */
public interface OrderDao {
    //保存 order数据
    void save(Order order);
}
