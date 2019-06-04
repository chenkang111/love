package com.zcf.words.controller.api;


import com.zcf.words.common.json.Body;
import com.zcf.words.common.json.LayUiResult;
import com.zcf.words.common.utils.FileUploadUtils;
import com.zcf.words.entity.Chat;
import com.zcf.words.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

/**
* Created by YuanQJ on 2018/11/01.
*/
@RestController
@RequestMapping("chat")
public class OnStageChatController {

    @Autowired
    private ChatService chatservice;

    /**
     * 获取聊天记录
     * @return
     */
    @RequestMapping(value = "chats", method = RequestMethod.POST)
    @ResponseBody
    public Body getchat() {
        return chatservice.getChat();
    }


}
