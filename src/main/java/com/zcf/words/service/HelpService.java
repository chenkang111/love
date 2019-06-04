package com.zcf.words.service;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zcf.words.common.json.Body;
import com.zcf.words.common.json.LayUiResult;
import com.zcf.words.entity.Help;
import com.zcf.words.mapper.HelpMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;


@Service
public class HelpService {

    private static final Logger logger = LoggerFactory.getLogger(HelpService.class);


    @Autowired
    private HelpMapper helpMapper;

    /**
     * 获取使用帮助详情
     *
     * @return
     */
    public Body helpInfo() {
        Help help = new Help();
        List<Help> list =  helpMapper.select(help);
        if (list == null||list.size()<=0) {
            return Body.newInstance(542, "查询使用帮助失败");
        }
        logger.info("= = = 》 {}查询使用帮助成功", new Date());
        return Body.newInstance(list.get(0));
    }

    /***************************************后台接口****************************************/

    public boolean add(Help help) {
        return this.helpMapper.insertSelective(help) == 1;
    }

    public boolean delete(Integer id) {
        return this.helpMapper.deleteByPrimaryKey(id) == 1;
    }

    public boolean update(Help help) {
        return this.helpMapper.updateByPrimaryKeySelective(help) == 1;
    }

    public LayUiResult query(Integer page, Integer limit) {
        PageHelper.startPage(page, limit);
        List<Help> list = this.helpMapper.selectAll();
        return new LayUiResult("0", "查询成功", new PageInfo<>(list).getTotal(), list);
    }

    public LayUiResult search(Integer page, Integer limit,String keywords) {
        Example example = new Example(Help.class);
        keywords = "%" + keywords + "%";
        example.createCriteria().andLike("name", keywords);//name为你想要搜索的字段
        PageHelper.startPage(page, limit);
        List<Help> list = this.helpMapper.selectByExample(example);
        return new LayUiResult("0", "查询成功", new PageInfo<>(list).getTotal(), list);
    }

}
