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
public class EQcontentService {

    private static final Logger logger = LoggerFactory.getLogger(EQcontentService.class);


    @Autowired
    private EQcontentMapper eqcontentMapper;

    @Autowired
    private EQtestMapper eqtestMapper;
    /**
     * 获取情商测试选项
     *
     * @return
     */
    public Body getEQcontent(String eqtestid,String tag) {
        EQcontent eqcontent = new EQcontent();
        List<EQcontent> eqcontents = new ArrayList<EQcontent>();
        if (StringUtils.isEmpty(eqtestid)) {
            return Body.newInstance(451, "获取情商测试选项失败,参数异常");
        }
        eqcontent.setEqcontent_eqtest_id(Integer.parseInt(eqtestid));
        eqcontent.setEqcontent_tag(tag);
        eqcontents = eqcontentMapper.select(eqcontent);
        if (eqcontents == null||eqcontents.size()<=0) {
            return Body.newInstance(542, "获取情商测试选项表失败");
        }
        logger.info("= = = 》 {}获取情商测试选项成功", eqcontents.size());
        return Body.newInstance(eqcontents);
    }

    /***************************************后台接口****************************************/

    public boolean add(EQcontent eqcontent) {
        return this.eqcontentMapper.insertSelective(eqcontent) == 1;
    }

    public boolean delete(Integer id) {
        return this.eqcontentMapper.deleteByPrimaryKey(id) == 1;
    }

    public boolean update(EQcontent eqcontent) {
        return this.eqcontentMapper.updateByPrimaryKeySelective(eqcontent) == 1;
    }

    public LayUiResult query(Integer page, Integer limit) {
        PageHelper.startPage(page, limit);
        List<EQcontent> list = this.eqcontentMapper.selectAll();
        for (EQcontent  eqcontent:list) {
            EQtest eqtest = new EQtest();
            eqtest = eqtestMapper.selectByPrimaryKey(eqcontent.getEqcontent_eqtest_id());
            eqcontent.setEqtest(eqtest);
        }
        return new LayUiResult("0", "查询成功", new PageInfo<>(list).getTotal(), list);
    }

    public LayUiResult query(String ids,Integer page, Integer limit) {
        PageHelper.startPage(page, limit);
        Example exp = new Example(EQcontent.class);
        exp.createCriteria().andEqualTo("eqcontent_eqtest_id",ids);
        List<EQcontent> list = this.eqcontentMapper.selectByExample(exp);
        for (EQcontent  eqcontent:list) {
            EQtest eqtest = new EQtest();
            eqtest = eqtestMapper.selectByPrimaryKey(eqcontent.getEqcontent_eqtest_id());
            eqcontent.setEqtest(eqtest);
        }
        return new LayUiResult("0", "查询成功", new PageInfo<>(list).getTotal(), list);
    }

    public LayUiResult search(Integer page, Integer limit,String keywords) {
        Example example = new Example(EQcontent.class);
        keywords = "%" + keywords + "%";
        example.createCriteria().andLike("eqcontent_content", keywords);//name为你想要搜索的字段
        PageHelper.startPage(page, limit);
        List<EQcontent> list = this.eqcontentMapper.selectByExample(example);
        for (EQcontent  eqcontent:list) {
            EQtest eqtest  = eqtestMapper.selectByPrimaryKey(eqcontent.getEqcontent_eqtest_id());
            eqcontent.setEqtest(eqtest);
        }
        return new LayUiResult("0", "查询成功", new PageInfo<>(list).getTotal(), list);
    }
}
