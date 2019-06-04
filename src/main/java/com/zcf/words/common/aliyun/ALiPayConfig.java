package com.zcf.words.common.aliyun;

/**
 * 支付宝支付所需的必要参数 理想情况下只需配置此处
 */
public class ALiPayConfig {
	// 合作身份者ID，以2088开头由16位纯数字组成的字符串
	public static String partner = "2088331222530263";

	// appid
	public static String appid = "2018120662469534";

	// 商户支付宝账号
	public static String seller_email = "13356512347@163.com";

	// 商户真实姓名
	public static String account_name = "贵阳视诺康商贸有限公司";

	// URL
	public static String url = "https://openapi.alipay.com/gateway.do";
	public static String timeoutExpress = "1000";

	// 商户的私钥RSA
	public static String private_key = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDo2fSpfWK4Pn2dePMFGH/5ZCGMO8X8hBMU+D9mnhebFxPG5fTOSxsS500wKUBJegp69LtbOzgfD4AYiQHaIOi2URN27gj3zyJi252967UseVrxacqlQyEmK3UOMv2Tbfdotonwczey3l/4UKMZZUHJw4/qpBPRoF1QDujXlcRehAT2ZIZisD1RnhGFrJko6nBUdEE9Wh/jiKt5vaiDTSS5T0D2P10Uv4NGY2woUkekRpokry9N+de2r/S1a89noh01W10csypP4laSWBMkPHg5p67/TboQO5VRZ7pAt6E/+C21Ieeh7RYxodwvwMJzRY8hVW1/WBEDgJorXIgV+AOFAgMBAAECggEAUWb4yFPkMiLuiSGiW7Kv8oCV1c+DrH6DqT4L9BTqjbZonI0BoyjQgzBHZMHXDpjSlx99bCxstzcVAf7LnfUJRctcMaSPkApirAq4v49qW+zk5SalyFaKfSyOFHOxAFxeNMYzvNKaszZYeJfAHP2wHRf39QB9STh1xqofrlZWOu/jd3xeY1hOIvGvODbtpe2Ty6+6dm+muHLC6dCAbU81FkxRsx3UT01imgyEwa68xC/mE/bHYcDhlty4H8wMhCjIaGAHtzHYQgcsuokbsAFFS9h1DNcs1bYLw3gXFnF74xflvEIJH4mmNaKkIev0rSlVNxjbERMdak+liA+CShaXAQKBgQD8+VIJHLYzMbdSGG0sN3/0/joPMSwreAgUm/woCmdcMFXQLkzC88jRzzRux58ko/rWhcqKSqjKNolauwMpKmAygavHUNE2uDLp4raMISmLC50otKKvWD1WOb9URVZkmfSmE0ZK7XDwVzjKDuLmNr+ivluT5DdVwUVLj8cKTx/WQQKBgQDrowOm8XOJY9P/G0xjeCgf2uLwSebPbDwAU/2z8LeK0TUnFfeuay69zsASJbg3OfHV4UcMGGX9BbTMO0f8N+bmogbOSbm1x/v0rEIioGCaPIOGgwaO0K1YAQKqC8bB1+gZsVjdXzAL+6GlTTx+n0Dr9+i4DV2OM6Nehbd0kvpERQKBgQD0jChrup+/mZ339LkE95nwwhpqOUTTuLNHCQuXwYbtraQnHbsC8xv9ButO9Ba6GvMPkkKbsVS+Nj5NwV9q2hYofQYMJeo5lZNSWeHSUPSR/+YrRYPHMewJgMWet3Cuy82fBh77q9Fz5T12xzE9D0vuDUsGCqYbdjyAsTrekmLagQKBgQDpMR0XJJH7YHDXYNObCLlktQCrXeduVKWYiYZkgg+R8HJSqPnBgc7zBl2sV+nTtaNvz465GJgt62y+RNh7cXMvwR9NHUcM78mXnLWigOa5HLPo9VgTE87Ibc+OKzNaeGn4qzrPcTkiUrbo2OPKR54/J8sGAiwY41+E1n30ftPvpQKBgBpqXAkH/jG74WzuR/6BfNHYEbGBhPUP34LsIYA5KR/FcepyChIq4ondwzS/Dw96Via1Yq3PvLGn5hb21kLxh0zCFEquRZ5/n6MXILstvAZkpBX+Z7HH+gn0CRh93IjpJ1/nzE8W08p/tL0b8lnuGxF6FRjIhDgXeN1CpTU5lInE";
	// 支付宝的公钥 RSA
	public static String ali_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEArNTtr9Q0mybE7UE7aDulGJD3ICA4SXNoFvsv38hoj5oE4CHfLKzdD3LG5Jt/v7Jc3kVwIcrnSiESbJ3oZSDwc9/9OFIal3mv42INlaJ2iXeTiAnEItDwx113oOyh0AaJHl/Y60Er17VZEXSyesO8clhZxvOF+E8ZuPuJgwebb45R1u0CSrwREeZZUwkd/UbWRrp5916RHRCkt5vYVEGVHwAC8J8s3zl6As6A0gLhkNx3cgmggeSeKnxdXWSJb7WRW44UJzIMR/R+mMCdi4rNEiXWRG5F6CcS/wS87GdEepfR6gSYQ706oewzqVEbNJn5vNcSO+lJ5uAj0dipzet9VwIDAQAB";
	// 签名方式 (支付回调签名方式)
	public static String pay_sign_type = "RSA2";

	/**
	 * 支付宝服务器主动通知商户服务器里指定的页面http/https路径。建议商户使用https
	 * 这里需要测试的话，需要用外网测试。https://www.ngrok.cc/ 这里有免费和付费的，实际上，免费用一下就可以了。
	 */
	// 商品的标题/交易标题/订单标题/订单关键字等。
	public static String subject = "商品购买";

	// ↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

	// 接口名称 固定为：alipay.trade.app.pay
	public static String method = "alipay.trade.app.pay";

	// 调用的接口版本，固定为：1.0
	public static String version = "1.0";

	// 销售产品码，商家和支付宝签约的产品码，为固定值QUICK_MSECURITY_PAY
	public static String product_code = "QUICK_MSECURITY_PAY";

	public static String notify_url_rechage = "http://47.107.67.75:8080/words/alipay/doRecharge";


	public static String notify_url_deposit = "http://172.16.1.233:8980/words/alipay/doRecharge";
	// 字符编码格式 目前支持 gbk 或 utf-8
	public static String input_charset = "utf-8";

}
