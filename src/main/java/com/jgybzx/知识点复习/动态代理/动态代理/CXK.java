package com.jgybzx.知识点复习.动态代理.动态代理;

/**
 * @author: guojy
 * @date: 2019/12/16 16:28
 * @Description:
 * @version:
 */
public class CXK implements Star {
    @Override//蔡徐坤实现明星接口，重写show方法
    public void show() {
        System.out.println("蔡徐坤唱歌");
    }
}
