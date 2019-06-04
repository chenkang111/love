package com.zcf.words.common.json;

import java.util.List;
/**
 * Created by YuanQJ on 2018/11/01.
 */
public class LayUiResult{

    private String code;
    private String msg;
    private Integer count;
    private List<?> data;

    public LayUiResult(String code, String msg, Integer count, List<?> data) {
        this.code = code;
        this.msg = msg;
        this.count = count;
        this.data = data;
    }

    public LayUiResult(String code, String msg, Long count, List<?> data) {
        this.code = code;
        this.msg = msg;
        this.count = count.intValue();
        this.data = data;
    }

    public static LayUiResult LayUi(String code, String msg, Integer count, List<?> data) {
		LayUiResult lay = new LayUiResult(code, msg, count, data);
		return lay;
	}

    public static LayUiResult LayUi(String code, String msg, Long count, List<?> data) {
		LayUiResult lay = new LayUiResult(code, msg, count, data);
		return lay;
	}

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<?> getData() {
        return data;
    }

    public void setData(List<?> data) {
        this.data = data;
    }
}
