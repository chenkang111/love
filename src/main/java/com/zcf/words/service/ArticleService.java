package com.zcf.words.service;


import com.alibaba.druid.util.StringUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zcf.words.common.json.Body;
import com.zcf.words.common.json.LayUiResult;
import com.zcf.words.entity.Article;
import com.zcf.words.mapper.ArticleMapper;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
public class ArticleService {

    private static final Logger logger = LoggerFactory.getLogger(ArticleService.class);

    @Autowired
    private ArticleMapper articleMapper;

    /**
     * 获取文章列表
     *
     * @return
     */
    public Body getArticle() {
        List<Article> articles = articleMapper.getArticel();
        if (articles.isEmpty()) {
            return Body.newInstance(542, "查询文章失败");
        }
        logger.info("= = = 》 {}查询文章成功", articles.size());
        return Body.newInstance(articles);
    }

    /**
     * 获取文章详情
     *
     * @return
     */
    public Body ArticleInfo(String articleid) {
        Article article = new Article();
        if (StringUtils.isEmpty(articleid)) {
            return Body.newInstance(451, "获取文章失败,参数异常");
        }
        article.setArticle_id(Integer.parseInt(articleid));
        article = articleMapper.selectOne(article);
        if (article == null||article.getArticle_id().toString().length()<=0) {
            return Body.newInstance(542, "查询文章失败");
        }
        logger.info("= = = 》 {}查询文章成功", new Date());
        return Body.newInstance(article);
    }

    /***************************************后台接口****************************************/

    public boolean add(Article article) {
        return this.articleMapper.insertSelective(article) == 1;
    }

    public boolean delete(Integer id) {
        return this.articleMapper.deleteByPrimaryKey(id) == 1;
    }

    public boolean update(Article article) {
        return this.articleMapper.updateByPrimaryKeySelective(article) == 1;
    }

    public LayUiResult query(Integer page, Integer limit) {
        PageHelper.startPage(page, limit);
        List<Article> list = this.articleMapper.selectAll();
        return new LayUiResult("0", "查询成功", new PageInfo<>(list).getTotal(), list);
    }

    public LayUiResult search(Integer page, Integer limit,String keywords) {
        Example example = new Example(Article.class);
        keywords = "%" + keywords + "%";
        example.createCriteria().andLike("article_title", keywords);//name为你想要搜索的字段
        PageHelper.startPage(page, limit);
        List<Article> list = this.articleMapper.selectByExample(example);
        return new LayUiResult("0", "查询成功", new PageInfo<>(list).getTotal(), list);
    }

}
