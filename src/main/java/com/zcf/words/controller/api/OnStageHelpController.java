package com.zcf.words.controller.api;


import com.zcf.words.common.json.Body;
import com.zcf.words.service.CorrelatiService;
import com.zcf.words.service.FeedbackService;
import com.zcf.words.service.HelpService;
import com.zcf.words.service.PolicyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("help")
public class OnStageHelpController {

    @Autowired
    private HelpService helpService;

    @Autowired
    private FeedbackService feedbackService;

    @Autowired
    private CorrelatiService correlatiService;

    @Autowired
    private PolicyService policyService;

    /**
     * 获取使用帮助
     * @return
     */
    @RequestMapping(value = "info_help", method = RequestMethod.POST)
    @ResponseBody
    public Body helpInfo() {
        return helpService.helpInfo();
    }

    /**
     * 意见反馈
     * @return
     */
    @RequestMapping(value = "crt_feedback", method = RequestMethod.POST)
    @ResponseBody
    public Body crtFeedback(String phone,String content) {
        return feedbackService.crtFeedback(phone,content);
    }

    /**
     * 关于我们
     * @return
     */
    @RequestMapping(value = "info_correlation", method = RequestMethod.POST)
    @ResponseBody
    public Body correlationInfo() {
        return correlatiService.correlationInfo();
    }

    /**
     * 隐私政策
     * @return
     */
    @RequestMapping(value = "info_policy", method = RequestMethod.POST)
    @ResponseBody
    public Body policyInfo() {
        return policyService.policyInfo();
    }



}
