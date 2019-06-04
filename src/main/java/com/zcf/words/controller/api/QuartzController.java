package com.zcf.words.controller.api;

import com.zcf.words.common.json.Body;
import com.zcf.words.entity.User;
import com.zcf.words.mapper.UserMapper;
import com.zcf.words.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class QuartzController {


    @Autowired
    private UserMapper userMapper;

    /**
     * VIP定时器：每天凌晨12点10分减少VIP时间
     * @return
     */
    @Scheduled(cron = "0 10 0 * * ?")
    public void updateVIP() {
        List<User> list = new ArrayList<User>();
        list = userMapper.selectAll();
        try {
            for (User user: list) {
                if(!"0".equals(user.getUser_vip().trim())&&Integer.parseInt(user.getUser_vip_time())>0){
                    Integer day = Integer.parseInt(user.getUser_vip_time())-1;
                    user.setUser_vip_time(day.toString());
                    userMapper.updateByPrimaryKeySelective(user);
                }
                if(Integer.parseInt(user.getUser_vip_time())<=0){
                    user.setUser_vip("0");
                    userMapper.updateByPrimaryKeySelective(user);
                }
            }
        } catch (Exception e) {
            System.out.println("定时器异常！");
            e.printStackTrace();
        }
    }


//    /**
//     * 定时器：测试
//     * @return
//     */
//    @Scheduled(cron = "*/5 * * * * ?")
//    public void test() {
//        System.out.println("定时器测试");
//    }
}
