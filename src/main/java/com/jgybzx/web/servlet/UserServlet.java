package com.jgybzx.web.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jgybzx.domain.ResultInfo;
import com.jgybzx.domain.User;
import com.jgybzx.service.UserService;
import com.jgybzx.service.imp.UserServiceIml;
import com.jgybzx.utils.SendCodeUtils;
import com.jgybzx.web.Base.BaseServlet;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * @author: guojy
 * @date: 2019/12/12 11:58
 * @Description: ${TODO}
 * @version:
 */
//http://localhost:8080/
@WebServlet("/UserServlet")
//WebServlet(name = "UserServlet",urlPatterns="/UserServlet")
public class UserServlet extends BaseServlet {
   /* protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if ("register".equals(action)) {
            //注册
            this.UserRegister(request, response);
        }else if("ifNameExistAjax".equals(action)){
            this.ifNameExistAjax(request,response);
        }else if("ifPhoneExistAjax".equals(action)){
            this.ifPhoneExistAjax(request,response);
        }
    }*/

    /**
     * 注册   获取数据并封装
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void register(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        try {
            //从session中获取验证码
            String code = (String) request.getSession().getAttribute("code");
            System.out.println("session中获取验证码"+code);
            //获取前台输入的验证码
            String smsCode = request.getParameter("smsCode");
            System.out.println("前台输入的验证码"+smsCode);
            //判断用户输入的验证码是否为空和 是否一致
            if (!StringUtils.isBlank(smsCode) && code.equals(smsCode)) {
                //获取表单数据
                Map<String, String[]> map = request.getParameterMap();
                //数据封装
                User user = new User();
                BeanUtils.populate(user, map);
                //调用service，传递数据
                UserService userService = new UserServiceIml();
                userService.saveUser(user);
                //通知用户是否成功，重定向到注册成功页面  register_ok.jsp
                response.sendRedirect(request.getContextPath() + "/register_ok.jsp");
            } else {
                //e.getMessage()，得到错误信息，请求转发到注册页面进行显示
                request.setAttribute("msg", "验证码不正确");
                request.getRequestDispatcher("/register.jsp").forward(request, response);
            }

        } catch (Exception e) {
            //e.getMessage()，得到错误信息，请求转发到注册页面进行显示
            request.setAttribute("msg", e.getMessage());
            request.getRequestDispatcher("/register.jsp").forward(request, response);
        }

    }

    /**
     * ajax方式，判断用户名是否存在，ajax的方式不能请求转发和重定向，只能使用 response.getWriter（）方式
     * 使用流返回 不支持对象格式 只支持字符串 或者 字节
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void ifNameExistAjax(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        //取出名字判断是否存在
        String usernameVal = request.getParameter("usernameVal");
        UserService userService = new UserServiceIml();
        ResultInfo resultInfo = userService.ifNameExistAjax(usernameVal);
        //将对象转换为json
        String json = new ObjectMapper().writeValueAsString(resultInfo);
        response.getWriter().print(json);
    }

    /**
     * 判断用户名和手机号是否存在
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void ifPhoneExistAjax(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //取出手机号判断是否已经存在
        String telephoneVal = request.getParameter("telephoneVal");
        UserService userService = new UserServiceIml();
        ResultInfo resultInfo = userService.ifPhoneExistAjax(telephoneVal);
        //将对象转换为json
        String json = new ObjectMapper().writeValueAsString(resultInfo);
        response.getWriter().print(json);
    }

    /**
     * 接收手机号  发送验证码
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void sendCode(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ResultInfo resultInfo = null;
        try {
            //取出手机号,调用发送验证码方法
            String phone = request.getParameter("phone");
            String code = SendCodeUtils.SendCode(phone);
            System.out.println("手机号"+phone);
            System.out.println("验证码"+code);
            //将验证吗放在session中，方便获取对比 用户输入的验证吗
            request.getSession().setAttribute("code", code);
            //响应页面
            resultInfo = new ResultInfo(true, "", "发送短信成功");
            System.out.println("发送短信成功");
        } catch (Exception e) {
            //此时可能发送成功，也可能发送失败，所以想到使用ResultInfo
            //失败
            resultInfo = new ResultInfo(false, "发送短信失败", "");
            System.out.println("发送短信失败");
        } finally {
            String codeMsg = new ObjectMapper().writeValueAsString(resultInfo);
            System.out.println(resultInfo);
            response.getWriter().print(codeMsg);
        }
    }
}
