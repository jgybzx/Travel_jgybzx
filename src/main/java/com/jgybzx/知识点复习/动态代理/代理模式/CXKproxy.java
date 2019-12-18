package com.jgybzx.知识点复习.动态代理.代理模式;

/**
 * @author: guojy
 * @date: 2019/12/16 16:15
 * @Description: 代理类  但是静态的代理，每一个类都需要建立一个代理类，这样瞬间类库就会扩大一倍
 * 所以要使用动态代理类，我们不再编写代理类，动态代理
 * @version:
 */
public class CXKproxy {
    //蔡徐坤经纪人，代理类，
    // 代理类必须 要有被代理类，因为最终还是要执行被代理类的方法
    public CXK cxk = new CXK();

    public void singProxy() {
        //如果要找经纪人找蔡徐坤唱歌就不是唱歌这么简单了，唱歌之前要干很多事情
        System.out.println("谈价钱");
        System.out.println("找场地");
        //然后 才是唱歌
        cxk.sing();
        //后续工作
        System.out.println("粉丝见面");
    }
}
