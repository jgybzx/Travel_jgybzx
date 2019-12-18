package com.jgybzx.web.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jgybzx.constant.Constant;
import com.jgybzx.domain.Address;
import com.jgybzx.domain.Cart;
import com.jgybzx.domain.User;
import com.jgybzx.service.UserService;
import com.jgybzx.utils.BeanFactoryUtils;
import com.jgybzx.utils.RedisUtils;
import com.jgybzx.web.Base.BaseServlet;
import org.apache.commons.lang3.StringUtils;
import redis.clients.jedis.Jedis;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

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
        Cart cart;
        String json = jedisBean.get(Constant.CART_NAME + loginUser.getUid());
        if (StringUtils.isBlank(json)) {
            cart = new Cart();
        } else {
            cart = new ObjectMapper().readValue(json, Cart.class);
        }

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


    public void XXXIII(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }
}
