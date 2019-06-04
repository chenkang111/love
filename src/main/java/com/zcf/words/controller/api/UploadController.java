package com.zcf.words.controller.api;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import com.zcf.words.common.json.Body;
import com.zcf.words.common.utils.FileUploadUtils;
import com.zcf.words.entity.Chat;
import com.zcf.words.mapper.ChatMapper;
import com.zcf.words.service.ChatService;
import com.zcf.words.service.UserService;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("upload")
public class UploadController {
	
	@Autowired
	private UserService userservice;


	@Autowired
	private ChatService chatservice;

	/**
	 * 头像上传
	 * file 图片文件
	 * @return
	 */
	@RequestMapping(value = "head", method = RequestMethod.POST)
	@ResponseBody
	public Body uploadHead(@RequestParam("file") MultipartFile file, String userid) {
		String pic_url = "";
		try {
			// 图片上传的绝对路径（因为相对路径放到页面上就可以显示图片）
			//String path = "c:/home/upload/";
			String path="/home/upload/";
			// 上传图片
			pic_url = FileUploadUtils.fileUpload(file,path,"userfiles/fileupload/");
			return userservice.uploadHeadPic(pic_url,userid);
		} catch (Exception e) {
			e.printStackTrace();
			return Body.newInstance(451, "上传头像失败,参数异常");
		}
	}



	/**
	 * 上传图片
	 * file 图片文件
	 * @return
	 */
	@RequestMapping(value = "pic", method = RequestMethod.POST)
	@ResponseBody
	public Body uploadPic(@RequestParam("file") MultipartFile file) {
		String pic_url = "";
		try {
			// 图片上传的绝对路径（因为相对路径放到页面上就可以显示图片）
			//String path = "c://upload/";
			String path="/home/upload/";
			// 上传图片
			pic_url = FileUploadUtils.fileUpload(file,path,"userfiles/fileupload/");

			return Body.newInstance(pic_url);
		} catch (Exception e) {
			e.printStackTrace();
			return Body.newInstance(451, "上传图片失败,参数异常");
		}
	}

	/**
	 * 多图上传
	 * file 文件
	 * @return
	 */
	@RequestMapping(value = "listpic", method = RequestMethod.POST)
	@ResponseBody
	public Body uploadListPic(@RequestParam("files") List<MultipartFile> files, HttpServletRequest request) {
		String[] wk_url = new String[10];
		try {
			for (int i = 0; i < files.size(); i++) {
				if(files.get(i)!=null) {
					// 图片上传的绝对路径（因为相对路径放到页面上就可以显示图片）
					String path = "c://upload/";
					// 上传图片
					wk_url[i] = FileUploadUtils.fileUpload(files.get(i),path,"userfiles/fileupload/");
				}
			}
			return Body.newInstance(wk_url);
		} catch (Exception e) {
			return Body.newInstance(451, "上传图片文件失败,参数异常");
		}
	}

	/**
	 * 发送图片
	 * file 图片文件
	 * @return
	 */
	@RequestMapping(value = "message", method = RequestMethod.POST)
	@ResponseBody
	public Body uploadMessagePic(@RequestParam("file") MultipartFile file, String userid) {
		String pic_url = "";
		try {
			// 图片上传的绝对路径（因为相对路径放到页面上就可以显示图片）
			//String path = "c://upload/";
			String path = "c://upload/";
			// 上传图片
			pic_url = FileUploadUtils.fileUpload(file,path,"userfiles/fileupload/");

			//chatservice.insertChat(userid,pic_url);

			return Body.newInstance(pic_url);
		} catch (Exception e) {
			e.printStackTrace();
			return Body.newInstance(451, "发送图片失败,参数异常");
		}
	}

}
