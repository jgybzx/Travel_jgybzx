package com.jgybzx.utils;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.util.ResourceBundle;

/**
 * @author: guojy
 * @date: 2019/12/14 22:29
 * @Description: 对象工厂实例化 对象 ：目的生产各种对象
 * @version:
 */
public class BeanFactoryUtils {
    /**
     * 传递classNameId  获得实例化对象,properties方式
     *
     * @param classNameId
     * @return
     */
/*    public static Object getBean(String classNameId) {
        //获取配置文件中的 className
        ResourceBundle bundle = ResourceBundle.getBundle("Bean");
        String className = bundle.getString(classNameId);
        //反射获取对象
        Object o = null;
        try {
            o = Class.forName(className).newInstance();
        } catch (Exception e) {
            System.out.println("实例化对象失败,检查名字是否正确");
            e.printStackTrace();
        }
        return o;
    }*/

    public static Object getBean(String classNameId) {
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
            /**
             *  //    表示从任意位置开始  //bean
             *  /     表示从根路径下开始 /beans/bean
             * @      表示属性的意思
             */
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
    }
}
