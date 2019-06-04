package com.zcf.words.service;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zcf.words.common.json.Body;
import com.zcf.words.common.json.LayUiResult;
import com.zcf.words.entity.Correlation;
import com.zcf.words.mapper.CorrelationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
public class CorrelatiService {

    private static final Logger logger = LoggerFactory.getLogger(CorrelatiService.class);



    @Autowired
    private CorrelationMapper correlationMapper;

    /**
     * 关于我们
     * @return
     */
    public Body correlationInfo() {
        Correlation correlation = new Correlation();
        List<Correlation> list = correlationMapper.select(correlation);
        if (list == null||list.size()<=0) {
            return Body.newInstance(542, "查询关于我们失败");
        }
        logger.info("= = = 》 {}查询关于我们成功", new Date());
        return Body.newInstance(list.get(0));
    }

    /***************************************后台接口****************************************/

    public boolean add(Correlation correlation) {
        return this.correlationMapper.insertSelective(correlation) == 1;
    }

    public boolean delete(Integer id) {
        return this.correlationMapper.deleteByPrimaryKey(id) == 1;
    }

    public boolean update(Correlation correlation) {
        return this.correlationMapper.updateByPrimaryKeySelective(correlation) == 1;
    }

    public LayUiResult query(Integer page, Integer limit) {
        PageHelper.startPage(page, limit);
        List<Correlation> list = this.correlationMapper.selectAll();
        return new LayUiResult("0", "查询成功", new PageInfo<>(list).getTotal(), list);
    }

    public LayUiResult search(Integer page, Integer limit,String keywords) {
        Example example = new Example(Correlation.class);
        keywords = "%" + keywords + "%";
        example.createCriteria().andLike("name", keywords);//name为你想要搜索的字段
        PageHelper.startPage(page, limit);
        List<Correlation> list = this.correlationMapper.selectByExample(example);
        return new LayUiResult("0", "查询成功", new PageInfo<>(list).getTotal(), list);
    }
}
