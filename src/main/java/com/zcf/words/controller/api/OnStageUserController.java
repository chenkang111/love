package com.zcf.words.controller.api;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.zcf.words.common.aliyun.ALiPayConfig;
import com.zcf.words.common.json.Body;
import com.zcf.words.common.utils.IDUtils;
import com.zcf.words.entity.User;
import com.zcf.words.service.UserService;
import com.zcf.words.service.VipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.text.DecimalFormat;
import java.util.Date;

@Controller
@RequestMapping("user")
public class OnStageUserController {

    @Autowired
    private UserService userService;

    @Autowired
    private VipService vipService;

    /**
     * 登录用户
     * phone 手机号
     * password 密码
     * @return
     */
    @RequestMapping(value = "login", method = RequestMethod.POST)
    @ResponseBody
    public Body login(String phone, String code) {
        return userService.login(phone,code);
    }


    /**
     * 第三方登录用户
     * wxid 微信ID
     * qqid QQID
     * @return
     */
    @RequestMapping(value = "thirdlogin", method = RequestMethod.POST)
    @ResponseBody
    public Body thirdlogin(String wxid,String qqid) {
        return userService.thirdlogin(wxid,qqid);
    }


    /**
     * 绑定第三方
     * phone 手机号
     * wxid 微信ID
     * qqid QQID
     * @return
     */
    @RequestMapping(value = "bindthird", method = RequestMethod.POST)
    @ResponseBody
    public Body bindthird(String phone,String wxid,String qqid) {
        return userService.bindthird(phone,wxid,qqid);
    }


    /**
     * 注册用户
     * phone 手机号
     * code 验证码
     * password 密码
     * @return
     */
    @RequestMapping(value = "register", method = RequestMethod.POST)
    @ResponseBody
    public Body register(String phone, String code,String password) {
        return userService.register(phone,code,password);
    }

    /**
     * 发送验证码
     * phone 手机号
     * @return
     */
    @RequestMapping(value = "sendcode", method = RequestMethod.POST)
    @ResponseBody
    public Body sendcode(String phone) {
        return userService.sendcode(phone);
    }

    /**
     *  获取个人信息
     * userid 用户ID
     * @return
     */
    @RequestMapping(value = "userInfo", method = RequestMethod.POST)
    @ResponseBody
    public Body userInfo(String userid) {
        return userService.userInfo(userid);
    }

    /**
     *  获取会员信息
     * userid 用户ID
     * @return
     */
    @RequestMapping(value = "vips", method = RequestMethod.POST)
    @ResponseBody
    public Body vips() {
        return vipService.getVip();
    }

    /**
     *  修改个人信息
     * userid 用户ID
     * nickname 昵称
     * @return
     */
    @RequestMapping(value = "updt_user", method = RequestMethod.POST)
    @ResponseBody
    public Body updateUser(String userid,String nickname) {
        return userService.updateUser(userid,nickname);
    }


    /**
     *  修改绑定手机号
     *  userid 用户ID
     *  oldphone 旧手机号
     *  oldcode 旧验证码
     *  newphone 新手机号
     *  newcode 新验证码
     * @return
     */
    @RequestMapping(value = "updt_phone", method = RequestMethod.POST)
    @ResponseBody
    public Body updatePhone(String userid,String oldphone,String newphone,String newcode) {
        return userService.updatePhone(userid,oldphone,newphone,newcode);
    }

    /**
     *  绑定手机号
     *  userid 用户ID
     *  phone 手机号
     *  code 验证码
     * @return
     */
    @RequestMapping(value = "bind_phone", method = RequestMethod.POST)
    @ResponseBody
    public Body bindPhone(String userid,String phone,String code) {
        return userService.bindPhone(userid,phone,code);
    }

    /**
     *  忘记密码
     * phone 手机号
     * password 密码
     * code 验证码
     * @return
     */
    @RequestMapping(value = "forget_pass", method = RequestMethod.POST)
    @ResponseBody
    public Body forgetPassword(String phone, String password, String code) {
        return userService.forgetPassword(phone,password,code);
    }

    /**
     *  修改密码
     * phone 手机号
     * code 验证码
     * oldpassword 旧密码
     * newpassword 新密码
     * @return
     */
    @RequestMapping(value = "re_pass", method = RequestMethod.POST)
    @ResponseBody
    public Body rePassword(String phone, String code, String oldpassword,String newpassword) {
        return userService.rePassword(phone,code,oldpassword,newpassword);
    }

    /**
     * 充值会员
     */
   @RequestMapping(value = "charge_money", method = RequestMethod.POST)
    @ResponseBody
   public Body chargeMoney(String userid,Integer level, Double balance, String paytype, HttpSession session, HttpServletRequest request, HttpServletResponse response) {
       return userService.chargeMoney(userid,level,balance,paytype,session,request,response);
    }



}
