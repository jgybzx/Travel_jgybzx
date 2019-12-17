package com.jgybzx.知识点复习.动态代理.动态代理日志功能;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Date;
import java.util.ResourceBundle;

/**
 * @author: guojy
 * @date: 2019/12/14 22:29
 * @Description: 对象工厂实例化 对象 ：目的生产各种对象
 * 利用动态代理，加强该工具类，实现增加日志的功能
 * @version:
 */
public class BeanFactoryUtils {
    /**
     * 传递classNameId  获得实例化对象,properties方式
     *
     * @param classNameId
     * @return
     */
    public static Object getBean(String classNameId) {
        //获取配置文件中的 className
        ResourceBundle bundle = ResourceBundle.getBundle("Bean");
        String className = bundle.getString(classNameId);
        //反射获取对象，就是一个接口的实现类对象
        Object object = null;
        try {
            object = Class.forName(className).newInstance();
            //如果直接对 object 加强，那么将会对产生的所有对象加强，不合理 ，在改项目中，只针对Service层的实现类加强
            //所以需要判断 名字的后缀是否是 serviceIml，前提是已经规范了接口实现类的格式
            String name = object.getClass().getName();
            if (name.endsWith("ServiceIml")) {
                //进行加强
                //此处要加强的是 object 但是在invoke中，无法使用外部定义的 object，只能重新顶一个类，接受object
                //如果不明白，将invoke代码的注释解开，就能知道，会报错
                Object finalObject = object;
                Object objectProxy = Proxy.newProxyInstance(BeanFactoryUtils.class.getClassLoader(),
                        object.getClass().getInterfaces(),
                        new InvocationHandler() {
                            @Override
                            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                                //加强的代码
                                String date = new Date().toLocaleString();//获取当前时间
                                System.out.println("执行了" + finalObject.getClass().getName() +
                                        "类@@执行了" + method.getName() +
                                        "方法@参数" + args +
                                        "时间：" + date);
                                Object returnObj = method.invoke(finalObject, args);
                                /*Object returnObj = method.invoke(object, args);  此处直接写object会报错*/
                                return returnObj;
                            }
                        });
            } else {
                System.out.println("实现类的名字不对");
            }
        } catch (Exception e) {
            System.out.println("实例化对象失败,检查名字是否正确");
            e.printStackTrace();
        }

        return object;
    }

    /*public static Object getBean(String classNameId) {
        Object o = null;
        //1.加载xml的流文件数据
        //ClassLoader 类加载器: 特点 保证文件数据在整个内存中唯一(节省空间)
        InputStream is = BeanFactoryUtils.class.getClassLoader().getResourceAsStream("Bean.xml");
        //读取xml文件的 核心类
        SAXReader saxReader = new SAXReader();
        try {
            //获取文档对象
            Document document = saxReader.read(is);
            //获取文档中的节点
            //Single 单个 node 节点  里面写 xpath表达式
            *//**
     *  //    表示从任意位置开始  //bean
     *  /     表示从根路径下开始 /beans/bean
     * @      表示属性的意思
     *//*
            //获得元素标签，不要忘记 拼接 单引号，因为从xml存储的是字符串
            Node node = document.selectSingleNode("//Bean[@id='" + classNameId + "']");
            Element element = (Element) node;
            //获得元素的 class属性的值
            String className = element.attributeValue("class");

            //反射获取实例化对象
            o = Class.forName(className).newInstance();

        } catch (DocumentException | ClassNotFoundException e) {
            System.out.println("读取xml文件失败");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }

        return o;
    }*/
}
