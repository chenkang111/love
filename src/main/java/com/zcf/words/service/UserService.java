package com.zcf.words.service;


import com.alibaba.druid.util.StringUtils;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zcf.words.common.aliyun.ALiPayConfig;
import com.zcf.words.common.aliyun.sendsms;
import com.zcf.words.common.json.Body;
import com.zcf.words.common.json.LayUiResult;
import com.zcf.words.common.utils.IDUtils;
import com.zcf.words.common.wxconfig.WxNotifyParam;
import com.zcf.words.controller.api.WXpayController;
import com.zcf.words.entity.Bill;
import com.zcf.words.entity.Code;
import com.zcf.words.entity.User;
import com.zcf.words.entity.Vip;
import com.zcf.words.mapper.BillMapper;
import com.zcf.words.mapper.CodeMapper;
import com.zcf.words.mapper.UserMapper;
import com.zcf.words.mapper.VipMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CodeMapper codeMapper;

    @Autowired
    private VipMapper vipMapper;

    @Autowired
    private BillMapper  billMapper;
    /**
     * 登录
     *
     * @param phone
     * @param code
     * @return
     */
    public Body login(String phone, String code) {
        Code codeinfo = new Code();
        codeinfo.setCode(code);
        codeinfo.setPhone(phone);
        List<Code> list = codeMapper.select(codeinfo);
        if (list != null && list.size() > 0) {
            codeMapper.delete(codeinfo);
            User user = new User();
            user.setUser_phone(phone);
            List<User> userlist = userMapper.select(user);
            if (userlist == null || userlist.size() <= 0) {
                User newuser = new User();
                newuser.setUser_phone(phone);
                newuser.setUser_vip("0");//免费用户
                newuser.setUser_nickname("恋爱游客" + phone.substring(0, 3) + "****" + phone.substring(7, phone.length()));
                int count = userMapper.insertSelective(newuser);
                if (count > 0) {
                    List<User> users = userMapper.select(newuser);
                    logger.info("= = = 》 {}新用户登录成功", newuser.getUser_nickname());
                    return Body.newInstance(users.get(0));
                } else {
                    return Body.newInstance(500, "注册用户失败");
                }
            } else {
                logger.info("= = = 》 {}用户登录成功", user.getUser_nickname());
                return Body.newInstance(userlist.get(0));
            }
        } else {
            return Body.newInstance(451, "验证码不正确");
        }
    }

    /**
     * 第三方登录
     *
     * @param wxid
     * @param qqid
     * @return
     */
    public Body thirdlogin(String wxid, String qqid) {
        User user = new User();
        Example exp = new Example(User.class);
        if (StringUtils.isEmpty(wxid) && StringUtils.isEmpty(qqid)) {
            return Body.newInstance(451, "第三方登录失败,参数异常");
        }
        if (!StringUtils.isEmpty(wxid)) {
            user.setUser_wx_id(wxid);
            exp.createCriteria().andEqualTo("user_wx_id", wxid);
        }
        if (!StringUtils.isEmpty(qqid)) {
            user.setUser_qq_id(qqid);
            exp.createCriteria().andEqualTo("user_qq_id", qqid);
        }
        List<User> list = userMapper.selectByExample(exp);
        if (list == null || list.size() <= 0) {
            //没有账号直接第三方登录
            user.setUser_vip("0");//免费用户
            user.setUser_nickname("恋爱游客");
            userMapper.insertSelective(user);
            List<User> list2 = userMapper.selectByExample(exp);
            if (list2 != null && list2.size() > 0) {
                logger.info("= = = 》 {}第三方用户登录成功", list2.get(0).getUser_nickname());
                return Body.newInstance(list2.get(0));
            }else {
                return Body.newInstance(404, "登录失败,没有找到该用户");
            }
        }else{
            logger.info("= = = 》 {}第三方用户登录成功", user.getUser_nickname());
            return Body.newInstance(list.get(0));
        }
    }

    /**
     * 绑定第三方
     *
     * @param wxid
     * @param qqid
     * @return
     */
    public Body bindthird(String phone, String wxid, String qqid) {
        User user = new User();
        if (StringUtils.isEmpty(wxid) || StringUtils.isEmpty(qqid) || StringUtils.isEmpty(phone)) {
            return Body.newInstance(451, "第三方绑定失败,参数异常");
        }

        user.setUser_phone(phone);
        List<User> list = userMapper.select(user);

        if (list == null || list.size() <= 0) {
            return Body.newInstance(542, "用户不存在");
        } else {
            Example exp = new Example(User.class);
            if (!StringUtils.isEmpty(wxid)) {
                exp.createCriteria().andEqualTo("user_wx_id", wxid);
            }
            if (!StringUtils.isEmpty(qqid)) {
                exp.createCriteria().andEqualTo("user_qq_id", qqid);
            }
            List<User> userlist = userMapper.selectByExample(exp);
            if (userlist == null || userlist.size() <= 0) {
                user.setUser_wx_id(wxid);
                user.setUser_qq_id(qqid);
                int count = userMapper.updateByPrimaryKeySelective(user);
                if (count > 0) {
                    logger.info("= = = 》 {}第三方用户绑定成功", user.getUser_nickname());
                    return Body.newInstance(user);
                } else {
                    return Body.newInstance(500, "第三方用户绑定失败");
                }
            } else {
                return Body.newInstance(542, "第三方账号已被绑定过！");
            }
        }
    }

    /**
     * 注册
     *
     * @param phone
     * @param code
     * @param password
     * @return
     */
    public Body register(String phone, String code, String password) {
        User user = new User();
        if (StringUtils.isEmpty(phone) || StringUtils.isEmpty(code) || StringUtils.isEmpty(password)) {
            return Body.newInstance(451, "注册用户失败,参数异常");
        }
        Code codeinfo = new Code();
        codeinfo.setCode(code);
        codeinfo.setPhone(phone);
        List<Code> list = codeMapper.select(codeinfo);
        if (list != null && list.size() > 0) {
            user.setUser_phone(phone);
            user.setUser_password(password);
            user.setUser_vip("0");//免费用户
            user.setUser_nickname("恋爱游客" + phone.substring(0, 3) + "****" + phone.substring(7, phone.length()));
            user.setUser_realpassword(password);
            int count = userMapper.insertSelective(user);
            if (count > 0) {
                logger.info("= = = 》 {}用户注册成功", user.getUser_nickname());
                return Body.newInstance(user);
            } else {
                return Body.newInstance(500, "注册用户失败");
            }
        } else {
            return Body.newInstance(451, "验证码不正确");
        }
    }


    /**
     * 发送验证码
     *
     * @param phone
     * @return
     */
    public Body sendcode(String phone) {
        String random = IDUtils.Random();
        try {
            //SmsUtils.sendSms(phone, random);
            sendsms.sendMsg(phone, random);
        } catch (Exception e) {
            e.printStackTrace();
            return Body.newInstance(452, "短信接口服务异常，请联系管理员");
        }
        logger.info("====>验证码:{} 发送成功", random);
        Code co = new Code();
        co.setPhone(phone);
        codeMapper.delete(co);
        co.setCode(random);
        co.setCreate_time(new Date());
        int count = codeMapper.insert(co);
        if (count > 0) {
            logger.info("====>验证码:{} 插入数据成功", random);
            logger.info("= = = 》 {}发送验证码成功", co.getCreate_time());
            return Body.newInstance(co);
        } else {
            return Body.newInstance(500, "插入数据失败");
        }
    }


    /**
     * 获取个人信息
     *
     * @param userid
     * @return
     */
    public Body userInfo(String userid) {
        User user = new User();
        if(userid==null||userid.length()<=0){
            return Body.newInstance(471, "参数错误");
        }
        user = userMapper.selectByPrimaryKey(Integer.parseInt(userid));
        if (user == null) {
            return Body.newInstance(542, "用户不存在");
        }
        logger.info("= = = 》 {}查询用户成功", user.getUser_nickname());
        return Body.newInstance(user);
    }


    /**
     * 上传头像
     *
     * @param pic_url
     * @param userId
     * @return
     */
    public Body uploadHeadPic(String pic_url, String userId) {
        if (StringUtils.isEmpty(pic_url) || StringUtils.isEmpty(userId)) {
            return Body.newInstance(451, "上传头像失败,参数异常");
        }
        User user = userMapper.selectByPrimaryKey(Integer.parseInt(userId));
        user.setUser_headpic(pic_url);
        int count = userMapper.updateByPrimaryKeySelective(user);
        if (count > 0) {
            return Body.newInstance(pic_url);
        } else {
            return Body.newInstance(500, "上传失败");
        }
    }

    /**
     * 修改个人信息
     *
     * @param userid
     * @return
     */
    public Body updateUser(String userid, String nickname) {
        if (StringUtils.isEmpty(userid)) {
            return Body.newInstance(451, "修改个人信息失败,参数异常");
        }
        User user = new User();
        user.setUser_id(Integer.parseInt(userid));
        if (!StringUtils.isEmpty(nickname)) {
            user.setUser_nickname(nickname);
        }
        int count = userMapper.updateByPrimaryKeySelective(user);
        if (count > 0) {
            return Body.newInstance(200, "修改个人信息成功");
        } else {
            return Body.newInstance(500, "修改个人信息失败");
        }
    }

    /**
     * 修改绑定手机号
     * userid 用户ID
     * oldphone 旧手机号
     * oldcode 旧验证码
     * newphone 新手机号
     * newcode 新验证码
     *
     * @return
     */
    public Body updatePhone(String userid, String oldphone, String newphone, String newcode) {
        if (StringUtils.isEmpty(userid) || StringUtils.isEmpty(oldphone) || StringUtils.isEmpty(newphone) || StringUtils.isEmpty(newcode)) {
            return Body.BODY_451;
        }
        User user =userMapper.selectByPrimaryKey(userid);
        if(user.getUser_phone().equals(oldphone)){
            Code code = new Code();
            code.setCode(newcode);
            code.setPhone(newphone);
            if( codeMapper.selectCount(code)>0){
                user.setUser_phone(newphone);
                return userMapper.updateByPrimaryKey(user)>0 ?Body.BODY_200:Body.newInstance(201,"失败");
            }
            return Body.newInstance(201,"验证码输入有误");
        }
        return Body.newInstance(201,"原手机号输入有误");
    }

    /**
     * 绑定手机号
     * userid 用户ID
     * phone 手机号
     * code 验证码
     *
     * @return
     */
    public Body bindPhone(String userid, String phone, String code) {
        if (StringUtils.isEmpty(userid) || StringUtils.isEmpty(phone) || StringUtils.isEmpty(code)) {
            return Body.newInstance(451, "绑定手机号失败,参数异常");
        }
        Code codes = new Code();
        codes.setCode(code);
        codes.setPhone(phone);
        List<Code> list = codeMapper.select(codes);
        User user =  userMapper.selectByPrimaryKey(Integer.parseInt(userid));
        if (list == null || list.size() <= 0) {
            return Body.newInstance(451, "验证码不匹配！");
        } else {
            user.setUser_phone(phone);
            userMapper.updateByPrimaryKeySelective(user);
            return Body.newInstance(200, "绑定手机成功");
        }
    }


    /**
     * 忘记密码
     * phone 手机号
     * password 密码
     * code 验证码
     *
     * @return
     */
    public Body forgetPassword(String phone, String password, String code) {
        if (StringUtils.isEmpty(phone) || StringUtils.isEmpty(password) || StringUtils.isEmpty(code)) {
            return Body.newInstance(451, "忘记密码操作失败,参数异常");
        }
        Code codes = new Code();
        codes.setCode(code);
        codes.setPhone(phone);
        List<Code> codelist = codeMapper.select(codes);
        if (codelist == null || codelist.size() <= 0) {
            return Body.newInstance(451, "验证码不匹配！");
        } else {
            codeMapper.delete(codes);
            User user = new User();
            List<User> list = new ArrayList<User>();
            user.setUser_phone(phone);
            list = userMapper.select(user);
            if (list != null && list.size() > 0) {
                user = list.get(0);
                user.setUser_password(password);
                user.setUser_realpassword(password);
                Integer count = userMapper.updateByPrimaryKeySelective(user);
                if (count > 0) {
                    return Body.newInstance(200, "重置密码成功,请重新登录");
                } else {
                    return Body.newInstance(500, "重置密码失败");
                }
            } else {
                return Body.newInstance(404, "该用户不存在,重置密码失败");
            }
        }
    }

    /**
     * 修改密码
     * phone 手机号
     * code 验证码
     * oldpassword 旧密码
     * newpassword 新密码
     *
     * @return
     */
    public Body rePassword(String phone, String code, String oldpassword, String newpassword) {
        if (StringUtils.isEmpty(phone) || StringUtils.isEmpty(code)
                || StringUtils.isEmpty(oldpassword) || StringUtils.isEmpty(newpassword)) {
            return Body.newInstance(451, "修改密码失败,参数异常");
        }
        Code codes = new Code();
        codes.setCode(code);
        codes.setPhone(phone);
        List<Code> codelist = codeMapper.select(codes);
        if (codelist == null || codelist.size() <= 0) {
            return Body.newInstance(451, "验证码不匹配！");
        } else {
            User user = new User();
            user.setUser_phone(phone);
            user = userMapper.selectOne(user);
            if (user != null) {
                if (user.getUser_password().equals(oldpassword)) {
                    user.setUser_password(newpassword);
                    user.setUser_realpassword(newpassword);
                    userMapper.updateByPrimaryKeySelective(user);
                    return Body.newInstance(200, "修改密码成功");
                } else {
                    return Body.newInstance(451, "原密码错误,修改密码失败");
                }
            } else {
                return Body.newInstance(404, "该用户不存在,重置密码失败");
            }
        }
    }

    /**
     * 充值会员
     *
     * @return
     */
    public Body chargeMoney(String userId,Integer level, Double balance, String paytype, HttpSession session, HttpServletRequest request, HttpServletResponse response) {
        try {
            if (paytype != null && paytype.trim().length() > 0) {
                User user = new User();
                if ("1".equals(paytype)) {
                    //微信充值
                    user = userMapper.selectByPrimaryKey(Integer.parseInt(userId));
                    if (user != null && user.getUser_id() != null && user.getUser_id().toString().trim().length() > 0) {
                        WXpayController wxpay = new WXpayController();
                        //充值
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
                        String time = sdf.format(new Date());
                        Vip vip = vipMapper.selectByPrimaryKey(level.toString());
                        Bill b = new Bill();
                        b.setBill_status("0");
                        b.setMark(time);
                        b.setBill_name(vip.getVip_name());
                        b.setBill_user_id(Integer.parseInt(userId));
                        b.setBill_paytype("1");
                        b.setBill_price(balance.toString());
                        b.setBill_service(vip.getVip_content());
                        b.setCreate_time(new Date());
                        if (billMapper.insert(b) > 0) {
                            WxNotifyParam param = wxpay.initWx(request, response, userId, balance, level, time);
                            if (param != null) {
                                return Body.newInstance(param);
                            }
                        }
                        return Body.newInstance(404, "充值失败");
                    } else {
                        return Body.newInstance(471, "充值失败,用户没有查询到！");
                    }
                } else if ("2".equals(paytype)) {
                    //支付宝充值
                    user = userMapper.selectByPrimaryKey(Integer.parseInt(userId));
                    return goRecharge(balance, user.getUser_id(), level);
                }
            }else{
                  return Body.newInstance(471, "充值失败,无法确认支付类型！");
            }
        } catch (Exception e) {
            return Body.newInstance(471, "充值失败！异常");
        }
        return Body.newInstance(471, "充值失败！异常");
    }





    /**
     * 支付宝充值
     * money 手机号
     * userId 验证码
     *
     * @return
     */
    public Body goRecharge(Double money, Integer userId,Integer level) {
        String OutTradeNo = null;// 订单号
        // 交易时间 生成订单开始
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String time = sdf.format(new Date());
        //OutTradeNo支付宝的
        OutTradeNo = time;
        try {
            DecimalFormat dex = new DecimalFormat("#0.00");
            String payAccount = String.valueOf(dex.format(money));// 测试使用（可删除）
            // 实例化客户端
            AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do",
                    ALiPayConfig.appid, ALiPayConfig.private_key, "json", ALiPayConfig.input_charset,
                    ALiPayConfig.ali_public_key, ALiPayConfig.pay_sign_type);
            // 实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
            AlipayTradeAppPayRequest request1 = new AlipayTradeAppPayRequest();
            // SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
            AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
            model.setBody("欢迎使用聊天套路APP");
            model.setSubject("使用聊天套路APP充值会员:" + payAccount + "元");
            model.setOutTradeNo(OutTradeNo);//订单ID
            model.setTimeoutExpress("10m");
            model.setTotalAmount(payAccount);
            model.setSellerId(ALiPayConfig.partner);
            model.setProductCode("QUICK_MSECURITY_PAY");
            model.setPassbackParams(userId.toString()+","+level);
            request1.setBizModel(model);
            request1.setNotifyUrl(ALiPayConfig.notify_url_rechage);
            try {
                // 这里和普通的接口调用不同，使用的是sdkExecute
                AlipayTradeAppPayResponse response1 = alipayClient.sdkExecute(request1);
                Vip vip = vipMapper.selectByPrimaryKey(level.toString());
                //用户充值会员
                User user = userMapper.selectByPrimaryKey(userId);
                Bill b= new Bill();
                b.setBill_status("0");
                b.setMark(OutTradeNo);
                b.setBill_name(vip.getVip_name());
                b.setBill_user_id(userId);
                b.setBill_paytype("2");
                b.setBill_price(money.toString());
                b.setBill_service(vip.getVip_content());
                b.setCreate_time(new Date());
                if(billMapper.insert(b)>0){
                    logger.info("-----------》创建微信订单成功: " );
                }
                return Body.newInstance(response1.getBody());
            } catch (AlipayApiException e) {
                e.getMessage();
                return Body.newInstance(471, "充值失败！支付异常！");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return Body.newInstance(471, "充值失败！系统异常！");
        }
    }

    /**
     * 充值会员
     *
     * @return
     */
    public User setUserVip(String userId, Integer vipId, String vipday) {
        User user = userMapper.selectByPrimaryKey(Integer.parseInt(userId));
        user.setUser_vip_time(Integer.parseInt(user.getUser_vip_time())+ Integer.parseInt(vipday)+"");
        if(vipId>=Integer.parseInt(user.getUser_vip())){
            user.setUser_vip(vipId.toString());
        }
        int count = userMapper.updateByPrimaryKeySelective(user);
        if (count > 0) {
            return user;
        } else {
            return null;
        }
    }


    /***************************************后台接口****************************************/


    public boolean add(User user) {
        return this.userMapper.insertSelective(user) == 1;
    }

    public boolean delete(Integer id) {
        return this.userMapper.deleteByPrimaryKey(id) == 1;
    }

    public boolean update(User user) {
        return this.userMapper.updateByPrimaryKeySelective(user) == 1;
    }

    public LayUiResult query(Integer page, Integer limit) {
        PageHelper.startPage(page, limit);
        List<User> list = this.userMapper.selectAll();
        return new LayUiResult("0", "查询成功", new PageInfo<>(list).getTotal(), list);
    }

    public LayUiResult search(Integer page, Integer limit, String keywords) {
        Example example = new Example(User.class);
        keywords = "%" + keywords + "%";
        example.createCriteria().andLike("user_nickname", keywords)
                .orLike("user_phone", keywords);//name为你想要搜索的字段
        PageHelper.startPage(page, limit);
        List<User> list = this.userMapper.selectByExample(example);
        return new LayUiResult("0", "查询成功", new PageInfo<>(list).getTotal(), list);
    }
}
