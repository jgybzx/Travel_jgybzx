package com.jgybzx.web.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jgybzx.domain.Category;
import com.jgybzx.service.CategoryService;
import com.jgybzx.utils.BeanFactoryUtils;
import com.jgybzx.web.Base.BaseServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author: guojy
 * @date: 2019/12/15 11:53
 * @Description: ${TODO}
 * @version:
 */
//http://localhost:8080/
@WebServlet("/CategoryServlet")
//WebServlet(name = "CategoryServlet",urlPatterns="/CategoryServlet")
public class CategoryServlet extends BaseServlet {
    public void showCategory(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //获取参数（没有）
        //2.查询数据，第一次从数据查，第二次从缓存查
        CategoryService categoryService =(CategoryService)BeanFactoryUtils.getBean("CategoryService");
        List<Category> list = categoryService.showCategory();
        //3.集合数据转json，返回
        String json = new ObjectMapper().writeValueAsString(list);
//        System.out.println(json);
        response.getWriter().print(json);
    }

    public void  temp (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }
}
