package com.jgybzx.dao;

import com.jgybzx.domain.RouteImg;

import java.util.List;

/**
 * @author: guojy
 * @date: 2019/12/17 19:57
 * @Description:
 * @version:
 */
public interface RouteImgDao {
    List<RouteImg> findByRid(String rid);
}
