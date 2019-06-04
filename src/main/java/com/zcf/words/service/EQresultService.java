package com.zcf.words.service;


import com.alibaba.druid.util.StringUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zcf.words.common.json.Body;
import com.zcf.words.common.json.LayUiResult;
import com.zcf.words.entity.EQresult;
import com.zcf.words.entity.EQtest;
import com.zcf.words.mapper.EQresultMapper;
import com.zcf.words.mapper.EQtestMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;


@Service
public class EQresultService {

    private static final Logger logger = LoggerFactory.getLogger(EQresultService.class);


    @Autowired
    private EQresultMapper eqresultMapper;

    @Autowired
    private EQtestMapper eqtestMapper;

    /**
     * 获取情商测试答案
     *
     * @return
     */
    public Body getEQresult(String eqtestid,Integer point) {
        Example exp = new Example(EQresult.class);
        exp.createCriteria().andEqualTo("eqresult_test_id",eqtestid);
        exp.orderBy("eqresult_id");
        List<EQresult> list =eqresultMapper.selectByExample(exp);
        int index=-1;
        for (int i = 0; i <list.size() ; i++) {
            if(point>=Integer.parseInt(list.get(i).getEqresult_point_start()) && point<=Integer.parseInt(list.get(i).getEqresult_point_end())){
                index=i;
                break;
            }
        }
        if(index<0){
            return Body.newInstance(201,"未查询到结果");
        }
        return Body.newInstance(list.get(index));
    }

    /***************************************后台接口****************************************/

    public boolean add(EQresult eqresult) {
        return this.eqresultMapper.insertSelective(eqresult) == 1;
    }

    public boolean delete(Integer id) {
        return this.eqresultMapper.deleteByPrimaryKey(id) == 1;
    }

    public boolean update(EQresult eqresult) {
        return this.eqresultMapper.updateByPrimaryKeySelective(eqresult) == 1;
    }

    public LayUiResult query(Integer page, Integer limit) {
        PageHelper.startPage(page, limit);
        List<EQresult> list = this.eqresultMapper.selectAll();
        for (EQresult  eqresult:list) {
            EQtest eqtest  = eqtestMapper.selectByPrimaryKey(eqresult.getEqresult_test_id());
            eqresult.setEqtest(eqtest);
        }
        return new LayUiResult("0", "查询成功", new PageInfo<>(list).getTotal(), list);
    }

    public LayUiResult search(Integer page, Integer limit,String keywords) {
        Example example = new Example(EQresult.class);
        keywords = "%" + keywords + "%";
        example.createCriteria().andLike("eqresult_title", keywords);//name为你想要搜索的字段
        PageHelper.startPage(page, limit);
        List<EQresult> list = this.eqresultMapper.selectByExample(example);
        for (EQresult  eqresult:list) {
            EQtest eqtest  = eqtestMapper.selectByPrimaryKey(eqresult.getEqresult_test_id());
            eqresult.setEqtest(eqtest);
        }
        return new LayUiResult("0", "查询成功", new PageInfo<>(list).getTotal(), list);
    }
}
