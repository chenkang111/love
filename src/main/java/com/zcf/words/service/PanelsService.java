package com.zcf.words.service;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zcf.words.common.json.Body;
import com.zcf.words.common.json.LayUiResult;
import com.zcf.words.entity.Panels;
import com.zcf.words.mapper.PanelsMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;


@Service
public class PanelsService {

    private static final Logger logger = LoggerFactory.getLogger(PanelsService.class);

    @Autowired
    private PanelsMapper panelsMapper;

    /**
     * 获取展示面列表
     *
     * @return
     */
    public Body getPanels() {
        Panels panels = new Panels();
        List<Panels> panelss = new ArrayList<Panels>();
        panelss = panelsMapper.select(panels);
        if (panelss == null||panelss.size()<=0) {
            return Body.newInstance(542, "获取展示面列表失败");
        }
        logger.info("= = = 》 {}获取展示面列表成功", panelss.size());
        return Body.newInstance(panelss);
    }


    /***************************************后台接口****************************************/

    public boolean add(Panels panels) {
        return this.panelsMapper.insertSelective(panels) == 1;
    }

    public boolean delete(Integer id) {
        return this.panelsMapper.deleteByPrimaryKey(id) == 1;
    }

    public boolean update(Panels panels) {
        return this.panelsMapper.updateByPrimaryKeySelective(panels) == 1;
    }

    public LayUiResult query(Integer page, Integer limit) {
        PageHelper.startPage(page, limit);
        List<Panels> list = this.panelsMapper.selectAll();
        return new LayUiResult("0", "查询成功", new PageInfo<>(list).getTotal(), list);
    }

    public LayUiResult search(Integer page, Integer limit,String keywords) {
        Example example = new Example(Panels.class);
        keywords = "%" + keywords + "%";
        example.createCriteria().andLike("panels_title", keywords);//name为你想要搜索的字段
        PageHelper.startPage(page, limit);
        List<Panels> list = this.panelsMapper.selectByExample(example);
        return new LayUiResult("0", "查询成功", new PageInfo<>(list).getTotal(), list);
    }

}
