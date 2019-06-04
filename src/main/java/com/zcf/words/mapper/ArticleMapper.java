package com.zcf.words.mapper;


import com.zcf.words.common.mybatis.MyMapper;
import com.zcf.words.entity.Article;

import java.util.List;

public interface ArticleMapper extends MyMapper<Article> {


    List<Article> getArticel();

}
