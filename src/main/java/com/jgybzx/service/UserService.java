package com.jgybzx.service;

import com.jgybzx.domain.ResultInfo;
import com.jgybzx.domain.User;

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
}
