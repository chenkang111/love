package com.zcf.words.service;


import com.alibaba.druid.util.StringUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zcf.words.common.json.Body;
import com.zcf.words.common.json.LayUiResult;
import com.zcf.words.entity.Comment;
import com.zcf.words.entity.Discussion;
import com.zcf.words.entity.User;
import com.zcf.words.mapper.CommentMapper;
import com.zcf.words.mapper.DiscussionMapper;
import com.zcf.words.mapper.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
public class CommentService {

    private static final Logger logger = LoggerFactory.getLogger(CommentService.class);

    @Autowired
    private CommentMapper commentMapper;


    @Autowired
    private DiscussionMapper discussionMapper;

    @Autowired
    private UserMapper userMapper;

    /**
     * 进行评论
     * @return
     */
    public Body crtComment(String discussionid, String userid,String content,String pic,String anonymity) {
        Comment comment = new Comment();
        if(discussionid==null||discussionid.length()<=0){
            return Body.newInstance(451,"评论失败,参数不正确");
        }
        if(userid==null||userid.length()<=0){
            return Body.newInstance(451,"评论失败,参数不正确");
        }
        if(content==null||content.length()<=0){
            return Body.newInstance(451,"评论失败,参数不正确");
        }
        if(anonymity==null||anonymity.length()<=0){
            return Body.newInstance(451,"评论失败,参数不正确");
        }
        comment.setComment_discussion_id(Integer.parseInt(discussionid));
        comment.setComment_user_id(Integer.parseInt(userid));
        comment.setComment_content(content);
        comment.setComment_anonymity(anonymity);
        comment.setComment_pic(pic);
        int count = commentMapper.insertSelective(comment);
        if(count>0){
            Discussion discussion = discussionMapper.selectByPrimaryKey(Integer.parseInt(discussionid));
            Integer discount = (Integer.parseInt(discussion.getDiscussion_count())+1);
            discussion.setDiscussion_count(discount.toString());
            discussionMapper.updateByPrimaryKeySelective(discussion);
            logger.info("= = = 》 {}进行评论成功", count);
            return Body.newInstance(200,"评论成功");
        }else {
            logger.info("= = = 》 {}进行评论失败", count);
            return Body.newInstance(500,"评论失败");
        }

    }


    /**
     * 根据用户ID查询评论
     * @return
     */
    public Body selUserComment(String userid) {
        Comment comment = new Comment();
        List<Comment> list = new ArrayList<Comment>();
        if (StringUtils.isEmpty(userid)){
            return Body.newInstance(451, "查询评论失败,参数异常");
        }
        comment.setComment_user_id(Integer.parseInt(userid));
        list = commentMapper.select(comment);
        logger.info("= = = 》 {}查询评论成功", new Date());
        return Body.newInstance(list);
    }


    /***************************************后台接口****************************************/

    public boolean add(Comment comment) {
        return this.commentMapper.insertSelective(comment) == 1;
    }

    public boolean delete(Integer id) {
        return this.commentMapper.deleteByPrimaryKey(id) == 1;
    }

    public boolean update(Comment comment) {
        return this.commentMapper.updateByPrimaryKeySelective(comment) == 1;
    }

    public LayUiResult query(Integer page, Integer limit) {
        PageHelper.startPage(page, limit);
        Example example = new Example(Comment.class);
        example.orderBy("create_time").desc();
        List<Comment> list = this.commentMapper.selectByExample(example);
        for (Comment  comment:list) {
            User  user = userMapper.selectByPrimaryKey(comment.getComment_user_id());
            Discussion discussion = discussionMapper.selectByPrimaryKey(comment.getComment_discussion_id());
            comment.setUser(user);
            comment.setDiscussion(discussion);
        }
        return new LayUiResult("0", "查询成功", new PageInfo<>(list).getTotal(), list);
    }

    public LayUiResult search(Integer page, Integer limit,String keywords) {
        Example example = new Example(Comment.class);
        keywords = "%" + keywords + "%";
        example.createCriteria().andLike("comment_content", keywords);//name为你想要搜索的字段
        PageHelper.startPage(page, limit);
        List<Comment> list = this.commentMapper.selectByExample(example);
        for (Comment  comment:list) {
            User user = userMapper.selectByPrimaryKey(comment.getComment_user_id());
            Discussion discussion = discussionMapper.selectByPrimaryKey(comment.getComment_discussion_id());
            comment.setUser(user);
            comment.setDiscussion(discussion);
        }
        return new LayUiResult("0", "查询成功", new PageInfo<>(list).getTotal(), list);
    }
}
