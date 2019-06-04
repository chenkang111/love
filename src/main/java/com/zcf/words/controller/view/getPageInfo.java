package com.zcf.words.controller.view;


import com.zcf.words.App;
import com.zcf.words.controller.api.ChatRoomServerEndpoint;
import com.zcf.words.entity.Highcontent;
import com.zcf.words.entity.Highlight;
import com.zcf.words.mapper.HighcontentMapper;
import com.zcf.words.mapper.HighlightMapper;
import org.jsoup.Connection;
import org.jsoup.Connection.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class getPageInfo {

    //此处是解决无法注入的关键
    private static ApplicationContext applicationContext;
    public static void setApplicationContext(ApplicationContext applicationContext) {
        getPageInfo.applicationContext = applicationContext;
    }

    @Autowired
    private  HighcontentMapper highcontentMapper;

    @Autowired
    private  HighlightMapper highlightMapper;

    private static Map<String, String> cookies = null;
//
//    public static void main(String args[]) {
//        try {
//            //ConfigurableApplicationContext configurableApplicationContext = SpringApplication.run(App.class, args);
//            //解决WebSocket不能注入的问题
//            //ChatRoomServerEndpoint.setApplicationContext(configurableApplicationContext);
//            getPageInfo page = new getPageInfo();
//            page.getPage("7");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }


    public  void getPage(String id) throws IOException {
        Connection con = Jsoup
                .connect("http://www.ltsq.fun/member/index.php");// 获取连接
        con.header("User-Agent",
                "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:29.0) Gecko/20100101 Firefox/29.0");// 配置模拟浏览器
        Response rs = con.execute();// 获取响应
        Document d1 = Jsoup.parse(rs.body());// 转换为Dom树
        List<Element> et = d1.select("#form1");// 获取form表单，可以通过查看页面源码代码得知
        // 获取，cooking和表单属性，下面map存放post时的数据
        Map<String, String> datas = new HashMap<>();

        for (Element e : et.get(0).getAllElements()) {
            if (e.attr("name").equals("userid")) {
                e.attr("value", "123456");// 设置用户名
            }
            if (e.attr("name").equals("pwd")) {
                e.attr("value", "123456"); // 设置用户密码
            }
            if (e.attr("name").length() > 0) {// 排除空值表单属性
                datas.put(e.attr("name"), e.attr("value"));
            }
        }

        /**
         * 第二次请求，post表单数据，以及cookie信息
         *
         * **/
        Connection con2 = Jsoup
                .connect("http://www.ltsq.fun/member/index_do.php");
        con2.header("User-Agent",
                "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:29.0) Gecko/20100101 Firefox/29.0");
        // 设置cookie和post上面的map数据
        Response login = con2.ignoreContentType(true).method(Method.POST)
                .data(datas).cookies(rs.cookies()).execute();
        // 打印，登陆成功后的信息
        //System.out.println(login.body());
        cookies = login.cookies();

        /**
         * 第三次请求，获取隐藏数据
         *
         * **/
        Connection con3 = Jsoup
                .connect("http://www.ltsq.fun/plus/list.php?tid=" + id).cookies(cookies);
        con3.header("User-Agent",
                "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:29.0) Gecko/20100101 Firefox/29.0");
        con3.data("username", "123456");
        Response rs2 = con3.execute();// 获取响应
        Document d2 = Jsoup.parse(rs2.body());// 转换为Dom树

        //所有表头和内容
        List<Element> et2 = d2.select(".zhankai");// 获取form表单，可以通过查看页面源码代码得知
        List<Element> et3 = d2.select(".H-theme-font-color1");// 获取form表单，可以通过查看页面源码代码得知
        highlightMapper = applicationContext.getBean(HighlightMapper.class);
        highcontentMapper = applicationContext.getBean(HighcontentMapper.class);

        int count = highlightMapper.selectCount(null);
        for (int i = 0; i < et3.size(); i++) {
            count++;
            Highlight highlight = new Highlight();
            highlight.setHighlight_id(count);
            highlight.setHighlight_toptag_id(Integer.parseInt(id) - 7 + 1);
            highlight.setHighlight_content(et3.get(i).text());
            Highlight h = highlightMapper.selectByPrimaryKey(count);
            if(h!=null&&h.getHighlight_id().toString().length()>0){
                highlightMapper.updateByPrimaryKeySelective(highlight);
            }else{
                highlightMapper.insertSelective(highlight);
            }
            StringBuffer sbf = new StringBuffer(et2.get(i).html());
            String s = sbf.toString();
            String[] strs = s.split("<br>");
            for (String str : strs) {
                Highcontent highcontent = new Highcontent();
                highcontent.setHighcontent_highlight_id(count);
                highcontent.setHighcontent_content(str.replace("<br>", ""));
                highcontentMapper.insertSelective(highcontent);
            }
        }
        System.out.print("爬去完成");

    }


}
