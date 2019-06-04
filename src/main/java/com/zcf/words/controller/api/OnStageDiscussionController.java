package com.zcf.words.controller.api;

import com.alipay.api.internal.util.StringUtils;
import com.zcf.words.common.json.Body;
import com.zcf.words.service.CommentService;
import com.zcf.words.service.DiscussionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("discussion")
public class OnStageDiscussionController {


    @Autowired
    private DiscussionService discussionService;

    @Autowired
    private CommentService commentService;


    /**
     * 发布提问讨论
     * phone 手机号
     * password 密码
     * @return
     */
    @RequestMapping(value = "crt_discussion", method = RequestMethod.POST)
    @ResponseBody
    public Body crtDiscussion(String userid, String title,String content,String pic,String anonymity) {
        return discussionService.crtDiscussion(userid,title,content,pic,anonymity);
    }

    /**
     * 进行评论
     * @return
     */
    @RequestMapping(value = "crt_comment", method = RequestMethod.POST)
    @ResponseBody
    public Body crtComment(String discussionid, String userid,String content,String pic,String anonymity) {
        return commentService.crtComment(discussionid,userid,content,pic,anonymity);
    }

    /**
     * 查询提问讨论
     * @return
     */
    @RequestMapping(value = "sel_discussion", method = RequestMethod.POST)
    @ResponseBody
    public Body selDiscussion(String title) {
        return discussionService.selDiscussion(title);
    }

    /**
     * 查询提问讨论内容信息
     * @return
     */
    @RequestMapping(value = "info_discussion", method = RequestMethod.POST)
    @ResponseBody
    public Body infoDiscussion(String discussionid) {
        return discussionService.infoDiscussion(discussionid);
    }


    /**
     * 举报提问讨论   //举报
     * @return
     */
    @RequestMapping(value = "reportdis", method = RequestMethod.POST)
    @ResponseBody
    public Body reportdis(Integer id, Integer  type,String content,Integer userid,String title) {
        if(null!=id && null!=type && !StringUtils.isEmpty(content) && null!=userid){
             return discussionService.reportdis(id, type,content,userid,title);
        }
        return Body.BODY_451;
    }



    /**
     * 查询用户提问
     * userid 用户ID
     * @return
     */
    @RequestMapping(value = "sel_userdiscussion", method = RequestMethod.POST)
    @ResponseBody
    public Body selUserDiscussion(String userid) {
        return discussionService.selUserDiscussion(userid);
    }

    /**
     * 查询用户回答
     * userid 用户ID
     * @return
     */
    @RequestMapping(value = "sel_usercomment", method = RequestMethod.POST)
    @ResponseBody
    public Body selUserComment(String userid) {
        return commentService.selUserComment(userid);
    }

}
