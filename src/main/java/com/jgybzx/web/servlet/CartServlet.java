package com.jgybzx.web.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jgybzx.constant.Constant;
import com.jgybzx.domain.Cart;
import com.jgybzx.domain.CartItme;
import com.jgybzx.domain.Route;
import com.jgybzx.domain.User;
import com.jgybzx.service.RouteService;
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

/**
 * @author: guojy
 * @date: 2019/12/18 16:06
 * @Description: ${TODO}
 * @version:
 */
//http://localhost:8080/
@WebServlet("/CartServlet")
//WebServlet(name = "CartServlet",urlPatterns="/CartServlet")
public class CartServlet extends BaseServlet {
    /**
     * 添加数据到购物车，由于需要长期储存，所以要存储在数据库种
     * 存储方式有两种，1：mysql  2：redis
     * 存在mysql中需要新建表，本次选择存在redis中
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void addCart(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1.获得数据
        String rid = request.getParameter("rid");
        String count = request.getParameter("count");
        System.out.println("count = " + count);
        //获取当前登陆用户
        User loginUser = (User) request.getSession().getAttribute(Constant.loginUser);


        //习惯判断是否有用户登录
        if (loginUser == null) {
            //如果没有登陆 ，直接重定向到主页
            response.sendRedirect(request.getContextPath());
            return; //直接方法结束
        }

        //调用购物车的添加方法，购物车数据存在 redies中，key使用uid区分  cart_uid,
        Cart cart = null;//注意，不能直接new，如果直接new，每次都会是一个新的购物车？啥不能new
        //从购物车中取出 数据
        Jedis jedisBean = RedisUtils.getJedisBean();
        String json = jedisBean.get(Constant.CART_NAME + loginUser.getUid());
        if (StringUtils.isBlank(json)) {//如果为空，new 一个购物车
            cart = new Cart();
        } else {
            //如果不为空，获取购物车
            cart = new ObjectMapper().readValue(json, Cart.class);
        }


        //创建 购物项
        CartItme cartItme = new CartItme();
        cartItme.setCount(Integer.valueOf(count));
//        cartItme.setRoute();

        //获取Route，前台传递了，rid，数据查询
        RouteService routeService = (RouteService) BeanFactoryUtils.getBean("RouteService");
        Route route = routeService.showDetail(rid);
        cartItme.setRoute(route);

        //加入购物车
        cart.addCart(cartItme);

        //更新缓存数据
        json = new ObjectMapper().writeValueAsString(cart);
        //写入redis
        jedisBean.set(Constant.CART_NAME + loginUser.getUid(), json);
        //释放资源
        jedisBean.close();
        request.setAttribute(Constant.CART, cart);
        request.getRequestDispatcher("cart.jsp").forward(request, response);
    }

    /**
     * 点击页面的购物车，查询数据
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    //findCart
    public void findCart(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //获取用户
        User loginUser = (User) request.getSession().getAttribute(Constant.loginUser);
        //根据uid，获得缓存中的数据
        Jedis jedisBean = RedisUtils.getJedisBean();
        Cart redisCart = getRedisCart(jedisBean, loginUser.getUid());
        jedisBean.close();
        request.setAttribute(Constant.CART, redisCart);
        request.getRequestDispatcher("cart.jsp").forward(request, response);
    }

    /**
     * 从缓存中 获取购物车对象
     *
     * @param jedis
     * @param uid
     * @return
     * @throws IOException
     */
    private Cart getRedisCart(Jedis jedis, Integer uid) throws IOException {
        String json = jedis.get(Constant.CART_NAME + uid);
        if (StringUtils.isBlank(json)) {
            return new Cart();
        } else {
            Cart cart = new ObjectMapper().readValue(json, Cart.class);
            return cart;
        }
    }

    //removeCartItemByRid

    /**
     * 购物车删除 功能，前台传递 一个rid
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void removeCartItemByRid(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String rid = request.getParameter("rid");
        //获得用户数据
        User loginUser = (User) request.getSession().getAttribute(Constant.loginUser);

        //从缓存中拿到cart
        Jedis jedisBean = RedisUtils.getJedisBean();
        Cart redisCart = getRedisCart(jedisBean, loginUser.getUid());

        //删除数据
        redisCart.removeCart(Integer.valueOf(rid));

        //写回缓存
        String json = new ObjectMapper().writeValueAsString(redisCart);
        jedisBean.set(Constant.CART_NAME+loginUser.getUid(),json);
        jedisBean.close();
        request.setAttribute(Constant.CART,redisCart);
        request.getRequestDispatcher("cart.jsp").forward(request,response);
    }

    public void XXXIII(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
