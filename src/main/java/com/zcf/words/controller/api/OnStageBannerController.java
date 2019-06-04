package com.zcf.words.controller.api;

import com.zcf.words.common.json.Body;
import com.zcf.words.service.BannerService;
import com.zcf.words.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("banner")
public class OnStageBannerController {

    @Autowired
    private BannerService bannerService;

    /**
     * 获取滚动海报
     * @return
     */
    @RequestMapping(value = "banners", method = RequestMethod.POST)
    @ResponseBody
    public Body getBanner() {
        return bannerService.getBanner();
    }


}
