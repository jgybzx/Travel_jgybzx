package com.jgybzx.dao;

import com.jgybzx.domain.Address;
import com.jgybzx.domain.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

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


    User findByNamePws(@Param("username") String username, @Param("password") String password);

    User findByPhone(String telephone);

    User findById(Integer uid);

    void updateUser(User user);

    List<Address> findUserAddress(Integer uid);

    void saveAddress(Address address);

    void setAddDef(Integer uid);

    void initAdder(Integer uid);

}
