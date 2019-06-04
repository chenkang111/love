package com.zcf.words.controller.console;

import com.zcf.words.common.json.LayUiResult;
import com.zcf.words.common.utils.FileUploadUtils;
import com.zcf.words.entity.Handbook;
import com.zcf.words.service.HandbookService;
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
@RequestMapping("s/handbook")
public class HandbookController {

    @Autowired
    private HandbookService handbookservice;

    @RequestMapping("add")
    public boolean add(@RequestBody Handbook handbook) {
        return this.handbookservice.add(handbook);
    }

    @RequestMapping("delete")
    public boolean delete(@RequestParam Integer id) {
        return this.handbookservice.delete(id);
    }

    @RequestMapping("update")
    public boolean update(@RequestBody Handbook handbook) {
        return this.handbookservice.update(handbook);
    }

    @RequestMapping("query")
    public LayUiResult queryHandbook(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "20") Integer limit) {
        return this.handbookservice.query(page,limit);
    }

    @RequestMapping("search")
    public LayUiResult search(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "20") Integer limit,
        @RequestParam String keywords) {
        return this.handbookservice.search(page,limit,keywords);
    }

    @RequestMapping("upload")
    public Map UploadBrand(@RequestParam("file") MultipartFile file, HttpServletRequest request){
        String  pathVal = "c://upload/";
        return FileUploadUtils.uploadLayUiImg(file, pathVal,"userfiles/fileupload/");

    }
}
