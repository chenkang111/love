package com.zcf.words.service;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zcf.words.common.json.Body;
import com.zcf.words.common.json.LayUiResult;
import com.zcf.words.entity.Comment;
import com.zcf.words.entity.Discussion;
import com.zcf.words.entity.Report;
import com.zcf.words.entity.User;
import com.zcf.words.mapper.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;


@Service
public class ReportService {

    private static final Logger logger = LoggerFactory.getLogger(ReportService.class);

    @Autowired
    private ReportMapper reportmapper;

    @Autowired
    private CommentMapper commentMapper;


    @Autowired
    private DiscussionMapper discussionMapper;

    @Autowired
    private UserMapper userMapper;


    /***************************************后台接口****************************************/



    public boolean add(Report report) {
        return this.reportmapper.insertSelective(report) == 1;
    }

    public boolean delete(Integer id) {
        return this.reportmapper.deleteByPrimaryKey(id) == 1;
    }
    //这个是删除
    public Body getOne(Integer id){
        Report r =reportmapper.selectByPrimaryKey(id);
        if(r.getType()==1){
            // 是帖子的
            if(discussionMapper.deleteByPrimaryKey(r.getReport_discussion_id())>0){
               return  reportmapper.deleteByPrimaryKey(id)>0?Body.BODY_200:Body.newInstance(201,"失败");
            }
        }else if(r.getType()==2){
            //是评论的
            if(commentMapper.deleteByPrimaryKey(r.getReport_discussion_id())>0){
                return  reportmapper.deleteByPrimaryKey(id)>0?Body.BODY_200:Body.newInstance(201,"失败");
            }
        }
        return Body.newInstance(201,"异常");
    }


    public boolean update(Report report) {
        return this.reportmapper.updateByPrimaryKeySelective(report) == 1;
    }

    public LayUiResult query(Integer page, Integer limit) {
        PageHelper.startPage(page, limit);
        Example e = new Example(Report.class);
        e.orderBy("create_time").desc();
        List<Report> list = this.reportmapper.selectByExample(e);
        for (Report report:list) {
            User user = userMapper.selectByPrimaryKey(report.getReport_user_id());
            report.setUser(user);
        }
        return new LayUiResult("0", "查询成功", new PageInfo<>(list).getTotal(), list);
    }

    public LayUiResult search(Integer page, Integer limit,String keywords) {
        Example example = new Example(Report.class);
        keywords = "%" + keywords + "%";
        example.createCriteria().andLike("report_content", keywords);//name为你想要搜索的字段
        PageHelper.startPage(page, limit);
        List<Report> list = this.reportmapper.selectByExample(example);
        for (Report report:list) {
            User user = userMapper.selectByPrimaryKey(report.getReport_user_id());
            report.setUser(user);
        }
        return new LayUiResult("0", "查询成功", new PageInfo<>(list).getTotal(), list);
    }
}
