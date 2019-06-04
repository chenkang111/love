package com.zcf.words.common.aliyun;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayFundTransToaccountTransferRequest;
import com.alipay.api.response.AlipayFundTransToaccountTransferResponse;

/**
 * @version 创建时间：2018年3月26日 下午5:29:31
 * 类说明
 */
public class transfer {

    //配置初始化信息参数
    public static DefaultAlipayClient alipayclient() {
        DefaultAlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do",
                ALiPayConfig.appid, ALiPayConfig.private_key, "json",
                ALiPayConfig.input_charset, ALiPayConfig.ali_public_key,
                "RSA2");
        return alipayClient;
    }


    //单笔转账到支付宝账户
    public static int AlipayFundTransToaccountTransfer(String realname, Double money, String alicount) {
        SimpleDateFormat sdf = new SimpleDateFormat(
                "yyyyMMddHHmmss");
        Date date = new Date();
        String logtDate = sdf.format(date)
                + (int) (Math.random() * 9) + (int) (Math.random() * 9);
        AlipayClient aliClient = alipayclient();
        AlipayFundTransToaccountTransferRequest request = new AlipayFundTransToaccountTransferRequest();
        request.setBizContent("{" +
                //参考接口文档参数解释：https://docs.open.alipay.com/api_28/alipay.fund.trans.toaccount.transfer
                "\"out_biz_no\":\"" + logtDate + "\"," +
                "\"payee_type\":\"ALIPAY_LOGONID\"," +
                "\"payee_account\":\"" + alicount + "\"," +
                "\"amount\":\"" + money + "\"," +
                "\"payee_real_name\":\"" + realname + "\"," +
                "\"remark\":\"提现\"" +
                "}");
        AlipayFundTransToaccountTransferResponse response = null;
        request.setNotifyUrl(ALiPayConfig.notify_url_deposit);
        try {
            response = aliClient.execute(request);
            System.out.println(response.getBody());
        } catch (AlipayApiException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (response.isSuccess()) {
            return 1;
        } else {
            return 2;
        }
    }

}
