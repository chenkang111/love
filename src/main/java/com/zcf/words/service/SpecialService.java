package com.zcf.words.service;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zcf.words.common.json.Body;
import com.zcf.words.common.json.LayUiResult;
import com.zcf.words.entity.Special;
import com.zcf.words.mapper.SpecialMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;


@Service
public class SpecialService {

    private static final Logger logger = LoggerFactory.getLogger(SpecialService.class);

    @Autowired
    private SpecialMapper specialMapper;

    /**
     * 获取专题列表
     *
     * @return
     */
    public Body getSpecial() {
        Special special = new Special();
        List<Special> specials = new ArrayList<Special>();
        specials = specialMapper.select(special);
        if (specials == null || specials.size() <= 0) {
            return Body.newInstance(404, "查询专题失败,数据为空");
        }
        logger.info("= = = 》 {}查询专题成功", specials.size());
        return Body.newInstance(specials);
    }

    /***************************************后台接口****************************************/

    public boolean add(Special special) {
        return this.specialMapper.insertSelective(special) == 1;
    }

    public boolean delete(Integer id) {
        return this.specialMapper.deleteByPrimaryKey(id) == 1;
    }

    public boolean update(Special special) {
        return this.specialMapper.updateByPrimaryKeySelective(special) == 1;
    }

    public LayUiResult query(Integer page, Integer limit) {
        PageHelper.startPage(page, limit);
        List<Special> list = this.specialMapper.selectAll();
        return new LayUiResult("0", "查询成功", new PageInfo<>(list).getTotal(), list);
    }

    public LayUiResult search(Integer page, Integer limit, String keywords) {
        Example example = new Example(Special.class);
        keywords = "%" + keywords + "%";
        example.createCriteria().andLike("special_name", keywords);//name为你想要搜索的字段
        PageHelper.startPage(page, limit);
        List<Special> list = this.specialMapper.selectByExample(example);
        return new LayUiResult("0", "查询成功", new PageInfo<>(list).getTotal(), list);
    }

}
