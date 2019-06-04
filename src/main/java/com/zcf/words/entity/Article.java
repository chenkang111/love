package com.zcf.words.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Setter
@Getter
@Table(name = "article")
public class Article {

    @Id
    private Integer article_id;//主键
    private String article_title;//精彩文章标题
    private String article_content;//精彩文章内容
    private String article_pic;//精彩文章图片
    private Date create_time;//创建日期
    private Date update_time;//修改日期
    private String mark;//备注
    private String del_flag;//删除标记 0正常 1已删除
}
