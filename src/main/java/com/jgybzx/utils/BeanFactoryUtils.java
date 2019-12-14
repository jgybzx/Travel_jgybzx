package com.jgybzx.utils;

import java.util.ResourceBundle;

/**
 * @author: guojy
 * @date: 2019/12/14 22:29
 * @Description: 对象工厂实例化 对象 ：目的生产各种对象
 * @version:
 */
public class BeanFactoryUtils {
    /**
     * 传递id  获得实例化对象
     *
     * @param classNameId
     * @return
     */
    public static Object getBean(String classNameId){
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
        return  o;
    }
}
