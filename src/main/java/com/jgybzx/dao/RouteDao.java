package com.jgybzx.dao;

import com.jgybzx.domain.Route;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author: guojy
 * @date: 2019/12/17 12:07
 * @Description:
 * @version:
 */
public interface RouteDao {
    //根据 参数获取 cid 的分页数据
    List<Route> findData(@Param("cid") String cid, @Param("pageSize") int pageSize, @Param("startindex") int startindex);

    int totalCountByCid(String cid);

    //根据搜索框和分类id动态查询
    List<Route> findByCondition(@Param("cid") String cid, @Param("searchName") String searchName, @Param("pageSize") int pageSize, @Param("startindex") int startindex);

    int totalCountByCondition(@Param("cid") String cid, @Param("searchName") String searchName);


    //根据id获得数据
    Route showDetail(String rid);
}
