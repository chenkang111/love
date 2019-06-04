package com.zcf.words.controller.console;


import com.zcf.words.common.json.LayUiResult;
import com.zcf.words.common.utils.FileUploadUtils;
import com.zcf.words.entity.Policy;
import com.zcf.words.service.PolicyService;
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
@RequestMapping("s/policy")
public class PolicyController {

    @Autowired
    private PolicyService policyservice;

    @RequestMapping("add")
    public boolean add(@RequestBody Policy policy) {
        return this.policyservice.add(policy);
    }

    @RequestMapping("delete")
    public boolean delete(@RequestParam Integer id) {
        return this.policyservice.delete(id);
    }

    @RequestMapping("update")
    public boolean update(@RequestBody Policy policy) {
        return this.policyservice.update(policy);
    }

    @RequestMapping("query")
    public LayUiResult queryPolicy(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "20") Integer limit) {
        return this.policyservice.query(page,limit);
    }

    @RequestMapping("search")
    public LayUiResult search(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "20") Integer limit,
                              @RequestParam String keywords) {
        return this.policyservice.search(page,limit,keywords);
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
