package com.zcf.words.service;


import com.alibaba.druid.util.StringUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zcf.words.common.json.Body;
import com.zcf.words.common.json.LayUiResult;
import com.zcf.words.entity.Highcontent;
import com.zcf.words.entity.Highlight;
import com.zcf.words.mapper.HighcontentMapper;
import com.zcf.words.mapper.HighlightMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;


@Service
public class HighcontentService {

    private static final Logger logger = LoggerFactory.getLogger(HighcontentService.class);

    @Autowired
    private HighcontentMapper highcontentMapper;

    @Autowired
    private HighlightMapper highlightMapper;
    /**
     * 获取专题内容列表
     *
     * @return
     */
    public Body getHighlightInfo(String highlightid) {
        Highcontent highlight = new Highcontent();
        if (StringUtils.isEmpty(highlightid)) {
            return Body.newInstance(451, "查询专题内容失败,参数异常");
        }
        highlight.setHighcontent_highlight_id(Integer.parseInt(highlightid));
        List<Highcontent> highcontents = new ArrayList<Highcontent>();
        highcontents = highcontentMapper.select(highlight);
        if (highcontents == null||highcontents.size()<=0) {
            return Body.newInstance(542, "查询专题内容失败,内容为空");
        }
        logger.info("= = = 》 {}查询专题内容成功", highcontents.size());
        return Body.newInstance(highcontents);
    }


    /***************************************后台接口****************************************/

    //新增
    public boolean add(Highcontent highcontent) {
        return this.highcontentMapper.insertSelective(highcontent) == 1;
    }

    //删除
    public boolean delete(Integer id) {
        return this.highcontentMapper.deleteByPrimaryKey(id) == 1;
    }

    //更新
    public boolean update(Highcontent highcontent) {
        return this.highcontentMapper.updateByPrimaryKeySelective(highcontent) == 1;
    }

    //查询
    public LayUiResult query(Integer page, Integer limit) {
        PageHelper.startPage(page, limit);
        List<Highcontent> list = this.highcontentMapper.selectAll();
        for (Highcontent  highcontent:list) {
            Highlight highlight = highlightMapper.selectByPrimaryKey(highcontent.getHighcontent_highlight_id());
            highcontent.setHighlight(highlight);
        }
        return new LayUiResult("0", "查询成功", new PageInfo<>(list).getTotal(), list);
    }

    //搜索
    public LayUiResult search(Integer page, Integer limit,String keywords) {
        Example example = new Example(Highcontent.class);
        example.createCriteria().andLike("highcontent_content", "%" + keywords + "%");//name为你想要搜索的字段
        PageHelper.startPage(page, limit);
        List<Highcontent> list = this.highcontentMapper.selectByExample(example);
        for (Highcontent  highcontent:list) {
            Highlight highlight = highlightMapper.selectByPrimaryKey(highcontent.getHighcontent_highlight_id());
            highcontent.setHighlight(highlight);
        }
        return new LayUiResult("0", "查询成功", new PageInfo<>(list).getTotal(), list);
    }


}
