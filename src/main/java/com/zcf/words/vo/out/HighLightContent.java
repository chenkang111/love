package com.zcf.words.vo.out;

import com.zcf.words.entity.Highcontent;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 内容
 */
@Getter
@Setter
public class HighLightContent {

    private Integer highlight_id;//主键
    private String highlight_content;//专题开场白

    private List<Highcontent> list;
}
