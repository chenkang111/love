package com.zcf.words.controller.api;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.zcf.words.common.aliyun.ALiPayConfig;
import com.zcf.words.entity.Bill;
import com.zcf.words.entity.User;
import com.zcf.words.entity.Vip;
import com.zcf.words.mapper.BillMapper;
import com.zcf.words.mapper.UserMapper;
import com.zcf.words.mapper.VipMapper;
import com.zcf.words.service.BillService;
import com.zcf.words.service.UserService;
import com.zcf.words.service.VipService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;

import static com.zcf.words.common.aliyun.transfer.alipayclient;


@Controller
@RequestMapping(value = "alipay")
public class ALiPayController{

	private static final Logger logger = LoggerFactory.getLogger(ALiPayController.class);

	@Autowired
	private UserService userService;

	@Autowired
	private BillService billService;

	@Autowired
	private VipService vipService;


	public void doRecharge(HttpServletRequest request, HttpServletResponse response) throws IOException {
		try {
			// 获取支付宝POST过来反馈信息
			request.setCharacterEncoding("UTF-8");
			Map<String, String> params = new HashMap<String, String>();
			Map requestParams = request.getParameterMap();
			for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
				String name = (String) iter.next();
				String[] values = (String[]) requestParams.get(name);
				String valueStr = "";
				for (int i = 0; i < values.length; i++) {
					valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
				}
				// 乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
				//valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
				params.put(name, valueStr.trim());
			}
			boolean signVerified = true;

			// 商户订单号
			String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"), "UTF-8");
			// 支付宝交易号
			String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"), "UTF-8");
			// 交易状态
			String trade_status = new String(request.getParameter("trade_status").getBytes("ISO-8859-1"), "UTF-8");
			// 支付金额
			String totalFee = new String(request.getParameter("buyer_pay_amount").getBytes("ISO-8859-1"), "UTF-8");
			// 买家账号
			String buyerEmail = new String(request.getParameter("buyer_logon_id").getBytes("ISO-8859-1"), "UTF-8");

			String passbackparams = new String(request.getParameter("passback_params").getBytes("ISO-8859-1"), "UTF-8"); //	公用回传参数

			System.out.println(">>>>>>>>>>>>>>>商户订单号:" + out_trade_no);
			System.out.println(">>>>>>>>>>>>>>>>>支付宝交易号" + trade_no);
			System.out.println(">>>>>>>>>>>>>>>>>>>>交易状态" + trade_status);
			System.out.println(">>>>>>>>>>>>>>>>>>>>>>>支付金额" + totalFee);
			System.out.println(">>>>>>>>>>>>>>>>>>>>买家账号" + buyerEmail);
			System.out.println(">>>>>>>>>>>>>>>>>>>>用户ID" + passbackparams);
			try {
				// 验证签名
				signVerified = AlipaySignature.rsaCheckV1(params, ALiPayConfig.ali_public_key, ALiPayConfig.input_charset,"RSA2");
				if (signVerified) { // 签名验证成功
					if (trade_status.equals("TRADE_FINISHED") || trade_status.equals("TRADE_SUCCESS")) {
						String userId = passbackparams.split(",")[0];
						Integer m = Integer.parseInt(totalFee);
						//获取VIP信息
						Vip vip = vipService.selectbyid(passbackparams.split(",")[1]);
						//用户充值会员
						User user = userService.setUserVip(userId,vip.getVip_id(),vip.getVip_day());
						//添加账单
						Bill bill = billService.crtBill(user,vip,"1",totalFee,out_trade_no);
						if(bill!=null){
							params.remove("sign_type");
							logger.debug("支付成功");
						}
					} else {
						logger.debug("支付失败");
					}
				} else {
					logger.debug("签名验证失败！");
					response.getWriter().print("fail");
				}
			} catch (AlipayApiException e1) {
				logger.debug("支付失败,支付异常");
				response.getWriter().print("fail");
			}
		} catch (UnsupportedEncodingException e) {
			logger.debug("字符串编译异常");
			response.getWriter().print("fail");
		}
	}


	// 充值
	@RequestMapping(value = "doRecharge", method = RequestMethod.POST)
	@ResponseBody
	public String rechargeMemberNotify(HttpServletRequest request, HttpServletResponse response) {
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>进入商品付款回调方法");
		try {
			Map<String, String> params=new HashMap<String, String>();
			Map requestParams=request.getParameterMap();//从响应的请求里面获取结果
			for (Iterator iter=requestParams.keySet().iterator(); iter.hasNext(); ) {
				String name=(String) iter.next(); //键
				String[] values=(String[]) requestParams.get(name);//值，
				String valueStr="";
				for (int i=0; i < values.length; i++) {
					valueStr=(i == values.length-1) ? valueStr+values[i] : valueStr+values[i]+",";
				}
				// 乱码解决，这段代码在出现乱码时使用。
				// valueStr=new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
				params.put(name, valueStr.trim());//将支付宝的返回值处理之后放到自己的map集合里面。
			}
			try {
				//ali_public_key，这里是支付宝公钥，不是应用公钥
				boolean flag=AlipaySignature.rsaCheckV1(params, ALiPayConfig.ali_public_key, ALiPayConfig.input_charset,"RSA2");
				// 商户订单号
				String out_trade_no=new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"), "UTF-8");
				// 支付宝交易号
				String trade_no=new String(request.getParameter("trade_no").getBytes("ISO-8859-1"), "UTF-8");
				// 交易状态
				String trade_status=new String(request.getParameter("trade_status").getBytes("ISO-8859-1"), "UTF-8");
				// 支付金额
				String totalFee=new String(request.getParameter("buyer_pay_amount").getBytes("ISO-8859-1"), "UTF-8");
				// 买家账号
				String buyerEmail=new String(request.getParameter("buyer_logon_id").getBytes("ISO-8859-1"), "UTF-8");

				String passbackPrams=new String(request.getParameter("passback_params").getBytes("ISO-8859-1"), "UTF-8");//附加参数
				System.out.println(">>>>>>>>>>>>>>>>>>>>商户订单号:"+out_trade_no);
				System.out.println(">>>>>>>>>>>>>>>>>>>>支付宝交易号"+trade_no);
				System.out.println(">>>>>>>>>>>>>>>>>>>>交易状态"+trade_status);
				System.out.println(">>>>>>>>>>>>>>>>>>>>支付金额"+totalFee);
				System.out.println(">>>>>>>>>>>>>>>>>>>>买家账号"+buyerEmail);
				if (flag) {// 验证
					//对交易状态进行验证
					if (trade_status.equals("TRADE_FINISHED") || trade_status.equals("TRADE_SUCCESS")) {
						System.out.println("success");
						// 支付成功后的逻辑
						System.out.println(billService==null);
						Bill b =billService.getByBillNo(out_trade_no);
						b.setBill_status("1");
						if(billService.update(b)){
							//更新用户的信息
							Vip  vip  =vipService.selectbyid(passbackPrams.split(",")[1]);
							User  user =userService.setUserVip(passbackPrams.split(",")[0],vip.getVip_id(),vip.getVip_day());
							if(user!=null){
								return "success";
							}
						}
					} else {
						System.out.println("fail1");
						return "fail";
					}
				} else {// 验证失败
					System.out.println("fail1");
					return "fail";
				}
			} catch (AlipayApiException e1) {
				System.out.println("fail2");
				return "fail";
			}
		} catch (UnsupportedEncodingException e) {
			System.out.println("fail3");
			return "fail";
		}
		return "fail";
	}



	public static void refundMoney(Long id) throws AlipayApiException{
		DefaultAlipayClient alipayClient = alipayclient();
		AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
		request.setBizContent("{" +
				"\"out_trade_no\":\""+id+"\"," +
				"\"refund_amount\":\""+0.01+"\"," +
				"\"refund_reason\":\"正常退款\"" +
				"  }");
		AlipayTradeRefundResponse response = alipayClient.execute(request);
		if(response.isSuccess()){
			System.out.println("退款成功");
		} else {
			System.out.println("退款失败");
		}
	}

}