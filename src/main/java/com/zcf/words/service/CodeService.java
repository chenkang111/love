package com.zcf.words.service;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zcf.words.common.json.LayUiResult;
import com.zcf.words.entity.Code;
import com.zcf.words.mapper.CodeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;


@Service
public class CodeService {

    private static final Logger logger = LoggerFactory.getLogger(CodeService.class);


    /***************************************后台接口****************************************/

    @Autowired
    private CodeMapper codemapper;

    public boolean add(Code code) {
        return this.codemapper.insertSelective(code) == 1;
    }

    public boolean delete(Integer id) {
        return this.codemapper.deleteByPrimaryKey(id) == 1;
    }

    public boolean update(Code code) {
        return this.codemapper.updateByPrimaryKeySelective(code) == 1;
    }

    public LayUiResult query(Integer page, Integer limit) {
        PageHelper.startPage(page, limit);
        List<Code> list = this.codemapper.selectAll();
        return new LayUiResult("0", "查询成功", new PageInfo<>(list).getTotal(), list);
    }

    public LayUiResult search(Integer page, Integer limit,String keywords) {
        Example example = new Example(Code.class);
        keywords = "%" + keywords + "%";
        example.createCriteria().andLike("name", keywords);//name为你想要搜索的字段
        PageHelper.startPage(page, limit);
        List<Code> list = this.codemapper.selectByExample(example);
        return new LayUiResult("0", "查询成功", new PageInfo<>(list).getTotal(), list);
    }
}
