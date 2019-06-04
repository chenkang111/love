package com.zcf.words.service;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zcf.words.common.json.Body;
import com.zcf.words.common.json.LayUiResult;
import com.zcf.words.entity.Special;
import com.zcf.words.entity.Toptitle;
import com.zcf.words.mapper.SpecialMapper;
import com.zcf.words.mapper.ToptitleMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;


@Service
public class ToptitleService {

    private static final Logger logger = LoggerFactory.getLogger(ToptitleService.class);

    @Autowired
    private ToptitleMapper toptitleMapper;

    @Autowired
    private SpecialMapper specialMapper;

    /**
     * 获取专题标题
     *
     * @return
     */
    public Body getToptitle(String specialid) {
        if(specialid==null||specialid.trim().length()<=0){
            return Body.newInstance(451, "参数错误");
        }
        Toptitle toptitle = new Toptitle();
        toptitle.setToptitle_special_id(Integer.parseInt(specialid));
        List<Toptitle> toptitles = new ArrayList<Toptitle>();
        toptitles = toptitleMapper.select(toptitle);
        if (toptitles == null || toptitles.size() <= 0) {
            return Body.newInstance(404, "查询专题标题失败");
        }
        logger.info("= = = 》 {}查询专题标题成功", toptitles.size());
        return Body.newInstance(toptitles);
    }

    /***************************************后台接口****************************************/

    //新增
    public boolean add(Toptitle toptitle) {
        return this.toptitleMapper.insertSelective(toptitle) == 1;
    }

    //删除
    public boolean delete(Integer id) {
        return this.toptitleMapper.deleteByPrimaryKey(id) == 1;
    }

    //更新
    public boolean update(Toptitle toptitle) {
        return this.toptitleMapper.updateByPrimaryKeySelective(toptitle) == 1;
    }

    //查询
    public LayUiResult query(Integer page, Integer limit) {
        PageHelper.startPage(page, limit);
        List<Toptitle> list = this.toptitleMapper.selectAll();
        for (Toptitle  toptitle:list) {
            Special special = specialMapper.selectByPrimaryKey(toptitle.getToptitle_special_id());
            toptitle.setSpecial(special);
        }
        return new LayUiResult("0", "查询成功", new PageInfo<>(list).getTotal(), list);
    }

    //搜索
    public LayUiResult search(Integer page, Integer limit, String keywords) {
        Example example = new Example(Toptitle.class);
        example.createCriteria().andLike("toptitle_title", "%" + keywords + "%");//name为你想要搜索的字段
        PageHelper.startPage(page, limit);
        List<Toptitle> list = this.toptitleMapper.selectByExample(example);
        for (Toptitle  toptitle:list) {
            Special special = specialMapper.selectByPrimaryKey(toptitle.getToptitle_special_id());
            toptitle.setSpecial(special);
        }
        return new LayUiResult("0", "查询成功", new PageInfo<>(list).getTotal(), list);
    }

}
