package com.zcf.words.service;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zcf.words.common.json.Body;
import com.zcf.words.common.json.LayUiResult;
import com.zcf.words.entity.Banner;
import com.zcf.words.entity.User;
import com.zcf.words.mapper.BannerMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;


@Service
public class BannerService {

    private static final Logger logger = LoggerFactory.getLogger(BannerService.class);

    @Autowired
    private BannerMapper bannerMapper;

    /**
     * 获取滚动海报
     *
     * @return
     */
    public Body getBanner() {
        Banner banner = new Banner();
        List<Banner> banners = new ArrayList<Banner>();
        banners = bannerMapper.select(banner);
        if (banners == null||banners.size()<=0) {
            return Body.newInstance(542, "没有海报");
        }
        logger.info("= = = 》 {}查询海报成功", banners.size());
        return Body.newInstance(banners);
    }

    /***************************************后台接口****************************************/

    public boolean add(Banner banner) {
        return this.bannerMapper.insertSelective(banner) == 1;
    }

    public boolean delete(Integer id) {
        return this.bannerMapper.deleteByPrimaryKey(id) == 1;
    }

    public boolean update(Banner banner) {
        return this.bannerMapper.updateByPrimaryKeySelective(banner) == 1;
    }

    public LayUiResult query(Integer page, Integer limit) {
        PageHelper.startPage(page, limit);
        List<Banner> list = this.bannerMapper.selectAll();
        return new LayUiResult("0", "查询成功", new PageInfo<>(list).getTotal(), list);
    }

    public LayUiResult search(Integer page, Integer limit,String keywords) {
        Example example = new Example(Banner.class);
        keywords = "%" + keywords + "%";
        example.createCriteria().andLike("banner_title", keywords);//name为你想要搜索的字段
        PageHelper.startPage(page, limit);
        List<Banner> list = this.bannerMapper.selectByExample(example);
        return new LayUiResult("0", "查询成功", new PageInfo<>(list).getTotal(), list);
    }
}
