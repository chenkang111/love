package com.zcf.words.service;


import com.alibaba.druid.util.StringUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zcf.words.common.json.Body;
import com.zcf.words.common.json.LayUiResult;
import com.zcf.words.entity.EQcontent;
import com.zcf.words.entity.EQitem;
import com.zcf.words.entity.EQtest;
import com.zcf.words.mapper.EQcontentMapper;
import com.zcf.words.mapper.EQitemMapper;
import com.zcf.words.mapper.EQtestMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;


@Service
public class EQitemService {

    private static final Logger logger = LoggerFactory.getLogger(EQitemService.class);


    @Autowired
    private EQitemMapper eqitemMapper;

    @Autowired
    private EQcontentMapper eqcontentMapper;

    @Autowired
    private EQtestMapper eqtestMapper;
    /**
     * 获取情商测试选项
     *
     * @return
     */
    public Body getEQitem(String contentid) {
        EQitem eqitem = new EQitem();
        List<EQitem> eqitems = new ArrayList<EQitem>();
        if (StringUtils.isEmpty(contentid)) {
            return Body.newInstance(451, "获取情商测试选项失败,参数异常");
        }
        eqitem.setEqitem_content_id(Integer.parseInt(contentid));
        eqitems = eqitemMapper.select(eqitem);
        if (eqitems == null||eqitems.size()<=0) {
            return Body.newInstance(542, "获取情商测试选项表失败");
        }
        logger.info("= = = 》 {}获取情商测试选项成功", eqitems.size());
        return Body.newInstance(eqitems);
    }

    /***************************************后台接口****************************************/

    public boolean add(EQitem eqitem) {
        return this.eqitemMapper.insertSelective(eqitem) == 1;
    }

    public boolean delete(Integer id) {
        return this.eqitemMapper.deleteByPrimaryKey(id) == 1;
    }

    public boolean update(EQitem eqitem) {
        return this.eqitemMapper.updateByPrimaryKeySelective(eqitem) == 1;
    }

    public LayUiResult query(Integer page, Integer limit) {
        PageHelper.startPage(page, limit);
        List<EQitem> list = this.eqitemMapper.selectAll();
        for (EQitem eqitem:list) {
            EQtest eqtest = new EQtest();
            EQcontent eqcontent = new EQcontent();
            eqtest = eqtestMapper.selectByPrimaryKey(eqitem.getEqitem_test_id());
            eqcontent = eqcontentMapper.selectByPrimaryKey(eqitem.getEqitem_content_id());
            eqitem.setEqtest(eqtest);
            eqitem.setEqcontent(eqcontent);
        }
        return new LayUiResult("0", "查询成功", new PageInfo<>(list).getTotal(), list);
    }

    public LayUiResult search(Integer page, Integer limit,String keywords) {
        Example example = new Example(EQitem.class);
        keywords = "%" + keywords + "%";
        example.createCriteria().andLike("eqitem_content", keywords);//name为你想要搜索的字段
        PageHelper.startPage(page, limit);
        List<EQitem> list = this.eqitemMapper.selectByExample(example);
        for (EQitem eqitem:list) {
            EQtest eqtest = eqtestMapper.selectByPrimaryKey(eqitem.getEqitem_test_id());
            EQcontent eqcontent = eqcontentMapper.selectByPrimaryKey(eqitem.getEqitem_content_id());
            eqitem.setEqtest(eqtest);
            eqitem.setEqcontent(eqcontent);
        }
        return new LayUiResult("0", "查询成功", new PageInfo<>(list).getTotal(), list);
    }
}
