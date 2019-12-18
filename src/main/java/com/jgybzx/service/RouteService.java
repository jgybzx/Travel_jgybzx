package com.jgybzx.service;

import com.jgybzx.domain.PageBean;
import com.jgybzx.domain.Route;

import java.util.List;

/**
 * @author: guojy
 * @date: 2019/12/17 9:32
 * @Description: 路线
 * @version:
 */
public interface RouteService {

    //通过分类的id和 pageNummber和pageSize进行分页查询
    PageBean<Route> findAllByCid(int pageNumber, int pageSize, String cid);

    //通过搜索框的数据和 分类id 以及 pageNummber和pageSize进行分页查询
    PageBean<Route> findByCondition(int pageNumber, int pageSize, String cid, String searchName);

    //获取线路的详细信息
    Route showDetail(String rid);
}
