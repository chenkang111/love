package com.zcf.words.service;


import com.alibaba.druid.util.StringUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zcf.words.common.json.Body;
import com.zcf.words.common.json.LayUiResult;
import com.zcf.words.entity.Highcontent;
import com.zcf.words.entity.Highlight;
import com.zcf.words.entity.Toptag;
import com.zcf.words.mapper.HighcontentMapper;
import com.zcf.words.mapper.HighlightMapper;
import com.zcf.words.mapper.ToptagMapper;
import com.zcf.words.vo.out.HighLightContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import java.util.ArrayList;
import java.util.List;


@Service
public class HighlightService {

    private static final Logger logger = LoggerFactory.getLogger(HighlightService.class);

    @Autowired
    private HighlightMapper highlightMapper;

    @Autowired
    private HighcontentMapper highcontentMapper;

    @Autowired
    private ToptagMapper toptagMapper;

    /**
     * 获取专题标签下的列表内容
     *
     * @return
     */
    public Body getHighlight(String toptagid,String content) {
//        if (StringUtils.isEmpty(toptagid)) {
//            return Body.newInstance(451, "查询专题内容失败,参数异常");
//        }
        Example exp = new Example(Highlight.class);
        Example.Criteria criteria=exp.createCriteria();
        criteria.andLike("highlight_content","%"+content+"%");
        if (!StringUtils.isEmpty(toptagid)) {
            criteria.andEqualTo("highlight_toptag_id",toptagid);
        }
        List<HighLightContent> highlightcontents = new ArrayList<HighLightContent>();
        List<Highlight> highlights = new ArrayList<Highlight>();
        List<Highcontent> Highcontents = new ArrayList<Highcontent>();
        Highcontent highcontent = new Highcontent();
        HighLightContent highlightcontent;
        highlights = highlightMapper.selectByExample(exp);
        for (Highlight hlc: highlights) {
            highlightcontent=new HighLightContent();
            highcontent.setHighcontent_highlight_id(hlc.getHighlight_id());
            Highcontents = highcontentMapper.select(highcontent);
            highlightcontent.setHighlight_id(hlc.getHighlight_id());
            highlightcontent.setHighlight_content(hlc.getHighlight_content());
            highlightcontent.setList(Highcontents);
            highlightcontents.add(highlightcontent);
        }
        if (highlightcontents == null||highlightcontents.size()<=0) {
            return Body.newInstance(542, "查询专题内容失败,内容为空");
        }
        logger.info("= = = 》 {}查询专题内容成功", highlightcontents.size());
        return Body.newInstance(highlightcontents);
    }


    /***************************************后台接口****************************************/

    public boolean add(Highlight highlight) {
        return this.highlightMapper.insertSelective(highlight) == 1;
    }

    public boolean delete(Integer id) {
        return this.highlightMapper.deleteByPrimaryKey(id) == 1;
    }

    public boolean update(Highlight highlight) {
        return this.highlightMapper.updateByPrimaryKeySelective(highlight) == 1;
    }

    public LayUiResult query(Integer page, Integer limit) {
        PageHelper.startPage(page, limit);
        List<Highlight> list = this.highlightMapper.selectAll();
        for (Highlight  highlight:list) {
            Toptag toptag = toptagMapper.selectByPrimaryKey(highlight.getHighlight_toptag_id());
            highlight.setToptag(toptag);
        }
        return new LayUiResult("0", "查询成功", new PageInfo<>(list).getTotal(), list);
    }

    public LayUiResult search(Integer page, Integer limit,String keywords) {
        Example example = new Example(Highlight.class);
        keywords = "%" + keywords + "%";
        example.createCriteria().andLike("highlight_content", keywords);//name为你想要搜索的字段
        PageHelper.startPage(page, limit);
        List<Highlight> list = this.highlightMapper.selectByExample(example);
        for (Highlight  highlight:list) {
            Toptag toptag = toptagMapper.selectByPrimaryKey(highlight.getHighlight_toptag_id());
            highlight.setToptag(toptag);
        }
        return new LayUiResult("0", "查询成功", new PageInfo<>(list).getTotal(), list);
    }
}
