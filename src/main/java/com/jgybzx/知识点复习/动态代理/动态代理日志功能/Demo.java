package com.jgybzx.知识点复习.动态代理.动态代理日志功能;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author: guojy
 * @date: 2019/12/17 20:41
 * @Description: tom的代理类
 * @version:
 */
public class Demo {
    public static void main(String[] args) {
        People people = new Tom();//被代理的实现类的对象

        People peopleProxy = (People) Proxy.newProxyInstance(Demo.class.getClassLoader(),
                Tom.class.getInterfaces(),
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        System.out.println("执行了"+method.getName()+"方法");
                        Object obj = method.invoke(people, args);
                        return obj;
                    }
                });
        peopleProxy.eating();
        System.out.println(peopleProxy.getAge(12));
        peopleProxy.runing();
    }
}
