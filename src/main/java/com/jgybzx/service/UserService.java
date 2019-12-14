package com.jgybzx.service;

import com.jgybzx.domain.Address;
import com.jgybzx.domain.ResultInfo;
import com.jgybzx.domain.User;

import java.util.List;

/**
 * @author: guojy
 * @date: 2019/12/12 9:28
 * @Description:
 * @version:
 */
public interface UserService {
    void saveUser(User user);

    ResultInfo ifNameExistAjax(String usernameVal);

    ResultInfo ifPhoneExistAjax(String telephoneVal);


    ResultInfo findByNamePws(String username, String password);

    ResultInfo findByPhone(String telephone);

    User findById(Integer uid);

    void updateUser(User user);

    List<Address> findUserAddress(Integer uid);

    ResultInfo saveAddress(Address address);

    void setAddDef(Integer uid,Integer addressId);
}
