package com.zcf.words.common.wxconfig;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class WXUtil {
	/**
	 * 生成随机字符串
	 * 
	 * @return
	 */
	public static String getNonceStr() {
		Random random = new Random();
		return Md5Util.MD5Encode(String.valueOf(random.nextInt(10000)), "utf8");
	}

	/**
	 * 获取时间戳
	 * 
	 * @return
	 */
	public static String getTimeStamp() {
		return String.valueOf(System.currentTimeMillis() / 1000);
	}

	/**
	 * 获取时间戳
	 *
	 * @return
	 */
	public static String getDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		Date date = new Date();
		try {
			String retdate = sdf.format(date);
			return retdate;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String getMoney(String money) {
		if (money.indexOf(".") != -1) {
			money = money.substring(0, money.indexOf("."));
		}
		return money;
	}

	/**
	 * 元转换成分
	 * @param amount
	 * @return
	 */
	public static String getMoneyF(String amount) {
		if(amount==null){
			return "";
		}
		// 金额转化为分为单位
		// 处理包含, ￥ 或者$的金额
		String currency =  amount.replaceAll("\\$|\\￥|\\,", "");
		int index = currency.indexOf(".");
		int length = currency.length();
		Long amLong = 0l;
		if(index == -1){
			amLong = Long.valueOf(currency+"00");
		}else if(length - index >= 3){
			amLong = Long.valueOf((currency.substring(0, index+3)).replace(".", ""));
		}else if(length - index == 2){
			amLong = Long.valueOf((currency.substring(0, index+2)).replace(".", "")+0);
		}else{
			amLong = Long.valueOf((currency.substring(0, index+1)).replace(".", "")+"00");
		}
		return amLong.toString();
	}
}
