package com.zcf.words.controller.console;


import com.zcf.words.common.json.LayUiResult;
import com.zcf.words.common.utils.FileUploadUtils;
import com.zcf.words.entity.User;
import com.zcf.words.service.UserService;
import com.zcf.words.service.VipService;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.FileNotFoundException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
/**
* Created by YuanQJ on 2018/11/01.
*/
@RestController
@RequestMapping("s/user")
public class UserController {

    @Autowired
    private UserService userservice;

    @Autowired
    private  VipService vipService;


    @RequestMapping("add")
    public boolean add(@RequestBody User user) {
        return this.userservice.add(user);
    }

    @RequestMapping("delete")
    public boolean delete(@RequestParam Integer id) {
        return this.userservice.delete(id);
    }

    @RequestMapping("update")
    public boolean update(@RequestBody User user) {
        return this.userservice.update(user);
    }


    @RequestMapping("updatelevel")
    public boolean updatelevel(@RequestBody User user) {
        if(user.getUser_vip().equals("0")){
            user.setUser_vip_time("0");
        }
        if(user.getUser_vip().equals("1")){
            user.setUser_vip_time(vipService.selectbyid("1").getVip_day());
        }
        if(user.getUser_vip().equals("2")){
            user.setUser_vip_time(vipService.selectbyid("2").getVip_day());
        }
        if(user.getUser_vip().equals("3")){
            user.setUser_vip_time(vipService.selectbyid("3").getVip_day());
        }
        System.out.println(user.toString());
        return this.userservice.update(user);
    }




    @RequestMapping("query")
    public LayUiResult queryUser(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "20") Integer limit) {
        return this.userservice.query(page,limit);
    }

    @RequestMapping("search")
    public LayUiResult search(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "20") Integer limit,
                              @RequestParam String keywords) {
        return this.userservice.search(page,limit,keywords);
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
