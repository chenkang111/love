package com.zcf.words.service;


import com.alibaba.druid.util.StringUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zcf.words.common.json.Body;
import com.zcf.words.common.json.LayUiResult;
import com.zcf.words.entity.Atlas;
import com.zcf.words.entity.Panels;
import com.zcf.words.mapper.AtlasMapper;
import com.zcf.words.mapper.PanelsMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;


@Service
public class AtlasService {

    private static final Logger logger = LoggerFactory.getLogger(AtlasService.class);

    @Autowired
    private AtlasMapper atlasMapper;

    @Autowired
    private PanelsMapper panelsMapper;
    /**
     * 获取图片集
     *
     * @return
     */
    public Body getAtlas(String panelsid) {
        Atlas atlas = new Atlas();
        List<Atlas> atlass = new ArrayList<Atlas>();
        if (StringUtils.isEmpty(panelsid)) {
            return Body.newInstance(451, "获取图片集失败,参数异常");
        }
        atlas.setAtlas_panels_id(Integer.parseInt(panelsid));
        atlass = atlasMapper.select(atlas);
        if (atlass == null||atlass.size()<=0) {
            return Body.newInstance(542, "没有图片");
        }
        logger.info("= = = 》 {}获取图片集成功", atlass.size());
        return Body.newInstance(atlass);
    }

    /***************************************后台接口****************************************/


    public boolean add(Atlas atlas) {
        return this.atlasMapper.insertSelective(atlas) == 1;
    }

    public boolean delete(Integer id) {
        return this.atlasMapper.deleteByPrimaryKey(id) == 1;
    }

    public boolean update(Atlas atlas) {
        return this.atlasMapper.updateByPrimaryKeySelective(atlas) == 1;
    }

    public LayUiResult query(Integer page, Integer limit) {
        PageHelper.startPage(page, limit);
        List<Atlas> list = this.atlasMapper.selectAll();
        for (Atlas  atlas:list) {
            Panels pnels = panelsMapper.selectByPrimaryKey(atlas.getAtlas_panels_id());
            atlas.setPanels(pnels);
        }
        return new LayUiResult("0", "查询成功", new PageInfo<>(list).getTotal(), list);
    }

    public LayUiResult search(Integer page, Integer limit,String keywords) {
        Example example = new Example(Atlas.class);
        keywords = "%" + keywords + "%";
        example.createCriteria().andLike("atlas_title", keywords);//name为你想要搜索的字段
        PageHelper.startPage(page, limit);
        List<Atlas> list = this.atlasMapper.selectByExample(example);
        for (Atlas  atlas:list) {
            Panels pnels = panelsMapper.selectByPrimaryKey(atlas.getAtlas_panels_id());
            atlas.setPanels(pnels);
        }
        return new LayUiResult("0", "查询成功", new PageInfo<>(list).getTotal(), list);
    }
}
