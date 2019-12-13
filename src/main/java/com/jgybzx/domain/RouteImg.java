package com.jgybzx.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * 旅游线路图片实体类
 */
@Data
public class RouteImg implements Serializable {

    private Integer rgid;//商品图片id
    private Integer rid;//旅游商品id
    private String bigPic;//详情商品大图
    private String smallPic;//详情商品小图


}
