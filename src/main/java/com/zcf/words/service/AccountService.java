package com.zcf.words.service;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zcf.words.common.json.Body;
import com.zcf.words.common.json.LayUiResult;
import com.zcf.words.entity.Account;
import com.zcf.words.entity.User;
import com.zcf.words.mapper.AccountMapper;
import com.zcf.words.mapper.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class AccountService {

    private static final Logger logger = LoggerFactory.getLogger(AccountService.class);

    @Autowired
    private AccountMapper accountmapper;

    public Map login(Account account) {
        List<Account> list = this.accountmapper.select(account);
        Map<String,Object> result = new HashMap<>();
        if (list!=null&&list.size()>0){
            Map<String,Object> d = new HashMap<>();
            d.put("access_token",list.get(0).getAccess_token());
            result.put("code",0);
            result.put("msg","登入成功");
            result.put("data",d);
            return result;
        }else {
            result.put("code",404);
            result.put("msg","登入失败");
            result.put("data","");
            return result;
        }

    }

    /***************************************后台接口****************************************/

    public boolean add(Account account) {
        return this.accountmapper.insertSelective(account) == 1;
    }

    public boolean delete(Integer id) {
        return this.accountmapper.deleteByPrimaryKey(id) == 1;
    }

    public boolean update(Account account) {
        return this.accountmapper.updateByPrimaryKeySelective(account) == 1;
    }

    public LayUiResult query(Integer page, Integer limit) {
        PageHelper.startPage(page, limit);
        List<Account> list = this.accountmapper.selectAll();
        return new LayUiResult("0", "查询成功", new PageInfo<>(list).getTotal(), list);
    }

    public LayUiResult search(Integer page, Integer limit,String keywords) {
        Example example = new Example(Account.class);
        keywords = "%" + keywords + "%";
        example.createCriteria().andLike("name", keywords);//name为你想要搜索的字段
        PageHelper.startPage(page, limit);
        List<Account> list = this.accountmapper.selectByExample(example);
        return new LayUiResult("0", "查询成功", new PageInfo<>(list).getTotal(), list);
    }


}
