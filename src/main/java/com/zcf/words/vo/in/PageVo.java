package com.zcf.words.vo.in;


import lombok.Getter;
import lombok.Setter;

/**
 * 分页参数公共 vo Created by shenguohao
 */
@Getter
@Setter
public class PageVo {
    private Integer page = 1;
    private Integer pageSize = 10;
}