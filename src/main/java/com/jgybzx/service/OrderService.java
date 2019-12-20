package com.jgybzx.service;

import com.jgybzx.domain.Order;

import java.util.Map;

/**
 * @author: guojy
 * @date: 2019/12/18 22:01
 * @Description:
 * @version:
 */
public interface OrderService {
    //保存订单信息
    void save(Order order);
    //改变订单状态
    void changeOrderState(Map map);

    String sheckOrderState(String oid);

}
