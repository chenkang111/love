package com.zcf.words.service;


import com.alibaba.druid.util.StringUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zcf.words.common.json.Body;
import com.zcf.words.common.json.LayUiResult;
import com.zcf.words.entity.EQtest;
import com.zcf.words.entity.User;
import com.zcf.words.mapper.EQtestMapper;
import com.zcf.words.mapper.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;


@Service
public class EQtestService {

    private static final Logger logger = LoggerFactory.getLogger(EQtestService.class);

    @Autowired
    private EQtestMapper eqtestMapper;

    @Autowired
    private UserMapper userMapper;
    /**
     * 获取情商测试试卷列表
     *
     * @return
     */
    public Body getEQtest() {
        EQtest eqtest = new EQtest();
        List<EQtest> eqtests = new ArrayList<EQtest>();
        eqtests = eqtestMapper.select(eqtest);
        if (eqtests == null||eqtests.size()<=0) {
            return Body.newInstance(542, "获取情商测试试卷列表失败");
        }
        logger.info("= = = 》 {}获取情商测试试卷列表成功", eqtests.size());
        return Body.newInstance(eqtests);
    }

    /**
     * 获取情商测试试卷列表
     *
     * @return
     */
    public Body infoEQtest(String userid,String eqtestid) {
        if (StringUtils.isEmpty(userid)||StringUtils.isEmpty(eqtestid)) {
            return Body.newInstance(451, "参数错误");
        }
        User user  = userMapper.selectByPrimaryKey(Integer.parseInt(userid));
        if(user==null||user.getUser_id().toString().trim().length()<=0){
            return Body.newInstance(404, "用户不存在");
        }
        EQtest eqtest = eqtestMapper.selectByPrimaryKey(Integer.parseInt(eqtestid));
        if(eqtest==null||eqtest.getEqtest_id().toString().trim().length()<=0){
            return Body.newInstance(404, "试卷不存在");
        }
        if(Integer.parseInt(user.getUser_vip())>=Integer.parseInt(eqtest.getEqtest_vip())){
            logger.info("= = = 》 {}获取情商测试试卷列表成功", eqtest);
            return Body.newInstance(eqtest);
        }else {
            return Body.newInstance(401, "会员等级不够,无法进行该测试");
        }
    }

    /***************************************后台接口****************************************/

    public boolean add(EQtest eqtest) {
        return this.eqtestMapper.insertSelective(eqtest) == 1;
    }

    public boolean delete(Integer id) {
        return this.eqtestMapper.deleteByPrimaryKey(id) == 1;
    }

    public boolean update(EQtest eqtest) {
        return this.eqtestMapper.updateByPrimaryKeySelective(eqtest) == 1;
    }

    public LayUiResult query(Integer page, Integer limit) {
        PageHelper.startPage(page, limit);
        List<EQtest> list = this.eqtestMapper.selectAll();
        return new LayUiResult("0", "查询成功", new PageInfo<>(list).getTotal(), list);
    }

    public LayUiResult search(Integer page, Integer limit,String keywords) {
        Example example = new Example(EQtest.class);
        keywords = "%" + keywords + "%";
        example.createCriteria().andLike("eqtest_title", keywords);//name为你想要搜索的字段
        PageHelper.startPage(page, limit);
        List<EQtest> list = this.eqtestMapper.selectByExample(example);
        return new LayUiResult("0", "查询成功", new PageInfo<>(list).getTotal(), list);
    }
}
