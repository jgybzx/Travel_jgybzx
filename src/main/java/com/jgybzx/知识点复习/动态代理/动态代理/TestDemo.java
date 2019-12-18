package com.jgybzx.知识点复习.动态代理.动态代理;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 * @author: guojy
 * @date: 2019/12/16 16:47
 * @Description:
 * @version:
 */
public class TestDemo {
    public static void main(String[] args) {
        //动态的在内存中产生一个被代理类的字节码
        //然后创建一个代理类对象  proxy
        Star star = (Star)Proxy.newProxyInstance(CXK.class.getClassLoader(),//被代理对象的类加载器，因为代理类的类型要跟被代理类的类型一致才行
                new Class[]{Star.class},//被代理类的父接口数组，既然要求类型一致，那么父接口也应该一样
                new StarHandler(new CXK()));
        star.show();
    }
}
