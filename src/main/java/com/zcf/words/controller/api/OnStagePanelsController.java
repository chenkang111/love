package com.zcf.words.controller.api;

import com.zcf.words.common.json.Body;
import com.zcf.words.service.AtlasService;
import com.zcf.words.service.PanelsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("panels")
public class OnStagePanelsController {

    @Autowired
    private PanelsService panelsService;

    @Autowired
    private AtlasService atlasService;

    /**
     * 获取展示面列表
     * @return
     */
    @RequestMapping(value = "panelss", method = RequestMethod.POST)
    @ResponseBody
    public Body getPanels() {
        return panelsService.getPanels();
    }

    /**
     * 获取展示面详细图片集
     * @return
     */
    @RequestMapping(value = "atlass", method = RequestMethod.POST)
    @ResponseBody
    public Body getAtlas(String panelsid) {
        return atlasService.getAtlas(panelsid);
    }

}
