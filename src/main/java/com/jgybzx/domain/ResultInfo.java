package com.jgybzx.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: guojy
 * @date: 2019/12/12 18:26
 * @Description: 此对象专门为用户返回异常数据
 * @version:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResultInfo {
    private Boolean flag ; //当前是成功或者是失败的
    private String errorMsg; //失败的提示信息
    private Object data ; //成功的提示信息,有可能是List 对象 字符串
    //成功的时候可能返回的数据类型就比较多，所以使用Object
}
