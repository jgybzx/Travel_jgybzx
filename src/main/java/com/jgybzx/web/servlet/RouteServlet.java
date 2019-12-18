package com.jgybzx.web.servlet;

import com.jgybzx.domain.PageBean;
import com.jgybzx.domain.Route;
import com.jgybzx.service.RouteService;
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
 * @date: 2019/12/17 8:48
 * @Description: ${TODO}
 * @version:
 */
//http://localhost:8080/
@WebServlet("/RouteServlet")
//WebServlet(name = "RouteServlet",urlPatterns="/RouteServlet")
public class RouteServlet extends BaseServlet {
    /**
     * 根据导航栏  分类的 id  获得 数据库中的数据，并根据pagenumber和pagesize进行分页
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void findAllByCid(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1.获取 数据
        String pageNumberStr = request.getParameter("pageNumber");
        String pageSizeStr = request.getParameter("pageSize");
        String cid = request.getParameter("cid");
        String searchName = request.getParameter("searchName");
        //2.转换成Integer类型，pageSize暂时写

        int pageSize = 5;

        //解决pageNummber 非数字问题
        int pageNumber = 1;
        try {
            pageNumber = Integer.parseInt(pageNumberStr);
            if (pageNumber < 1) {//结局pageNumber 小于1的问题，比如上一页
                pageNumber = 1;
            }
        } catch (NumberFormatException e) {
            System.out.println("pageNumber = " + pageNumber + ":  类型转换异常");
        }
        //3.调用service，传递参数，获取数据
        RouteService routeService = (RouteService) BeanFactoryUtils.getBean("RouteService");
//        PageBean<Route> pageBean = routeService.findAllByCid(pageNumber, pageSize, cid);
        PageBean<Route> pageBean = routeService.findByCondition(pageNumber, pageSize, cid, searchName);
//        System.out.println(pageBean);
        //4.请求转发数据
        request.setAttribute("pageBean", pageBean);
        //返回当前分类的cid
        request.setAttribute("cid", cid);
        //返回搜索框中 的 东西，以便在页面显示
        request.setAttribute("searchName", searchName);
        request.getRequestDispatcher("route_list.jsp").forward(request, response);
    }

    /**
     * 搜索框根据条件搜索，四种情况，动态sql
     * 1、页面加载完，不输入条件直接搜，出来的应该是数据库全部内容
     * 2、页面加载完，输入条件直接搜 ，模糊查询
     * 3、已经点击了某个分类，不输条件直接搜，搜出该分类下的全部内容
     * 4、已经点击了某个分类，输入条件模糊查询
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
   /* public void findByCondition(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1.获取 数据
        String pageNumberStr = request.getParameter("pageNumber");
        String pageSizeStr = request.getParameter("pageSize");
        String cid = request.getParameter("cid");
        String searchName = request.getParameter("searchName");
        //2.转换成Integer类型，pageSize暂时写

        int pageSize = 5;

        //解决pageNummber 非数字问题
        int pageNumber = 1;
        try {
            pageNumber = Integer.parseInt(pageNumberStr);
            if(pageNumber<1){//结局pageNumber 小于1的问题，比如上一页
                pageNumber=1;
            }
        } catch (NumberFormatException e) {
            System.out.println("类型转换异常");
        }
        //3.调用service，传递参数，获取数据
        RouteService routeService = (RouteService) BeanFactoryUtils.getBean("RouteService");
        PageBean<Route> pageBean = routeService.findByCondition(pageNumber, pageSize, cid,searchName);
        //4.请求转发数据
        request.setAttribute("pageBean",pageBean);
        //返回当前分类的cid
        request.setAttribute("cid",cid);
        request.getRequestDispatcher("route_list.jsp").forward(request,response);
    }*/

    /**
     * 线路详情，根据id获得线路详情，线路详情页面，需要嵌套查询
     * 包括 线路信息，名字，简介，价格
     *     线路的相关图片  1对多
     *     所属商家信息名字，电话，地址 1对1
     *     线路所属于的分类  1对1
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void showDetail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1.获取线路id
        String rid = request.getParameter("rid");
        String cid = request.getParameter("cid");

        //2.调用service获取返回的数据，
        RouteService routeService =(RouteService) BeanFactoryUtils.getBean("RouteService");
        Route route=routeService.showDetail(rid);
        //3.请求转发页面，
        request.setAttribute("route",route);
        request.setAttribute("cid",cid);
        request.getRequestDispatcher("route_detail.jsp").forward(request,response);
    }

    public void xxxx(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
