package com.jgybzx.知识点复习.动态代理.动态代理;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author: guojy
 * @date: 2019/12/16 16:32
 * @Description: 来代替静态代理类
 * handler 就有管理者的意思
 * 要求 必须实现  InvocationHandler接口，重写方法
 * @version:
 */
public class StarHandler implements InvocationHandler {
    //既然要代理，那么需要知道代理谁，此处代理的应该是明星，所以要有明星接口
    private  Star star;
    //通过构造，传入对象
    public  StarHandler(Star star){
        this.star=star;
    }
    //拦截方法

    /**
     * 还是通过反射，执行接口里边的 方法，
     * 反射 反射需要知道，对象是谁，方法是啥，参数是啥
     * @param proxy 产生的动态代理对象
     * @param method 被代理对象的方法的  对象，反射调用该方法
     * @param args 被代理对象的方法的参数
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("谈价钱");
        System.out.println("定场地");
        //反射调用方法，就是 接口的show方法
        Object invoke = method.invoke(this.star, args);
        System.out.println("粉丝见面");
        return invoke;
    }
}
