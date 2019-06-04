package com.zcf.words.service;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zcf.words.common.json.Body;
import com.zcf.words.common.json.LayUiResult;
import com.zcf.words.entity.Vip;
import com.zcf.words.mapper.VipMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;


@Service
public class VipService {

    private static final Logger logger = LoggerFactory.getLogger(VipService.class);


    @Autowired
    private VipMapper vipMapper;

    /**
     * 添加会员充值账单
     *
     * @return
     */
    public Vip selVip(String totalFee) {
        //查询充值的VIP类型
        Example exp = new Example(Vip.class);
        exp.createCriteria().andEqualTo("vip_price",totalFee);
        List<Vip> vipList = vipMapper.selectByExample(exp);
        if (vipList!=null&&vipList.size()>0){
            return vipList.get(0);
        }else
        {
            return null;
        }
    }

    public  Vip  selectbyid(String id){
        return vipMapper.selectByPrimaryKey(id);
    }



    /**
     * 添加会员充值账单
     *
     * @return
     */
    public Body getVip() {
        Vip vip = new Vip();
        //查询充值的VIP类型
        List<Vip> vipList = vipMapper.select(vip);
        if (vipList!=null&&vipList.size()>0){
            return Body.newInstance(vipList);
        }else
        {
            return Body.newInstance(470,"查询失败");
        }
    }

    /***************************************后台接口****************************************/

    public boolean add(Vip vip) {
        return this.vipMapper.insertSelective(vip) == 1;
    }

    public boolean delete(Integer id) {
        return this.vipMapper.deleteByPrimaryKey(id) == 1;
    }

    public boolean update(Vip vip) {
        return this.vipMapper.updateByPrimaryKeySelective(vip) == 1;
    }

    public LayUiResult query(Integer page, Integer limit) {
        PageHelper.startPage(page, limit);
        List<Vip> list = this.vipMapper.selectAll();
        return new LayUiResult("0", "查询成功", new PageInfo<>(list).getTotal(), list);
    }

    public LayUiResult search(Integer page, Integer limit, String keywords) {
        Example example = new Example(Vip.class);
        keywords = "%" + keywords + "%";
        example.createCriteria().andLike("vip_name", keywords);//name为你想要搜索的字段
        PageHelper.startPage(page, limit);
        List<Vip> list = this.vipMapper.selectByExample(example);
        return new LayUiResult("0", "查询成功", new PageInfo<>(list).getTotal(), list);
    }
}
