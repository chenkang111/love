package com.zcf.words.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zcf.words.common.json.Body;
import com.zcf.words.common.json.LayUiResult;
import com.zcf.words.entity.Handbook;
import com.zcf.words.mapper.HandbookMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
public class HandbookService {

    private static final Logger logger = LoggerFactory.getLogger(HandbookService.class);


    @Autowired
    private HandbookMapper handbookMapper;

    /**
     * 获取恋爱指南详情
     *
     * @return
     */
    public Body HandbookInfo() {
        List<Handbook> handbook = handbookMapper.selectAll();
        if (handbook == null||handbook.size()<=0) {
            return Body.newInstance(542, "查询恋爱指南失败");
        }
        logger.info("= = = 》 {}查询恋爱指南成功", new Date());
        return Body.newInstance(handbook);
    }


    /***************************************后台接口****************************************/

    public boolean add(Handbook handbook) {
        return this.handbookMapper.insertSelective(handbook) == 1;
    }

    public boolean delete(Integer id) {
        return this.handbookMapper.deleteByPrimaryKey(id) == 1;
    }

    public boolean update(Handbook handbook) {
        return this.handbookMapper.updateByPrimaryKeySelective(handbook) == 1;
    }

    public LayUiResult query(Integer page, Integer limit) {
        PageHelper.startPage(page, limit);
        List<Handbook> list = this.handbookMapper.selectAll();
        return new LayUiResult("0", "查询成功", new PageInfo<>(list).getTotal(), list);
    }

    public LayUiResult search(Integer page, Integer limit,String keywords) {
        Example example = new Example(Handbook.class);
        keywords = "%" + keywords + "%";
        example.createCriteria().andLike("handbook_title", keywords);//name为你想要搜索的字段
        PageHelper.startPage(page, limit);
        List<Handbook> list = this.handbookMapper.selectByExample(example);
        return new LayUiResult("0", "查询成功", new PageInfo<>(list).getTotal(), list);
    }
}
