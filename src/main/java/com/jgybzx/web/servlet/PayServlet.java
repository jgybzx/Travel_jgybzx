package com.jgybzx.web.servlet;

import com.jgybzx.service.OrderService;
import com.jgybzx.utils.BeanFactoryUtils;
import com.jgybzx.utils.PayUtils;
import com.jgybzx.web.Base.BaseServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author: guojy
 * @date: 2019/12/20 12:04
 * @Description: ${TODO}
 * @version:
 */
//http://localhost:8080/
@WebServlet("/PayServlet")
//WebServlet(name = "PayServlet",urlPatterns="/PayServlet")
public class PayServlet extends BaseServlet {
    public void CreateCodeUrl(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //接受 参数
        String oid = request.getParameter("oid");
        String totalPrice = request.getParameter("totalPrice");

        System.out.println("oid = " + oid);
        //获得支付链接，第一个参数：订单号，第二个：钱，单位  分
        String payUrl = PayUtils.createOrder(oid, 1);
        System.out.println("payUrl = " + payUrl);


        //响应 页面，生成 二维码
        request.setAttribute("payUrl",payUrl);
        //响应订单单号，目的：页面ajax定时根据订单号访问数据库，检查订单状态
        request.setAttribute("oid",oid);
        request.setAttribute("totalPrice",totalPrice);

        request.getRequestDispatcher("pay.jsp").forward(request,response);

    }

    /**
     * 页面  ajax定时访问数据库，检查订单状态
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void sheckOrderState(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String oid = request.getParameter("oid");
        OrderService orderService = (OrderService) BeanFactoryUtils.getBean("OrderService");
        //返回状态码
        String state =orderService.sheckOrderState(oid);

        //响应数据
        response.getWriter().print(state);
    }
}
