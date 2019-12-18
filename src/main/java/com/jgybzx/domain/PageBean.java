package com.jgybzx.domain;

import lombok.Data;

import java.util.List;

/**
 * @author: guojy
 * @date: 2019/12/17 11:55
 * @Description: 负责返回分页查询的数据，包括主要的数据，以及总共的条数，分页的页数,d
 * @version:
 */
@Data
public class PageBean<T> {
    private List<T> data;
    private Integer totalCount;//总条数
    private Integer totalPage;//总页数
    private Integer pageNumber;//当前页码
    private Integer pageSize;//每页 显示的个数


    //控制页码遍历的开始和结束，route_list.jsp
    private Integer startNumber;//开始页码
    private Integer endNumber;//结束页码
    //由于我们没有在service层判断这些东西，所以没法在service层赋值，而jsp 在取值的时候${},其实底层用的是 getXxx()
    //所以直接 重写 startNumber endNumber 的get方法，把计算后的值直接返回。


    public Integer getStartNumber() {
        jisuan();
        return startNumber;
    }

    public Integer getEndNumber() {
        jisuan();
        return endNumber;
    }

    //开始计算两个页码的值，根据pageNumber计算，前5 后 4
    private void jisuan() {
        //首先，如果总页码小于10，那么就没有必要前 5 后 4 了，开始就是1，结束就是总页码
        if (totalPage < 10) {
            startNumber = 1;
            endNumber = totalPage;
        } else {//总页数大于10,为了使用保证是10个数，要求一个关系： endNumber - startNumber = 9；
            startNumber = pageNumber - 5;
            endNumber = pageNumber + 4;
            //此处还需要注意，如果当前页是 1 2 3 4 5 ，减掉5之后，会出现一个小于0的数，遍历的时候会出错，所以排除掉startNumber < 1
            if (startNumber < 1) {
                startNumber = 1;
                endNumber = 10;
            }
            //如果 加完后的endNumber 大于总页数，也会出现问题，需要排除掉endNumber>totalPage
            if (endNumber > totalPage) {
                endNumber = totalPage;
                startNumber = totalPage - 9;
            }

        }
    }
}
