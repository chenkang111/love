package com.zcf.words.controller.api;

import com.zcf.words.common.json.Body;
import com.zcf.words.service.ArticleService;
import com.zcf.words.service.HandbookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("article")
public class OnStageArticleController {

    @Autowired
    private ArticleService articleService;

    /**
     * 获取文章列表
     * @return
     */
    @RequestMapping(value = "articles", method = RequestMethod.POST)
    @ResponseBody
    public Body getArticle() {
        return articleService.getArticle();
    }


    /**
     * 获取文章详情
     * @return
     */
    @RequestMapping(value = "info_article", method = RequestMethod.POST)
    @ResponseBody
    public Body ArticleInfo(String articleid) {
        return articleService.ArticleInfo(articleid);
    }


}
