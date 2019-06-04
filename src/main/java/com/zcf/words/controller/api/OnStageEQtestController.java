package com.zcf.words.controller.api;

import com.zcf.words.common.json.Body;
import com.zcf.words.service.EQcontentService;
import com.zcf.words.service.EQitemService;
import com.zcf.words.service.EQresultService;
import com.zcf.words.service.EQtestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("eqtest")
public class OnStageEQtestController {


    @Autowired
    private EQtestService eqtestService;

    @Autowired
    private EQitemService eqitemService;

    @Autowired
    private EQresultService eqresultService;


    @Autowired
    private EQcontentService EQcontentService;


    /**
     * 获取情商测试试卷列表
     * @return
     */
    @RequestMapping(value = "eqtests", method = RequestMethod.POST)
    @ResponseBody
    public Body getEQtest() {
        return eqtestService.getEQtest();
    }

    /**
     * 获取情商测试试卷详情
     * @return
     */
    @RequestMapping(value = "eqtest_info", method = RequestMethod.POST)
    @ResponseBody
    public Body infoEQtest(String userid,String eqtestid) {
        return eqtestService.infoEQtest(userid,eqtestid);
    }


    /**
     * 获取情商测试题目内容
     * @return
     */
    @RequestMapping(value = "eqcontent", method = RequestMethod.POST)
    @ResponseBody
    public Body getEQcontent(String eqtestid,String tag) {
        return EQcontentService.getEQcontent(eqtestid,tag);
    }

    /**
     * 获取情商测试选项
     * @return
     */
    @RequestMapping(value = "eqitems", method = RequestMethod.POST)
    @ResponseBody
    public Body getEQitem(String contentid) {
        return eqitemService.getEQitem(contentid);
    }

    /**
     * 获取情商测试答案
     * @return
     */
    @RequestMapping(value = "eqresults", method = RequestMethod.POST)
    @ResponseBody
    public Body getEQresult(String eqtestid,Integer point) {
        return eqresultService.getEQresult(eqtestid,point);
    }


}
