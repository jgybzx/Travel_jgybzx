package com.jgybzx.service.imp;

import com.jgybzx.dao.OrderDao;
import com.jgybzx.dao.OrderItemDao;
import com.jgybzx.domain.Order;
import com.jgybzx.domain.OrderItem;
import com.jgybzx.service.OrderService;
import com.jgybzx.utils.MyBatisUtils;
import org.apache.ibatis.session.SqlSession;

/**
 * @author: guojy
 * @date: 2019/12/18 22:01
 * @Description:
 * @version:
 */
public class OrderServiceIml implements OrderService {
    @Override
    public void save(Order order) {
        SqlSession sqlSession = MyBatisUtils.openSession();
        OrderDao orderDao = sqlSession.getMapper(OrderDao.class);
        OrderItemDao orderItemDao = sqlSession.getMapper(OrderItemDao.class);


        orderDao.save(order);
        //取出orderDao 中的 OrderItemList 遍历 插入到数据库
//        System.out.println("order.getOrderItemList() = " + order.getOrderItemList());
        for (OrderItem orderItem : order.getOrderItemList()) {
//            System.out.println("orderItem = " + orderItem);
           orderItemDao.saveOrderItem(orderItem);
        }


        MyBatisUtils.close(sqlSession);
    }
}
