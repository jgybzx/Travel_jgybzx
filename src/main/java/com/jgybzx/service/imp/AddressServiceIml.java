package com.jgybzx.service.imp;

import com.jgybzx.dao.AddressDao;
import com.jgybzx.domain.Address;
import com.jgybzx.service.AddressService;
import com.jgybzx.utils.MyBatisUtils;
import org.apache.ibatis.session.SqlSession;

/**
 * @author: guojy
 * @date: 2019/12/18 20:54
 * @Description:
 * @version:
 */
public class AddressServiceIml implements AddressService {
    @Override
    public Address findAddressByid(int aid) {
        SqlSession sqlSession = MyBatisUtils.openSession();
        AddressDao addressDao = sqlSession.getMapper(AddressDao.class);
        Address address = addressDao.findAddressByid(aid);
        MyBatisUtils.close(sqlSession);
        return address;
    }
}
