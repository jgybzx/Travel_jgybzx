package com.jgybzx.service;

import com.jgybzx.domain.Order;

/**
 * @author: guojy
 * @date: 2019/12/18 22:01
 * @Description:
 * @version:
 */
public interface OrderService {
    //保存订单信息
    void save(Order order);
}
