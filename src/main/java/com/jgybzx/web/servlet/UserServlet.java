package com.jgybzx.web.servlet;

import com.aliyuncs.exceptions.ClientException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jgybzx.domain.Address;
import com.jgybzx.domain.ResultInfo;
import com.jgybzx.domain.User;
import com.jgybzx.service.UserService;
import com.jgybzx.service.imp.UserServiceIml;
import com.jgybzx.utils.*;
import com.jgybzx.web.Base.BaseServlet;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import redis.clients.jedis.Jedis;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.awt.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

/**
 * @author: guojy
 * @date: 2019/12/12 11:58
 * @Description: ${TODO}
 * @version:
 */
//http://localhost:8080/
@WebServlet("/UserServlet")
@MultipartConfig
//WebServlet(name = "UserServlet",urlPatterns="/UserServlet")
public class UserServlet extends BaseServlet {

    /**
     * 注册   获取数据并封装
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void register(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        Jedis jedisBean =null;
        try {
            /*//从session中获取验证码
            String code = (String) request.getSession().getAttribute("code");
            System.out.println("session中获取验证码" + code);*/

            //优化，从缓存redis中获取验证码 REGISTER_CODE_
            //获取手机号
            String telephone = request.getParameter("telephone");
            //获取jedis对象
             jedisBean = RedisUtils.getJedisBean();
            String code =jedisBean.get("REGISTER_CODE_"+telephone);



            //获取前台输入的验证码
            String smsCode = request.getParameter("smsCode");
            System.out.println("前台输入的验证码" + smsCode);
            //判断用户输入的验证码是否为空和 是否一致
            if (!StringUtils.isBlank(smsCode) && code.equals(smsCode)) {
                //如果一致，清空缓存中的相关数据
                jedisBean.del("REGISTER_CODE_"+telephone);
                //获取表单数据
                Map<String, String[]> map = request.getParameterMap();
                //数据封装
                User user = new User();
                BeanUtils.populate(user, map);
                //调用service，传递数据
                UserService userService = (UserService)BeanFactoryUtils.getBean("UserService");
                userService.saveUser(user);
                //通知用户是否成功，重定向到注册成功页面  register_ok.jsp
                response.sendRedirect(request.getContextPath() + "/register_ok.jsp");
            } else {
                //e.getMessage()，得到错误信息，请求转发到注册页面进行显示
                request.setAttribute("msg", "验证码不正确");
                request.getRequestDispatcher("/register.jsp").forward(request, response);
            }

        } catch (Exception e) {
            //e.getMessage()，得到错误信息，请求转发到注册页面进行显示
            request.setAttribute("msg", e.getMessage());
            request.getRequestDispatcher("/register.jsp").forward(request, response);
        }finally {
            jedisBean.close();
        }

    }

    /**
     * ajax方式，判断用户名是否存在，ajax的方式不能请求转发和重定向，只能使用 response.getWriter（）方式
     * 使用流返回 不支持对象格式 只支持字符串 或者 字节
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void ifNameExistAjax(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        //取出名字判断是否存在
        String usernameVal = request.getParameter("usernameVal");
        UserService userService = (UserService)BeanFactoryUtils.getBean("UserService");
        ResultInfo resultInfo = userService.ifNameExistAjax(usernameVal);
        //将对象转换为json
        String json = new ObjectMapper().writeValueAsString(resultInfo);
        response.getWriter().print(json);
    }

    /**
     * 判断用户名和手机号是否存在
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void ifPhoneExistAjax(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //取出手机号判断是否已经存在
        String telephoneVal = request.getParameter("telephoneVal");
        UserService userService = (UserService)BeanFactoryUtils.getBean("UserService");
        ResultInfo resultInfo = userService.ifPhoneExistAjax(telephoneVal);
        //将对象转换为json
        String json = new ObjectMapper().writeValueAsString(resultInfo);
        response.getWriter().print(json);
    }

    /**
     * 接收手机号  发送验证码，注册使用
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void sendCode(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ResultInfo resultInfo = null;
        try {
            //取出手机号,调用发送验证码方法
            String phone = request.getParameter("phone");
            String code = SendCodeUtils.SendCode(phone);
            System.out.println("手机号" + phone);
            System.out.println("验证码" + code);



            /*//将验证码放在session中，方便获取对比 用户输入的验证吗
            request.getSession().setAttribute("code", code);*/
            //将验证码放在 redis中，设置有效时间,并且要考虑每个用户一个验证码，跟手机绑定
            Jedis jedisBean = RedisUtils.getJedisBean();
            jedisBean.set("REGISTER_CODE_"+phone,code);
            jedisBean.close();



            //响应页面
            resultInfo = new ResultInfo(true, "", "发送短信成功");
            System.out.println("发送短信成功");
        } catch (Exception e) {
            //此时可能发送成功，也可能发送失败，所以想到使用ResultInfo
            //失败
            resultInfo = new ResultInfo(false, "发送短信失败", "");
            System.out.println("发送短信失败");
        } finally {
            String codeMsg = new ObjectMapper().writeValueAsString(resultInfo);
            System.out.println(resultInfo);
            response.getWriter().print(codeMsg);
        }
    }

    /**
     * 用户名密码登陆
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void pwdLogin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ResultInfo resultInfo = null;

        //获取前台传递的用户名密码
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String Md5password = Md5Utils.encodeByMd5(password);
        //调用service
//        UserService userService = (UserService)BeanFactoryUtils.getBean("UserService");
        UserService userService = (UserService)BeanFactoryUtils.getBean("UserService");
        resultInfo = userService.findByNamePws(username, Md5password);

        //response.getWriter().print("数据查询到了");
        //判断 resultInfo 的flag
        if (resultInfo.getFlag()) {//如果为ture 说明有数据，那么就返回 resultInfo的 data
            //此处因为接下来要登录，然后在页面显示 欢迎***，就要想到需要把user一直保存，因为一旦登陆成功
            //其他页面也会显示 欢迎***，所以需要用到 session
            request.getSession().setAttribute("loginUser", resultInfo.getData());
        }
        //将数据返回，由于ajax请求，只能 使用 response.getWrite().print();
        String json = new ObjectMapper().writeValueAsString(resultInfo);
        response.getWriter().print(json);
    }

    /**
     * 短信验证码登陆，获得验证码
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    //phoneSendCode
    public void phoneSendCode(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            //1.获取手机号
            String login_telephone = request.getParameter("login_telephone");
            //2.发送验证码
            String code = SendCodeUtils.SendCode(login_telephone);
            System.out.println("code = " + code);
            /*//3.此处需要将验证码存入session，因为，登陆的时候，还要取出code进行判断
            request.getSession().setAttribute("loginCode", code);*/


            //优化代码，将验证码存入缓存中，一段时间失效，一个用户对象一个code，手机绑定
            Jedis jedisBean = RedisUtils.getJedisBean();
            jedisBean.set("LOGIN_CODE_"+login_telephone,code);
            jedisBean.close();

            response.getWriter().print("1");//ajax响应数据,1表示成功
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 通过手机号登陆
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    //telLogin
    public void telLogin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //如果登陆成功，需要返回用户数据，所以使用 resultInfo
        ResultInfo resultInfo = null;
        //1。获取手机号
        String telephone = request.getParameter("telephone");
        //2.获取验证码
        String smsCode = request.getParameter("smsCode");
        System.out.println(smsCode);
        /*//3.获取session中的验证码
        String loginCode = (String) request.getSession().getAttribute("loginCode");*/
        //优化代码，从缓存中取出数据，一旦登陆成功，验证码失效 LOGIN_CODE_
        Jedis jedisBean = RedisUtils.getJedisBean();
        String loginCode =jedisBean.get("LOGIN_CODE_"+telephone);


        //比对验证码是否相同
        if (StringUtils.equals(smsCode, loginCode)) {//String工具类
            //如果验证码比对成功，清空缓存数据
            jedisBean.del("LOGIN_CODE_"+telephone);
            UserService userService = (UserService)BeanFactoryUtils.getBean("UserService");

            resultInfo = userService.findByPhone(telephone);
            if (resultInfo.getFlag()) {//如果如果获取到了用户，那么将用户数据放在session中
                request.getSession().setAttribute("loginUser", resultInfo.getData());
            }
        } else {
            resultInfo = new ResultInfo(false, "验证码不一致", "");
        }
        jedisBean.close();
        //响应数据
        String json = new ObjectMapper().writeValueAsString(resultInfo);
        response.getWriter().print(json);
    }

    //loginout
    public void loginout(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getSession().removeAttribute("loginUser");
        //退出之后直接返回首页
        response.sendRedirect(request.getContextPath());
    }

    /**
     * 根据用户id 查询用户信息，请求转发到个人中心界面，将数据显示
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void findById(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1.获取用户id，session中
        User loginUser = (User) request.getSession().getAttribute("loginUser");
        Integer uid = loginUser.getUid();
        //1.1,调用service获取用户
        UserService userService = (UserService)BeanFactoryUtils.getBean("UserService");
        User user = userService.findById(uid);
        //2.设置数据，请求转发
        request.setAttribute("user", user);
        request.getRequestDispatcher("home_index.jsp").forward(request, response);
    }

    /**
     * 获取要修改的数据，获取上传的文件，启用文件上传配置，得到输入输出流，进行文件上传
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void updateUser(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        try {
            //1。获取表单数据
            Map<String, String[]> parameterMap = request.getParameterMap();
            User user= (User) request.getSession().getAttribute("loginUser");//取出session中的对象
            BeanUtils.populate(user, parameterMap);

            //2。获取图片数据.getPart("pic");,参数是input 标签里边的name
            Part picPart = request.getPart("pic");//获取图片对象
            long size = picPart.getSize();//获取图片大小，当上传图片的时候才进行更新
            if (size > 0) {
                //将图片存储到本地磁盘  一般放在项目部署的路径下
                //1、获得img在本地的真实路径
                String realPath = request.getServletContext().getRealPath("/img/");

                //2.为了防止同名过图片覆盖，随机生成图片的名字
                String filename = UuidUtils.getUuid().toString() + ".png";

                //3.使用文件输出流，向指定地址写入文件
                FileOutputStream outputStream = new FileOutputStream(realPath + filename);

                //3.1 获取文件输入流 part
                InputStream inputStream = picPart.getInputStream();

                //使用工具类文件复制
                IOUtils.copy(inputStream, outputStream);
                inputStream.close();
                outputStream.close();
                //写入文件之后，将文件的地址存储到user对象中，然后写入数据库
                //页面获取图片时，肯定是获取项目根目录来获取，${pageContext.request.contextPath}，所以只需要写上图片的上级目录  img即可

                user.setPic("img/" + filename);
            }
            //将数据写入到数据库
            UserService userService = (UserService)BeanFactoryUtils.getBean("UserService");
            userService.updateUser(user);
            //将修改之后的user设置回session
            request.getSession().setAttribute("loginUser", user);
            //响应重定向一下
            response.sendRedirect(request.getContextPath() + "/UserServlet?action=findById");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 通过在session中的用户id，获取对应的地址信息
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void findUserAddress(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //通过用户uid，从tab_address 中获取该用户的所有 地址信息，用户信息session取
        System.out.println("findUserAddress执行");
        User loginUser = (User) request.getSession().getAttribute("loginUser");
        Integer uid = loginUser.getUid();
        UserService userService = (UserService)BeanFactoryUtils.getBean("UserService");
        List<Address> addressList = userService.findUserAddress(uid);
        request.setAttribute("addressList", addressList);
        //由于要显示在页面，所以请求转发
        request.getRequestDispatcher("home_address.jsp").forward(request, response);
    }

    /**
     * 添加地址信息
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void addAddress(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ResultInfo resultInfo=null;
        //获取表单数据
        try {
            Map<String, String[]> parameterMap = request.getParameterMap();
            Address address = new Address();
            BeanUtils.populate(address,parameterMap);
            //获取到的数据 很少，只有姓名 地址，联系电话，但是address类中还有idDefault和 User，所以需要手动添加
            UserService userService = (UserService)BeanFactoryUtils.getBean("UserService");
            address.setIsDefault("0");//手动设置为非默认
            address.setUser((User) request.getSession().getAttribute("loginUser"));
            resultInfo=userService.saveAddress(address);

        } catch (Exception e) {
            e.printStackTrace();
        }
        //将结果返回
        String json = new ObjectMapper().writeValueAsString(resultInfo);
        response.getWriter().write(json);
    }

    /**
     * 设置为默认地址
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void setAddDef(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("setAddDef执行");
        //获取session中的user
        User loginUser = (User) request.getSession().getAttribute("loginUser");
        UserService userService = (UserService)BeanFactoryUtils.getBean("UserService");
        //获取页面传递的 地址id
        String aid = request.getParameter("aid");
        int addressId = Integer.parseInt(aid);
        userService.setAddDef(loginUser.getUid(),addressId);
        response.sendRedirect(request.getContextPath()+"/UserServlet?action=findUserAddress");
    }
}
