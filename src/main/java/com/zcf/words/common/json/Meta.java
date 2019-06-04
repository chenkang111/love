package com.zcf.words.common.json;

/**
 * Json响应中的meta头
 *
 * @author six
 * @date 2018/07/16
 */
public class Meta {

	public static final Meta CODE_200 = new Meta(200, "OK");
	public static final Meta CODE_400 = new Meta(400, "Bad Request");
	public static final Meta CODE_401 = new Meta(401, "Unauthorized");
	public static final Meta CODE_404 = new Meta(404, "Not Found");
	public static final Meta CODE_450 = new Meta(450, "参数签名错误");
	public static final Meta CODE_451 = new Meta(451, "参数验证错误");
	public static final Meta CODE_452 = new Meta(452, "请求已过期");
	public static final Meta CODE_460 = new Meta(460, "token验证错误");
	public static final Meta CODE_461 = new Meta(461, "token格式错误");
	public static final Meta CODE_462 = new Meta(462, "token过期");
	public static final Meta CODE_500 = new Meta(500, "Internal Server Error");
	public static final Meta CODE_470 = new Meta(470, "对象不存在");
	public static final Meta CODE_471 = new Meta(471, "充值异常");
	/**
	 * 状态码
	 */
	private int code;
	/**
	 * 状态信息
	 */
	private String msg;

	public Meta(final int code, final String msg) {
		this.code = code;
		this.msg = msg;
	}

	public int getCode() {
		return code;
	}

	public String getMsg() {
		return msg;
	}
}
