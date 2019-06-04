package com.zcf.words.controller.console;


import com.zcf.words.common.json.LayUiResult;
import com.zcf.words.common.utils.FileUploadUtils;
import com.zcf.words.entity.Account;
import com.zcf.words.service.AccountService;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
* Created by YuanQJ on 2018/11/01.
*/
@RestController
@RequestMapping("s/account")
public class AccountController {

    @Autowired
    private AccountService accountservice;


    @PostMapping("login")
    public Map login(@ModelAttribute Account account) {
        return this.accountservice.login(account);
    }


    @RequestMapping("add")
    public boolean add(@RequestBody Account account) {
        return this.accountservice.add(account);
    }

    @RequestMapping("delete")
    public boolean delete(@RequestParam Integer id) {
        return this.accountservice.delete(id);
    }

    @RequestMapping("update")
    public boolean update(@RequestBody Account account) {
        return this.accountservice.update(account);
    }

    @RequestMapping("query")
    public LayUiResult queryAccount(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "20") Integer limit) {
        return this.accountservice.query(page,limit);
    }

    @RequestMapping("search")
    public LayUiResult search(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "20") Integer limit,
        @RequestParam String keywords) {
        return this.accountservice.search(page,limit,keywords);
    }

    @RequestMapping("upload")
    public Map UploadBrand(@RequestParam("file") MultipartFile file, HttpServletRequest request){
        //String pathVal = request.getSession().getServletContext().getRealPath("/") + "WEB-INF/";
        String pathVal = null;
        // try {
        //pathVal = ResourceUtils.getURL("classpath:").getPath() + "static/";
        pathVal = "c://upload/";
        return FileUploadUtils.uploadLayUiImg(file, pathVal,"userfiles/fileupload/");
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//            Map LayUiImageResult = new HashMap<>();
//            LayUiImageResult.put("code", 500);
//            LayUiImageResult.put("msg", "上传失败");
//            return LayUiImageResult;
//        }
    }

}
