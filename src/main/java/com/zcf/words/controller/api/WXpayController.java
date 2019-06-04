package com.zcf.words.controller.api;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.zcf.words.common.wxconfig.*;
import com.zcf.words.entity.Bill;
import com.zcf.words.entity.User;
import com.zcf.words.entity.Vip;
import com.zcf.words.mapper.BillMapper;
import com.zcf.words.service.BillService;
import com.zcf.words.service.UserService;
import com.zcf.words.service.VipService;
import org.jdom2.JDOMException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/wxpay")
public class WXpayController {

	@Autowired
	private UserService userService;

	@Autowired
	private BillService billService;

	@Autowired
	private VipService vipService;

	private static final Logger logger = LoggerFactory.getLogger(WXpayController.class);

	/**
	 * 初始化微信支付
	 *
	 * // * @param request // * @param response // * @param orderId // * @return
	 */
	// @RequestMapping("/pay/wx")
	// @ResponseBody
	public WxNotifyParam initWx(HttpServletRequest request, HttpServletResponse response,String userId, Double balance,Integer level,String OutTradeNo) {
		// Map<String, Object> map = new HashMap<String, Object>();
		// 获取生成预支付订单的请求类
		PrepayIdRequestHandler prepayReqHandler = new PrepayIdRequestHandler(request, response);
		String totalFee = WXUtil.getMoneyF(balance.toString());// 微信支付是
		// 交易时间 生成订单开始

		// 上线后，将此代码放开
		prepayReqHandler.setParameter("appid", ConstantUtil.APP_ID);
		prepayReqHandler.setParameter("mch_id", ConstantUtil.MCH_ID);

		String nonce_str = WXUtil.getNonceStr();
		prepayReqHandler.setParameter("nonce_str", nonce_str);

		prepayReqHandler.setParameter("body", ConstantUtil.BODY);
		//微信附加参数
		prepayReqHandler.setParameter("attach", userId+","+level);

		// String out_trade_no = userOrder.getOrderNo();
		String out_trade_no = OutTradeNo + "";
		prepayReqHandler.setParameter("out_trade_no", out_trade_no);
		// logger.info("金额" + totalFee);
		prepayReqHandler.setParameter("total_fee", totalFee);
		// request.getRemoteAddr()
		// "39.98.219.33"
		//prepayReqHandler.setParameter("spbill_create_ip", "172.16.1.233");
		prepayReqHandler.setParameter("spbill_create_ip", "47.107.67.75");
		String timestamp = WXUtil.getTimeStamp();
		prepayReqHandler.setParameter("time_start", timestamp);
		prepayReqHandler.setParameter("notify_url", ConstantUtil.NOTIFY_URL);
		prepayReqHandler.setParameter("trade_type", "APP");
		/**
		 * 注意签名（sign）的生成方式，具体见官方文档（传参都要参与生成签名，且参数名按照字典序排序，最后接上APP_KEY,转化成大写）
		 */
		prepayReqHandler.setParameter("sign", prepayReqHandler.createMD5Sign());
		prepayReqHandler.setGateUrl(ConstantUtil.GATEURL);
		String prepayid;
		WxNotifyParam param = new WxNotifyParam();
		try {
			prepayid = prepayReqHandler.sendPrepay();
			// 若获取prepayid成功，将相关信息返回客户端
			if (prepayid != null && !prepayid.equals("")) {
				String signs = "appid=" + ConstantUtil.APP_ID + "&noncestr=" + nonce_str
						+ "&package=Sign=WXPay&partnerid=" + ConstantUtil.PARTNER_ID + "&prepayid=" + prepayid
						+ "&timestamp=" + timestamp + "&key=" + ConstantUtil.APP_KEY;
				/**
				 * 签名方式与上面类似
				 */
				param.setPrepayId(prepayid);
				param.setSign(Md5Util.MD5Encode(signs, "utf8").toUpperCase());
				param.setAppId(ConstantUtil.APP_ID);
				// 等于请求prepayId时的time_start
				param.setTimeStamp(timestamp);
				// 与请求prepayId时值一致
				param.setNonceStr(nonce_str);
				// 固定常量
				param.setPackages("Sign=WXPay");
				param.setPartnerId(ConstantUtil.PARTNER_ID);
				//  执行增加订单
                return param;
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 接收微信支付回调通知
	 *
	 * @param request
	 * @throws Exception
	 */
	@RequestMapping("wx/notify_url")
	@ResponseBody
	public String getTenPayNotify(HttpServletRequest request) throws Exception {
		logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>进入微信回调方法");
		InputStream inStream = request.getInputStream();
		ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = inStream.read(buffer)) != -1) {
			outSteam.write(buffer, 0, len);
		}
		outSteam.close();
		inStream.close();
		String result = new String(outSteam.toByteArray(), "utf-8");
		logger.info("微信回调支付通知结果：" + result);
		Map<String, String> map = null;
		try {
			/**
			 * 解析微信通知返回的信息
			 */
			map = XMLUtil.doXMLParse(result);
		} catch (JDOMException e) {
			e.printStackTrace();
		}
		logger.info("- - - - - - - - -");
		logger.info("= = = = = = = = =:" + map);
		// 若充值成功，则告知微信服务器收到通知
		String tradeNo = map.get("out_trade_no"); //订单号
		Bill b = billService.getByBillNo(tradeNo);
		if (map.get("return_code").equals("SUCCESS")) {
			if (map.get("result_code").equals("SUCCESS")) {
				logger.info("微信充值会员体系成功！");
				b.setBill_status("1");
				if (billService.update(b)) {
					//更新用户信息
					String  attach=map.get("attach");
					Vip  vip  =vipService.selectbyid(attach.split(",")[1]);
					User  user =userService.setUserVip(attach.split(",")[0],vip.getVip_id(),vip.getVip_day());
					if(user!=null){
						return XMLUtil.setXML("SUCCESS", "");
					}
					return XMLUtil.setXML("FAIL", "");
				}
			} else {
				logger.error("微信支付回调失败");
				return XMLUtil.setXML("FAIL", "");
			}
		} else {
			logger.error("微信支付回调失败");
			b.setBill_status("2");
			billService.update(b);
			return XMLUtil.setXML("FAIL", "");
		}
		logger.error("微信支付回调失败");
		b.setBill_status("2");
		billService.update(b);
		return XMLUtil.setXML("FAIL", "");
	}



//	public static void main(String args[]) {
////		int a=1;
////		System.out.println(a++);
////		int b=1;
////		System.out.println(b+=1);
//		String notifyStr = XMLUtil.setXML("SUCCESS", "");
//		System.out.println(notifyStr);
//	}

}
