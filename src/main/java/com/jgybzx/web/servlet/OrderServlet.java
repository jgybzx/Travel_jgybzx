package com.jgybzx.web.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jgybzx.constant.Constant;
import com.jgybzx.domain.*;
import com.jgybzx.service.AddressService;
import com.jgybzx.service.OrderService;
import com.jgybzx.service.UserService;
import com.jgybzx.utils.BeanFactoryUtils;
import com.jgybzx.utils.RedisUtils;
import com.jgybzx.utils.UuidUtils;
import com.jgybzx.web.Base.BaseServlet;
import org.apache.commons.lang3.StringUtils;
import redis.clients.jedis.Jedis;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * @author: guojy
 * @date: 2019/12/18 19:25
 * @Description:
 * @version:
 */
//http://localhost:8080/
@WebServlet("/OrderServlet")
//WebServlet(name = "OrderServlet",urlPatterns="/OrderServlet")
public class OrderServlet extends BaseServlet {
    /**
     * 获取结算页面所需数据
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void submitCart(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //获取 用户登陆信息
        User loginUser = (User) request.getSession().getAttribute(Constant.loginUser);
        if (loginUser == null) {
            //如果没有用户登陆，重定向到首页
            response.sendRedirect(request.getContextPath());
            return;
        }
        //从缓存中获取购物车数据，
        Jedis jedisBean = RedisUtils.getJedisBean();
        Cart cart = getRedisCart(jedisBean, loginUser.getUid());

        request.setAttribute(Constant.CART, cart);
        jedisBean.close();

        //获取收货人的地址信息，根据登陆用户 的uid
        UserService userService = (UserService) BeanFactoryUtils.getBean("UserService");
        List<Address> userAddress = userService.findUserAddress(loginUser.getUid());
        System.out.println("userAddress = " + userAddress);
        request.setAttribute(Constant.USERADDRESS, userAddress);
        System.out.println("Constant.USERADDRESS = " + Constant.USERADDRESS);

        //请求转发到 结算页面
        request.getRequestDispatcher("order_info.jsp").forward(request, response);
    }

    //从缓存中获取购物车数据,抽取方法
    public Cart getRedisCart(Jedis jedis, Integer uid) throws IOException {
        Cart cart;
        String json = jedis.get(Constant.CART_NAME + uid);
        if (StringUtils.isBlank(json)) {
            return new Cart();
        } else {
            cart = new ObjectMapper().readValue(json, Cart.class);
        }
        return cart;
    }

    /**
     * 提交订单，将订单中的相关数据写入 数据库，同时删除redis中的数据
     * 订单表，存储：订单号，购物项id，用户id，总价格，时间，状态（是否付款）收货人地址，收货人电话
     * 购物项存储：购物项id，时间，数量，小计，商品id，订单id。
     * 所以就是 点击提交订单，就获取这些数据 写入到数据库
     * 获取到 地址信息
     * 获取到购物项信息
     */
    public void submitOrder(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1.获取数据
        //获取登陆用户
        User loginUser = (User) request.getSession().getAttribute(Constant.loginUser);
        //a.缓存中获取购物车数据
        Jedis jedisBean = RedisUtils.getJedisBean();
        Cart redisCart = getRedisCart(jedisBean, loginUser.getUid());
        jedisBean.close();

        //b.获取地址信息，通过id查数据库
        String addressId = request.getParameter("addressId");
        AddressService addressService = (AddressService) BeanFactoryUtils.getBean("AddressService");
        Address address = addressService.findAddressByid(Integer.parseInt(addressId));


        //2.封装数据数据，写入数据库
        Order order = new Order();
        order.setOid(UuidUtils.getUuid());//订单id
        order.setOrdertime(new Date());//下单时间
        order.setTotal(redisCart.getTotalPrice());//订单金额
        order.setState(0);//订单状态，默认初始化为0
        order.setAddress(address.getAddress());//收货人地址
        order.setContact(address.getContact());//收货联系人
        order.setTelephone(address.getTelephone());//收货人手机号
        order.setUser(loginUser);//下单用户
//        order.setOrderItemList();//购物项列表,目前缺少orderItemList 无法封装

        //封装 购物项数据
        //c.获取OrderItem数据

        //获取出所有的商品   cartItmeMap 将里边的信息，遍历封装到 OrderItemList中
        Map<Integer, CartItme> cartItmeMap = redisCart.getCartItmeMap();
        //定义 oder所需要的 OrderItemList，
        List<OrderItem> orderItemList = new ArrayList<>();
        //遍历map
        Set<Integer> integers = cartItmeMap.keySet();
        //integer
        for (Integer integer : integers) {
            //获得map中的value
            CartItme cartItme = cartItmeMap.get(integer);
            OrderItem orderItem = new OrderItem();
//            orderItem.setItemid();//主键 自增不需要赋值
            orderItem.setItemtime(new Date());
            orderItem.setNum(cartItme.getCount());
            orderItem.setSubtotal(cartItme.getSubTotal());
            orderItem.setRoute(cartItme.getRoute());
            orderItem.setOrder(order);

            //将orderItem放入 orderItemList
            orderItemList.add(orderItem);
        }
        //最后  设置一下  order 的 setOrderItemList
        order.setOrderItemList(orderItemList);

        //数据封转完之后  调用service 保存到数据库，因为order中 已经有了orderItemList 所以只调用OrderService
        OrderService orderService = (OrderService) BeanFactoryUtils.getBean("OrderService");
        orderService.save(order);

        //数据保存完之后 删除缓存数据
        Jedis jedisBean1 = RedisUtils.getJedisBean();
        jedisBean1.del(Constant.CART_NAME+loginUser.getUid());
        jedisBean1.close();

        //3.响应数据
        request.getRequestDispatcher("pay.jsp").forward(request,response);

    }

    public void XX(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
