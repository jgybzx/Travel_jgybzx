package com.jgybzx.web.servlet;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.jgybzx.service.OrderService;
import com.jgybzx.utils.BeanFactoryUtils;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: guojy
 * @date: 2019/12/20 12:22
 * @Description: ${TODO}
 * @version:
 */
//http://localhost:8080/
@WebServlet("/CallBackServlet")
//WebServlet(name = "CallBackServlet",urlPatterns="/CallBackServlet")
public class CallBackServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        /**
         * 一旦支付成功，微信会访问我们的回调函数，既然是外部访问，所以不要继承BaseServlet，要不然还要传递action
         * BaseServlet 是使用反射的方式，执行方法
         */

        //支付成功的回调函数，获取回调信息
        //回复的信息是以xml格式，需要 解析，使用 XMLMapper,回复的信息也是键值对，解析成map
        ServletInputStream inputStream = request.getInputStream();
        Map map = new XmlMapper().readValue(inputStream, Map.class);

        System.out.println(map);
        //处理订单状态，state 为1，已支付状态
        OrderService orderService = (OrderService) BeanFactoryUtils.getBean("OrderService");
        orderService.changeOrderState(map);


        //告诉微信，收到请求，需要给微信返回 几个数据，键值对的方式，所以使用map
        //https://pay.weixin.qq.com/wiki/doc/api/native.php?chapter=9_2
        //需要设置返回信息  return_code = SUCCESS    return_msg = ok
        Map<String,String> resultMap = new HashMap<>();
        resultMap.put("return_code","SUCCESS");
        resultMap.put("return_msg","ok");

        //以xml文件接收，也需要以xml返回给微信终端，如果不给 微信返回信息，微信会一直尝试访问我们
        String resultMapStr = new XmlMapper().writeValueAsString(map);
        response.getWriter().print(resultMapStr);

        //至此与微信的交互结束，并且支付成功，但是由于servlet只能转发或响应一次，所以不能转到支付成功的页面
        //所以需要页面，ajax请求，定时访问数据库，如果订单状态发生变化，那么，跳转页面支付成功

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }
}
