package com.zcf.words.service;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zcf.words.common.json.Body;
import com.zcf.words.common.json.LayUiResult;
import com.zcf.words.entity.Policy;
import com.zcf.words.mapper.PolicyMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;


@Service
public class PolicyService {

    private static final Logger logger = LoggerFactory.getLogger(PolicyService.class);


    @Autowired
    private PolicyMapper policyMapper;

    /**
     * 隐私政策
     * @return
     */
    public Body policyInfo() {
        Policy policy = new Policy();
        List<Policy> list =  policyMapper.select(policy);
        if (list == null||list.size()<=0) {
            return Body.newInstance(542, "查询隐私政策失败");
        }
        logger.info("= = = 》 {}查询隐私政策成功", new Date());
        return Body.newInstance(list.get(0));
    }

    /***************************************后台接口****************************************/


    public boolean add(Policy policy) {
        return this.policyMapper.insertSelective(policy) == 1;
    }

    public boolean delete(Integer id) {
        return this.policyMapper.deleteByPrimaryKey(id) == 1;
    }

    public boolean update(Policy policy) {
        return this.policyMapper.updateByPrimaryKeySelective(policy) == 1;
    }

    public LayUiResult query(Integer page, Integer limit) {
        PageHelper.startPage(page, limit);
        List<Policy> list = this.policyMapper.selectAll();
        return new LayUiResult("0", "查询成功", new PageInfo<>(list).getTotal(), list);
    }

    public LayUiResult search(Integer page, Integer limit,String keywords) {
        Example example = new Example(Policy.class);
        keywords = "%" + keywords + "%";
        example.createCriteria().andLike("name", keywords);//name为你想要搜索的字段
        PageHelper.startPage(page, limit);
        List<Policy> list = this.policyMapper.selectByExample(example);
        return new LayUiResult("0", "查询成功", new PageInfo<>(list).getTotal(), list);
    }

}
