package com.zcf.words.controller.api;

import com.zcf.words.common.json.Body;
import com.zcf.words.service.BannerService;
import com.zcf.words.service.HandbookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("handbook")
public class OnStageHandbookController {

    @Autowired
    private HandbookService handbookService;

    /**
     * 获取恋爱指南详情
     * @return
     */
    @RequestMapping(value = "info_handbook", method = RequestMethod.POST)
    @ResponseBody
    public Body HandbookInfo() {
        return handbookService.HandbookInfo();
    }


}
