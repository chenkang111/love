package com.zcf.words.service;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zcf.words.common.json.Body;
import com.zcf.words.common.json.LayUiResult;
import com.zcf.words.entity.Banner;
import com.zcf.words.entity.Bill;
import com.zcf.words.entity.User;
import com.zcf.words.entity.Vip;
import com.zcf.words.mapper.BannerMapper;
import com.zcf.words.mapper.BillMapper;
import com.zcf.words.mapper.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;


@Service
public class BillService {

    private static final Logger logger = LoggerFactory.getLogger(BillService.class);

    @Autowired
    private BillMapper billMapper;

    @Autowired
    private UserMapper userMapper;
    /**
     * 添加会员充值账单
     *
     * @return
     */
    public Bill crtBill(User user, Vip vip,String paytype,String totalFee,String tradeNo) {
        // 添加到账单里
        Bill bill = new Bill();
        bill.setBill_user_id(user.getUser_id());
        bill.setBill_name(vip.getVip_name());
        bill.setBill_price(totalFee);
        bill.setBill_paytype(paytype);// 1支付宝充值 2微信充值
        bill.setBill_service(vip.getVip_content());//充值会员内容
        bill.setMark(tradeNo);
        int count = billMapper.insertSelective(bill);
        if (count>0){
            return bill;
        }else {
            return null;
        }
    }
    public Integer billcount(String tradeno){
        Bill bill = new Bill();
        bill.setMark(tradeno);
        return billMapper.selectCount(bill);
    }


    public Bill  getByBillNo(String billNo){
        Bill b = new Bill();
        b.setMark(billNo);
        return billMapper.selectOne(b);
    }


    /***************************************后台接口****************************************/


    public boolean add(Bill bill) {
        return this.billMapper.insertSelective(bill) == 1;
    }

    public boolean delete(Integer id) {
        return this.billMapper.deleteByPrimaryKey(id) == 1;
    }

    public boolean update(Bill bill) {
        return this.billMapper.updateByPrimaryKeySelective(bill) == 1;
    }

    public LayUiResult query(Integer page, Integer limit) {
        PageHelper.startPage(page, limit);
        List<Bill> list = this.billMapper.selectAll();
        for (Bill  bill:list) {
            User user = new User();
            user = userMapper.selectByPrimaryKey(bill.getBill_user_id());
            bill.setUser(user);
        }
        return new LayUiResult("0", "查询成功", new PageInfo<>(list).getTotal(), list);
    }

    public LayUiResult search(Integer page, Integer limit,String keywords) {
        Example example = new Example(Bill.class);
        keywords = "%" + keywords + "%";
        example.createCriteria().andLike("bill_name", keywords);//name为你想要搜索的字段
        PageHelper.startPage(page, limit);
        List<Bill> list = this.billMapper.selectByExample(example);
        for (Bill  bill:list) {
            User user = new User();
            user = userMapper.selectByPrimaryKey(bill.getBill_user_id());
            bill.setUser(user);
        }
        return new LayUiResult("0", "查询成功", new PageInfo<>(list).getTotal(), list);
    }

}
