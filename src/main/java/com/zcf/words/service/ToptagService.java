package com.zcf.words.service;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zcf.words.common.json.Body;
import com.zcf.words.common.json.LayUiResult;
import com.zcf.words.entity.Special;
import com.zcf.words.entity.Toptag;
import com.zcf.words.entity.Toptitle;
import com.zcf.words.mapper.SpecialMapper;
import com.zcf.words.mapper.ToptagMapper;
import com.zcf.words.mapper.ToptitleMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;


@Service
public class ToptagService {

    private static final Logger logger = LoggerFactory.getLogger(ToptagService.class);

    @Autowired
    private ToptagMapper toptagMapper;

    @Autowired
    private ToptitleMapper toptitleMapper;

    /**
     * 获取专题标签
     *
     * @return
     */
    public Body getToptag(String toptitleid) {
        Toptag toptag = new Toptag();
        toptag.setToptag_toptitle_id(Integer.parseInt(toptitleid));
        List<Toptag> toptags = new ArrayList<Toptag>();
        toptags = toptagMapper.select(toptag);
        if (toptags == null || toptags.size() <= 0) {
            return Body.newInstance(542, "获取专题标签失败");
        }
        logger.info("= = = 》 {}获取专题标签成功", toptags.size());
        return Body.newInstance(toptags);
    }

    /***************************************后台接口****************************************/


    //新增
    public boolean add(Toptag toptag) {
        return this.toptagMapper.insertSelective(toptag) == 1;
    }

    //删除
    public boolean delete(Integer id) {
        return this.toptagMapper.deleteByPrimaryKey(id) == 1;
    }

    //更新
    public boolean update(Toptag toptag) {
        return this.toptagMapper.updateByPrimaryKeySelective(toptag) == 1;
    }

    //查询
    public LayUiResult query(Integer page, Integer limit) {
        PageHelper.startPage(page, limit);
        List<Toptag> list = this.toptagMapper.selectAll();
        for (Toptag  toptag:list) {
            Toptitle toptitle = toptitleMapper.selectByPrimaryKey(toptag.getToptag_toptitle_id());
            toptag.setToptitle(toptitle);
        }
        return new LayUiResult("0", "查询成功", new PageInfo<>(list).getTotal(), list);
    }

    //搜索
    public LayUiResult search(Integer page, Integer limit,String keywords) {
        Example example = new Example(Toptag.class);
        example.createCriteria().andLike("toptag_name", "%" + keywords + "%");//name为你想要搜索的字段
        PageHelper.startPage(page, limit);
        List<Toptag> list = this.toptagMapper.selectByExample(example);
        for (Toptag  toptag:list) {
            Toptitle toptitle = toptitleMapper.selectByPrimaryKey(toptag.getToptag_toptitle_id());
            toptag.setToptitle(toptitle);
        }
        return new LayUiResult("0", "查询成功", new PageInfo<>(list).getTotal(), list);
    }

}
