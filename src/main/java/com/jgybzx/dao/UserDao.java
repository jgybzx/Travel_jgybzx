package com.jgybzx.dao;

import com.jgybzx.domain.User;

/**
 * @author: guojy
 * @date: 2019/12/12 12:04
 * @Description:
 * @version:
 */
public interface UserDao {
    //判断用户是否存在
    int nameIfExists(String username);

    void save(User user);

    int phoneIfExists(String telephone);


}
