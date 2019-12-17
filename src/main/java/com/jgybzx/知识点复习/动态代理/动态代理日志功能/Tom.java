package com.jgybzx.知识点复习.动态代理.动态代理日志功能;

/**
 * @author: guojy
 * @date: 2019/12/17 20:40
 * @Description:
 * @version:
 */
public class Tom implements People {

    @Override
    public void runing() {
        System.out.println("tom跑步");
    }

    @Override
    public void eating() {
        System.out.println("tom吃东西");
    }

    @Override
    public Integer getAge(int age) {

        return age;
    }
}
