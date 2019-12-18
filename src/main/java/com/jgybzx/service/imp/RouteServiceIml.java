package com.jgybzx.service.imp;

import com.jgybzx.dao.RouteDao;
import com.jgybzx.domain.PageBean;
import com.jgybzx.domain.Route;
import com.jgybzx.service.RouteService;
import com.jgybzx.utils.MyBatisUtils;
import org.apache.ibatis.session.SqlSession;

import java.io.StringReader;
import java.util.List;

/**
 * @author: guojy
 * @date: 2019/12/17 9:33
 * @Description:
 * @version:
 */
public class RouteServiceIml implements RouteService {
    /**
     * 根据不同的类别以及页码 获取分页数据，并且返回 pageBean对象，里边封装了的页面显示需要的信息，
     * 以及页码条需要的信息
     *
     * @param pageNumber 当前页码
     * @param pageSize   分页大小
     * @param cid        类别
     * @return
     */
    @Override
    public PageBean<Route> findAllByCid(int pageNumber, int pageSize, String cid) {
        //1.准备接受数据 的对象
        PageBean<Route> pageBean = new PageBean<>();
        List<Route> routes = null;
        int totalCount = 0;
        //2.，调用 dao获取分页 数据数据
        SqlSession sqlSession = MyBatisUtils.openSession();
        try {
            RouteDao routeDao = sqlSession.getMapper(RouteDao.class);
            //计算分页 所需要 的参数，limit startindex  pagesize
            int startindex = (pageNumber - 1) * pageSize;
            //获取分页数据
            routes = routeDao.findData(cid, pageSize, startindex);

            //获取总记录数
            totalCount = routeDao.totalCountByCid(cid);
        } finally {
            MyBatisUtils.close(sqlSession);
        }
        //计算总页数，注意余数的情况
        int totalPage = totalCount % pageSize == 0 ? totalCount / pageSize : totalCount / pageSize + 1;


        // 2。封装数据
        pageBean.setTotalCount(totalCount);
        pageBean.setTotalPage(totalPage);
        pageBean.setPageSize(pageSize);
        pageBean.setPageNumber(pageNumber);
        pageBean.setData(routes);
        return pageBean;
    }

    /**
     * 搜索框根据条件搜索，四种情况，动态sql
     *
     * @param pageNumber
     * @param pageSize
     * @param cid
     * @param searchName
     * @return
     */
    @Override
    public PageBean<Route> findByCondition(int pageNumber, int pageSize, String cid, String searchName) {
        //1.准备接受数据 的对象
        PageBean<Route> pageBean = new PageBean<>();
        List<Route> routes = null;
        int totalCount = 0;
        //2.，调用 dao获取分页 数据数据
        SqlSession sqlSession = MyBatisUtils.openSession();
        try {
            RouteDao routeDao = sqlSession.getMapper(RouteDao.class);
            //计算分页 所需要 的参数，limit startindex  pagesize
            int startindex = (pageNumber - 1) * pageSize;

            //动态查询获取分页数据
            routes = routeDao.findByCondition(cid, searchName, pageSize, startindex);

            //获取总记录数
            totalCount = routeDao.totalCountByCondition(cid, searchName);
        } finally {
            MyBatisUtils.close(sqlSession);
        }
        //计算总页数，注意余数的情况
        int totalPage = totalCount % pageSize == 0 ? totalCount / pageSize : totalCount / pageSize + 1;


        // 2。封装数据
        pageBean.setTotalCount(totalCount);
        pageBean.setTotalPage(totalPage);
        pageBean.setPageSize(pageSize);
        pageBean.setPageNumber(pageNumber);
        pageBean.setData(routes);
        return pageBean;
    }

    /**
     * 根据id查询数据
     *
     * @param rid
     * @return
     */
    @Override
    public Route showDetail(String rid) {
        Route route = new Route();
        SqlSession sqlSession = MyBatisUtils.openSession();
        RouteDao routeDao = sqlSession.getMapper(RouteDao.class);

        route = routeDao.showDetail(rid);

        MyBatisUtils.close(sqlSession);
        return route;
    }


}
