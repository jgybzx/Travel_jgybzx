package com.jgybzx.domain;

/**
 * @author: guojy
 * @date: 2019/12/18 12:11
 * @Description:
 * @version:
 */

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.Test;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 购物车对象，一个购物车应该有很多的商品，cartItme  以及总价格
 * 此处的cartItme，使用map存储，根据商品的id，当作key，商品为value
 * 添加购物车的时候，先判断购物车里边是否有  这个 key
 * 如果没有直接添加，计算总价
 * 如果有数据，取出数据，重新设置数量，计算总价
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cart implements Serializable {
    private Double totalPrice = 0d;
    //此处需要注意，如果我们 Cart cart = new Cart()的时候，要想给map赋值，还要进行
    //Map< Integer , CartItem> map = new HashMap<>();
    //比较麻烦，不如直接初始化,就可以直接操作
    private Map<Integer, CartItme> cartItmeMap = new HashMap<>();

    /**
     * 内部封装两个方法，加入购物车和移除购物车
     * 目的，减少外部调用时的代码编写
     */
    /**
     * 添加购物车，需要一个，CartItem  和 商品id
     * 但是 map中封装这 CartItem  ，CartItem中又封装着Route，那么rid就有了，就不需要传递参数了
     *
     * @param cartItme
     */
    public void addCart(CartItme cartItme) {
        //获取到 rid
        Integer rid = cartItme.getRoute().getRid();
        //根据id先判断，原来的购物车是否有  值
        CartItme oldCartItem = cartItmeMap.get(rid);
        if (oldCartItem == null) {
            //原来购物车没有这个 商品的信息，直接添加，计算总价
            cartItmeMap.put(rid, cartItme);
            totalPrice = totalPrice + cartItme.getSubTotal();
        } else {
            //原来购物车有这个商品的信息，更新数量，然后计算总价
            oldCartItem.setCount(oldCartItem.getCount() + cartItme.getCount());
            totalPrice = totalPrice + cartItme.getSubTotal();
        }
    }

    /**
     * 删除数据，只需要map.remove(key),然后计算总价格
     *
     * @param rid
     */
    public void removeCart(Integer rid) {
        CartItme remove = cartItmeMap.remove(rid);//remove方法返回被删除的元素
        totalPrice = totalPrice-remove.getSubTotal();

    }




}
