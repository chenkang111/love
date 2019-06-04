package com.zcf.words.service;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zcf.words.common.json.Body;
import com.zcf.words.common.json.LayUiResult;
import com.zcf.words.entity.Feedback;
import com.zcf.words.mapper.FeedbackMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


@Service
public class FeedbackService {

    private static final Logger logger = LoggerFactory.getLogger(FeedbackService.class);

    @Autowired
    private FeedbackMapper feedbackMapper;

    /**
     * 发送意见反馈
     * @param phone
     * @param content···········································
     * @return
     */
    public Body crtFeedback(String phone,String content) {
        if(content==null||content.length()<10){
            return Body.newInstance(451, "发送意见反馈失败,反馈内容要达到10字以上");
        }
        Feedback feedback = new Feedback();
        feedback.setFeedback_content(content);
        feedback.setFeedback_phone(phone);

        int count = feedbackMapper.insertSelective(feedback);
        if(count>0) {
            return Body.newInstance(200, "发送意见反馈成功");
        }else{
            return Body.newInstance(500, "发送意见反馈失败");
        }
    }

    /***************************************后台接口****************************************/

    public boolean add(Feedback feedback) {
        return this.feedbackMapper.insertSelective(feedback) == 1;
    }

    public boolean delete(Integer id) {
        return this.feedbackMapper.deleteByPrimaryKey(id) == 1;
    }

    public boolean update(Feedback feedback) {
        return this.feedbackMapper.updateByPrimaryKeySelective(feedback) == 1;
    }

    public LayUiResult query(Integer page, Integer limit) {
        PageHelper.startPage(page, limit);
        List<Feedback> list = this.feedbackMapper.selectAll();
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return new LayUiResult("0", "查询成功", new PageInfo<>(list).getTotal(), list);
    }

    public LayUiResult search(Integer page, Integer limit,String keywords) {
        Example example = new Example(Feedback.class);
        keywords = "%" + keywords + "%";
        example.createCriteria().andLike("feedback_content", keywords);//name为你想要搜索的字段
        PageHelper.startPage(page, limit);
        List<Feedback> list = this.feedbackMapper.selectByExample(example);
        return new LayUiResult("0", "查询成功", new PageInfo<>(list).getTotal(), list);
    }
}
