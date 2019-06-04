package com.zcf.words.controller.api;

import com.zcf.words.common.json.Body;
import com.zcf.words.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("special")
public class OnStageSpecialController {

    @Autowired
    private SpecialService specialService;


    @Autowired
    private ToptitleService toptitleService;

    @Autowired
    private ToptagService toptagService;

    @Autowired
    private HighlightService highlightService;

    @Autowired
    private HighcontentService highcontentService;

    /**
     * 获取精彩专题
     * @return
     */
    @RequestMapping(value = "specials", method = RequestMethod.POST)
    @ResponseBody
    public Body getSpecial() {
        return specialService.getSpecial();
    }

    /**
     * 获取专题标题
     * @return
     */
    @RequestMapping(value = "toptitles", method = RequestMethod.POST)
    @ResponseBody
    public Body getToptitle(String specialid) {
        return toptitleService.getToptitle(specialid);
    }

    /**
     * 获取专题标签
     * @return
     */
    @RequestMapping(value = "toptags", method = RequestMethod.POST)
    @ResponseBody
    public Body getToptag(String toptitleid) {
        return toptagService.getToptag(toptitleid);
    }

    /**
     * 获取专题开场白
     * @return
     */
    @RequestMapping(value = "highlights", method = RequestMethod.POST)
    @ResponseBody
    public Body Highlights(String toptagid,String content) {
        return highlightService.getHighlight(toptagid,content);
    }

    /**
     * 获取专题开场白对话内容
     * @return
     */
    @RequestMapping(value = "info_highlight", method = RequestMethod.POST)
    @ResponseBody
    public Body HighlightInfo(String highlightid) {
        return highcontentService.getHighlightInfo(highlightid);
    }

}
