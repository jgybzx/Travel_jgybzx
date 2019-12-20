package com.jgybzx.service.imp;

import com.jgybzx.dao.OrderDao;
import com.jgybzx.dao.OrderItemDao;
import com.jgybzx.domain.Order;
import com.jgybzx.domain.OrderItem;
import com.jgybzx.service.OrderService;
import com.jgybzx.utils.MyBatisUtils;
import org.apache.ibatis.session.SqlSession;

import java.util.Map;

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

    @Override
    public void changeOrderState(Map map) {
        SqlSession sqlSession = MyBatisUtils.openSession();
        OrderDao orderDao = sqlSession.getMapper(OrderDao.class);
        //获取oid，微信给我的返回值 中，out_trade_no表示的是 订单id
        String oid = (String) map.get("out_trade_no");
        System.out.println("回调函数的"+oid);
        //修改订单状态
        orderDao.changeOrderState(oid);

        MyBatisUtils.close(sqlSession);
    }

    @Override
    public String sheckOrderState(String oid) {
        String state = "";

        SqlSession sqlSession = MyBatisUtils.openSession();
        OrderDao mapper = sqlSession.getMapper(OrderDao.class);

        state =mapper.sheckOrderState(oid);
        MyBatisUtils.close(sqlSession);
        return state;
    }
}
