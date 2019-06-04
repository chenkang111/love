package com.zcf.words.common.wxconfig;

public class ConstantUtil {
	/**
	 * 微信开发平台应用ID
	 */
	public static final String APP_ID = "wx15117cdf42f7651c";
	/**
	 * 应用对应的AppSecret
	 */
//	public static final String APP_SECRET = "6a77323e10950b553a5fdb0ac7596ad9";
	public static final String APP_SECRET = "10d6a0077e65d32fbe5a48b0e3c849b5";
	/**
	 * APP_KEY 商户平台---api安全---密钥
	 */
	public static final String APP_KEY = "b0dbd8bced4b887b9aad2462b9f0c5d7";
	/**
	 * 微信支付商户号
	 */
	public static final String MCH_ID = "1520464681";
	/**
	 * 商品描述
	 */
	public static final String BODY = "goodspayment";
	/**
	 * 商户号对应的密钥 也是 商户平台---api安全---密钥 同上面那个APP_KEY
	 */
	public static final String PARTNER_key = "b0dbd8bced4b887b9aad2462b9f0c5d7";

	/**
	 * 商户id 同微信支付商户号
	 */
	public static final String PARTNER_ID = "1520464681";
	/**
	 * 常量固定值
	 */
	public static final String GRANT_TYPE = "client_credential";
	/**
	 * 获取预支付id的接口url
	 */
	public static String GATEURL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
	/**
	 * 微信服务器回调通知url
	 */
	// 测试
	// public static String NOTIFY_URL =
	// "192.168.31.88:8080/works/order/wx/notify_url";
	// 服务器
	// public static String NOTIFY_URL =
	// "http://172.16.1.109/redWars/order/wx/notify_url";
	public static String NOTIFY_URL = "http://47.107.67.75:8080/words/wxpay/wx/notify_url";
	//服务器最新地址
	//public static String NOTIFY_URL = "http://47.107.67.75:8080/words/wxpay/wx/notify_url";
	//public static String NOTIFY_URL = "http://172.16.1.233:8980/words/wxpay/wx/notify_url";
	// public static String NOTIFY_RECHARGE_URL =
	// "http://47.100.110.119:8089/works/order/wx/notify_recharge_url";
}