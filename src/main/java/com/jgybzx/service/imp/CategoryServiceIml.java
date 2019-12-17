package com.jgybzx.service.imp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jgybzx.dao.CategoryDao;
import com.jgybzx.domain.Category;
import com.jgybzx.service.CategoryService;
import com.jgybzx.utils.MyBatisUtils;
import com.jgybzx.utils.RedisUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSession;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.util.List;

/**
 * @author: guojy
 * @date: 2019/12/15 11:49
 * @Description: CategorySevice的实现类
 * @version:
 */
public class CategoryServiceIml implements CategoryService {
    @Override
    public List<Category> showCategory() {
        //首先判断缓存中是否有数据
        Jedis jedisBean = RedisUtils.getJedisBean();
        String category_list_json = jedisBean.get("CATEGORY_LIST_JSON");
        List<Category> list = null;
        if (StringUtils.isBlank(category_list_json)) {//如果缓存没数据，那么放入数据
            SqlSession sqlSession = MyBatisUtils.openSession();
            try {
                CategoryDao categoryDao = sqlSession.getMapper(CategoryDao.class);
                list = categoryDao.showCategory();
                //将list转化为json存入缓存
                String json = new ObjectMapper().writeValueAsString(list);
                jedisBean.set("CATEGORY_LIST_JSON", json);
//                System.out.println("数据库中的数据");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                MyBatisUtils.close(sqlSession);
            }
        } else {//如果缓存中有数据
            //那么将缓存中的json数据，转换成list
            try {
                list = new ObjectMapper().readValue(category_list_json, List.class);
//                System.out.println("缓存中的数据");
            } catch (IOException e) {
                System.out.println("转换json失败");
                e.printStackTrace();
            }
        }
        //释放资源
        jedisBean.close();
        return list;
    }
}
