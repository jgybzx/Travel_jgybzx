package com.jgybzx.domain;

/**
 * @author: guojy
 * @date: 2019/12/18 12:17
 * @Description: 购物项
 * @version:
 */

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 购物车里边的每一条记录，应该包括，商品的信息，数量信息，小计（数量*单价）
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItme {
    private Route route;//具体 商品
    private Integer count;//商品数量
    private Double subTotal;//小计，（数量*单价）

    //因为属性中 已经有了  route和count，那么就可以直接计算subTotal，因为el表达式底层取值，是get
    //重写 subTotal 的get
    public Double getSubTotal() {
        return route.getPrice() * count;
    }

}
