package com.jgybzx.service.imp;

import com.jgybzx.dao.UserDao;
import com.jgybzx.domain.Address;
import com.jgybzx.domain.ResultInfo;
import com.jgybzx.domain.User;
import com.jgybzx.service.UserService;
import com.jgybzx.utils.Md5Utils;
import com.jgybzx.utils.MyBatisUtils;
import jdk.nashorn.internal.ir.CallNode;
import org.apache.ibatis.session.SqlSession;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: guojy
 * @date: 2019/12/12 9:28
 * @Description: 实现UserService 实现业务处理
 * @version:
 */
public class UserServiceIml implements UserService {


    @Override
    public void saveUser(User user) {
        SqlSession sqlSession = MyBatisUtils.openSession();
        try {
            UserDao userDao = sqlSession.getMapper(UserDao.class);
            //使用工具类加密 密码  加密类型 md5，
            //md5 不可逆加密，之所以可以从加密后的字符转到加密前的字符，是因为改技术同一个字符加密后的字符不是动态的
            //也就是说，有一个数据库存储了 加密器前和加密后的两个字符串
            //https://www.cmd5.com/  创建的记录约90万亿条，占用硬盘超过500TB
            String passwordByMd5 = Md5Utils.encodeByMd5(user.getPassword());
            user.setPassword(passwordByMd5);

            //在添加数据之前需要进行用户名和手机号验证，这两个是唯一的
            int count = userDao.nameIfExists(user.getUsername());//数据库检索相同名字的行数count(*)
            int count1 = userDao.phoneIfExists(user.getTelephone());
            if (count > 0) {//大于0，代表存在，抛出异常，由servlet的catch捕获
                System.out.println("用户已存在");
                throw new RuntimeException("用户已存在");
                //一旦抛出异常，后续代码 保存用户的代码不会执行
            }
            if (count1 > 0) {
                System.out.println("手机号已存在");
                throw new RuntimeException("手机号已存在");
            }

            userDao.save(user);
        } finally { //如果执行sql语句报错，那么就会导致资源无法释放，所以需要try finally,最后释放掉资源
            MyBatisUtils.close(sqlSession);
        }
    }

    @Override//ajax判断名字是否存在
    //第一次考虑，返回一个行数，让servlet判断是否该如何处理,但是由于ajax不能使用请求或者转发，只能使用getWriter(),
    //使用流返回 不支持对象格式 只支持字符串 或者 字节，然而我们不仅需要返回一个行数，还要让用户知道一个信息
    //如果我们此时仅仅给servlet返回一个值，让servlet来处理返回什么，这就不对了，处理 业务逻辑是service的活。所以要在service中返回行数和信息
    //至此，我们需要一个对象，里边的属性是，查询出来的行数，以及异常信息
    public ResultInfo ifNameExistAjax(String usernameVal) {
        ResultInfo resultInfo = new ResultInfo();
        int count;
        SqlSession sqlSession = MyBatisUtils.openSession();
        UserDao userDao = sqlSession.getMapper(UserDao.class);
        count = userDao.nameIfExists(usernameVal);
        if (count > 0) {//表示已经有数据
            resultInfo.setFlag(false);
            resultInfo.setErrorMsg("用户已经存在");
        } else {
            resultInfo.setFlag(true);
            resultInfo.setData("用户名可用");
        }
        MyBatisUtils.close(sqlSession);
        return resultInfo;
    }

    @Override
    public ResultInfo ifPhoneExistAjax(String telephoneVal) {
        ResultInfo resultInfo = new ResultInfo();
        int count;
        SqlSession sqlSession = MyBatisUtils.openSession();
        UserDao userDao = sqlSession.getMapper(UserDao.class);
        count = userDao.phoneIfExists(telephoneVal);
        if (count > 0) {//表示已经有数据
            resultInfo.setFlag(false);
            resultInfo.setErrorMsg("手机已经存在");
        } else {
            resultInfo.setFlag(true);
            resultInfo.setData("手机号可用");
        }
        MyBatisUtils.close(sqlSession);
        return resultInfo;
    }

    /**
     * 验证用户名和密码
     *
     * @param username
     * @param password
     * @return
     */
    @Override
    public ResultInfo findByNamePws(String username, String password) {

        ResultInfo resultInfo = null;
        SqlSession sqlSession = MyBatisUtils.openSession();
        try {
            UserDao userDao = sqlSession.getMapper(UserDao.class);
            //通过用户名密码，查询数据库，返回一个用户的信息，将其放到resultInfo的data中，返回给页面
            //因为登录完，页面还要显示 欢迎 ***，所以就可以直接从resultInfo的data中取出对象，获得username
            System.out.println("username = " + username);
            System.out.println("password = " + password);
            User user = userDao.findByNamePws(username, password);
            System.out.println("user = " + user);
            //判断是否有这个用户
            if (user == null) {//没有找到数据
                resultInfo = new ResultInfo(false, "用户名或密码不正确", null);
            } else {//如果找到 数据，将ueser返回
                resultInfo = new ResultInfo(true, "", user);
            }
        } finally {
            MyBatisUtils.close(sqlSession);
        }
        return resultInfo;
    }

    @Override
    public ResultInfo findByPhone(String telephone) {
        ResultInfo resultInfo = null;
        SqlSession sqlSession = MyBatisUtils.openSession();
        try {
            UserDao userDao = sqlSession.getMapper(UserDao.class);
            User user = userDao.findByPhone(telephone);
            if (user != null) {
                resultInfo = new ResultInfo(true, "", user);

            } else {
                resultInfo = new ResultInfo(false, "手机号输入错误", "");
            }
        } finally {
            MyBatisUtils.close(sqlSession);
        }
        return resultInfo;
    }

    @Override
    public User findById(Integer uid) {
        User user = null;
        SqlSession sqlSession = MyBatisUtils.openSession();
        try {
            UserDao userDao = sqlSession.getMapper(UserDao.class);
            user=userDao.findById(uid);
        } finally {
            MyBatisUtils.close(sqlSession);
        }
        return user;
    }

    @Override
    public void updateUser(User user) {
        SqlSession sqlSession = MyBatisUtils.openSession();

        try {
            UserDao userDao = sqlSession.getMapper(UserDao.class);
            userDao.updateUser(user);
        }finally {
            MyBatisUtils.close(sqlSession);
        }
    }

    @Override
    public List<Address> findUserAddress(Integer uid) {
        List<Address> addresses = new ArrayList<>();
        SqlSession sqlSession = MyBatisUtils.openSession();
        try {
            UserDao userDao = sqlSession.getMapper(UserDao.class);
            addresses =userDao.findUserAddress(uid);
            System.out.println(addresses);
        }finally {
            MyBatisUtils.close(sqlSession);
        }
        return addresses;
    }

    @Override
    public ResultInfo saveAddress(Address address) {
        ResultInfo resultInfo =null;
        SqlSession sqlSession = MyBatisUtils.openSession();

        try {
            UserDao userDao = sqlSession.getMapper(UserDao.class);
            //保存不会返回影响几行，只会直接报错，所以如果报错失败直接进catch
            userDao.saveAddress(address);
            resultInfo = new ResultInfo(true,"","保存地址成功");
        } catch (Exception e) {
            resultInfo = new ResultInfo(false , "保存地址失败" ,"");
        } finally {
            MyBatisUtils.close(sqlSession);
        }

        return resultInfo;
    }

    @Override
    public void setAddDef(Integer uid, Integer addressId) {
        SqlSession sqlSession = MyBatisUtils.openSession();
        UserDao userDao = sqlSession.getMapper(UserDao.class);
        userDao.initAdder(uid);
        userDao.setAddDef(addressId);
        MyBatisUtils.close(sqlSession);

    }

}
