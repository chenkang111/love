package com.zcf.words.service;


import com.alibaba.druid.util.StringUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zcf.words.common.json.Body;
import com.zcf.words.common.json.LayUiResult;
import com.zcf.words.entity.Comment;
import com.zcf.words.entity.Discussion;
import com.zcf.words.entity.Report;
import com.zcf.words.entity.User;
import com.zcf.words.mapper.CommentMapper;
import com.zcf.words.mapper.DiscussionMapper;
import com.zcf.words.mapper.ReportMapper;
import com.zcf.words.mapper.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.*;


@Service
public class DiscussionService {

    private static final Logger logger = LoggerFactory.getLogger(DiscussionService.class);


    @Autowired
    private DiscussionMapper discussionMapper;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private ReportMapper reportMapper;

    @Autowired
    private UserMapper userMapper;




    public Body  reportdis(Integer id, Integer  type,String content,Integer userid,String title){
        Report r = new Report();
        r.setReport_discussion_id(id);
        r.setType(type);
        r.setReport_user_id(userid);
        if(reportMapper.selectCount(r)>0){
            return Body.newInstance(201,"你已经举报过该信息");
        }
        r.setTitle(title);
        r.setReport_content(content);
        r.setCreate_time(new Date());
        return reportMapper.insert(r)>0?Body.BODY_200:Body.newInstance(201,"失败");
    }


    /**
     * 发布提问讨论
     * @return
     */
    public Body crtDiscussion(String userid, String title,String content,String pic,String anonymity) {
        Discussion discussion = new Discussion();
        if(userid==null||userid.length()<=0){
            return Body.newInstance(451,"发布提问讨论失败,参数不正确");
        }
        if(title==null||title.length()<=0){
            return Body.newInstance(451,"发布提问讨论失败,参数不正确");
        }
        if(content==null||content.length()<=0){
            return Body.newInstance(451,"发布提问讨论失败,参数不正确");
        }
        if(anonymity==null||anonymity.length()<=0){
            return Body.newInstance(451,"发布提问讨论失败,参数不正确");
        }
        discussion.setDiscussion_user_id(Integer.parseInt(userid));
        discussion.setDiscussion_title(title);
        discussion.setDiscussion_content(content);
        discussion.setDiscussion_pic(pic);
        discussion.setDiscussion_anonymity(anonymity);
        discussion.setDiscussion_splendid("0");
        int count = discussionMapper.insertSelective(discussion);
        if(count>0){
            logger.info("= = = 》 {}发布提问讨论成功", new Date());
            return Body.newInstance(200,"发布提问讨论成功");
        }else {
            logger.info("= = = 》 {}发布提问讨论失敗", new Date());
            return Body.newInstance(500,"发布提问讨论失敗");
        }

    }

    /**
     * 查询提问讨论
     * @return
     */
    public Body selDiscussion(String title) {
        Example exp = new Example(Discussion.class);
        exp.createCriteria().orLike("discussion_title","%"+title+"%");
        List<Discussion> list = new ArrayList<Discussion>();
        list = discussionMapper.selectByExample(exp);
        logger.info("= = = 》 {}查询提问讨论成功", new Date());
        return Body.newInstance(list);
    }

    /**
     * 查询提问讨论内容信息
     * @return
     */
    public Body infoDiscussion(String discussionid) {
        Map<String,Object> data = new HashMap<String,Object>();
        List<Comment> comments = new ArrayList<Comment>();
        Discussion discussion = new Discussion();
        Comment comment = new Comment();
        if(discussionid==null||discussionid.length()<=0){
            return Body.newInstance(451,"查询失败,参数不正确");
        }
        discussion = discussionMapper.selectByPrimaryKey(Integer.parseInt(discussionid));
        discussion.setUser(userMapper.selectByPrimaryKey(discussion.getDiscussion_user_id()));
        if (discussion == null) {
            return Body.newInstance(542, "用户不存在");
        }
        comment.setComment_discussion_id(discussion.getDiscussion_id());
        comments = commentMapper.select(comment);
        for (Comment c :comments) {
            c .setUser(userMapper.selectByPrimaryKey(c.getComment_user_id()));
        }
        data.put("discussion",discussion);
        data.put("comments",comments);
        logger.info("= = = 》 {}查询讨论内容信息成功", new Date());
        return Body.newInstance(data);
    }



    /**
     * 根据用户ID查询提问讨论
     * @return
     */
    public Body selUserDiscussion(String userid) {
        Discussion discussion = new Discussion();
        List<Discussion> list = new ArrayList<Discussion>();
        if (StringUtils.isEmpty(userid)){
            return Body.newInstance(451, "查询提问讨论失败,参数异常");
        }
        discussion.setDiscussion_user_id(Integer.parseInt(userid));
        list = discussionMapper.select(discussion);
        logger.info("= = = 》 {}查询提问讨论成功", new Date());
        return Body.newInstance(list);
    }


    /***************************************后台接口****************************************/

    public boolean add(Discussion discussion) {
        return this.discussionMapper.insertSelective(discussion) == 1;
    }

    public boolean delete(Integer id) {
        return this.discussionMapper.deleteByPrimaryKey(id) == 1;
    }

    public boolean update(Discussion discussion) {
        return this.discussionMapper.updateByPrimaryKeySelective(discussion) == 1;
    }

    public LayUiResult query(Integer page, Integer limit) {
        PageHelper.startPage(page, limit);
        Example e = new Example(Discussion.class);
        e.orderBy("create_time").desc();
        List<Discussion> list = this.discussionMapper.selectByExample(e);
        for (Discussion  discussion:list) {
            User user = new User();
            user = userMapper.selectByPrimaryKey(discussion.getDiscussion_user_id());
            discussion.setUser(user);
        }
        return new LayUiResult("0", "查询成功", new PageInfo<>(list).getTotal(), list);
    }

    public LayUiResult search(Integer page, Integer limit,String keywords) {
        Example example = new Example(Discussion.class);
        keywords = "%" + keywords + "%";
        example.createCriteria().andLike("discussion_title", keywords);//name为你想要搜索的字段
        PageHelper.startPage(page, limit);
        List<Discussion> list = this.discussionMapper.selectByExample(example);
        for (Discussion  discussion:list) {
            User user = userMapper.selectByPrimaryKey(discussion.getDiscussion_user_id());
            discussion.setUser(user);
        }
        return new LayUiResult("0", "查询成功", new PageInfo<>(list).getTotal(), list);
    }

}
