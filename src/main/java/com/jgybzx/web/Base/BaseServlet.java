package com.jgybzx.web.Base;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;

/**
 * @author: guojy
 * @date: 2019/12/12 15:05
 * @Description: ${TODO}
 * @version:
 */

/**
 *         //反射获取实例对象:三种方式：Class.forName("全类名"),  类.class, 获得实例化对象.getClass()
 *         try {
 *             //1。Class<?> aClass = Class.forName("com.jgybzx.web.Base.BaseServlet");
 *             //2。Class<BaseServlet> baseServletClass = BaseServlet.class;
 *
 *             //3。BaseServlet baseServlet = new BaseServlet();
 *             //Class<? extends BaseServlet> aClass1 = baseServlet.getClass();
 *             //综合考虑，第一和第二种，都需要知道类名，那么以后类名改的话，此处的代码还要改，所以使用第三种，类.class
 *             //此处一定有疑问，第三种也要使用一个对象才行，此处知识点：this关键字就代表当前对象
 *             Class aClass = this.getClass();
 *             //获取类的public方法aClass.getMethod(方法名,方法参数类型)
 *             Method save = aClass.getMethod("save", HttpServletRequest.class, HttpServletResponse.class);
 *             //调用执行方法
 *             save.invoke(this, request, response);
 *             //获取类的private和protected方法，注意，如果使用getMethod获得方法需要暴力反射
 *             //或者使用getDeclaredMethod,建议都需要暴力破解
 *             Method delete = aClass.getMethod("delete", HttpServletRequest.class, HttpServletResponse.class);
 *             delete.setAccessible(true);
 *             delete.invoke(this,request,response);
 *         } catch (Exception e) {
 *             e.printStackTrace();
 *         }
 */
//http://localhost:8080/travel/BaseServlet
@WebServlet("/BaseServlet")
//WebServlet(name = "BaseServlet",urlPatterns="/BaseServlet")
public class BaseServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //反射获取实例对象:三种方式：Class.forName("全类名"),  类.class, 获得实例化对象.getClass()
        try {
            //获取action，
            String action = request.getParameter("action");
            System.out.println(action);
            Class aClass = this.getClass();
            Method method = aClass.getMethod(action, HttpServletRequest.class, HttpServletResponse.class);
            method.setAccessible(true);
            method.invoke(this,request,response);

        } catch (Exception e) {
            System.out.println("BaseServlet报错");
            e.printStackTrace();
        }
    }


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }

}
