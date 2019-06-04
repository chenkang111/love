package com.zcf.words.common.json;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * json响应中的body
 *
 * @author six
 * @date 2018/07/16
 */
public class Body {

    /**
     * 请求成功
     */
    public static Body BODY_200 = new Body(Meta.CODE_200);
    /**
     * 错误的请求
     */
    public static Body BODY_400 = new Body(Meta.CODE_400);
    /**
     * 未授权
     */
    public static Body BODY_401 = new Body(Meta.CODE_401);
    /**
     * 请求不存在
     */
    public static Body BODY_404 = new Body(Meta.CODE_404);
    /**
     * 参数验证错误
     */
    public static Body BODY_451 = new Body(Meta.CODE_451);
    /**
     * token验证错误
     */
    public static Body BODY_460 = new Body(Meta.CODE_460);
    /**
     * token格式错误
     */
    public static Body BODY_461 = new Body(Meta.CODE_461);
    /**
     * token过期
     */
    public static Body BODY_462 = new Body(Meta.CODE_462);
    /**
     * 对象不存在
     */
    public static Body BODY_470 = new Body(Meta.CODE_470);
    /**
     * 服务器内部错误
     */
    public static Body BODY_500 = new Body(Meta.CODE_500);

    private Meta meta;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object data;

    public Body(Meta meta) {
        this.meta = meta;
    }

    public Body(Meta meta, Object data) {
        this.meta = meta;
        this.data = data;
    }

    public static Body newInstance() {
        return Body.BODY_200;
    }

    public static Body newInstance(Object data) {
        return Body.newInstance(Meta.CODE_200, data);
    }

    public static Body newInstance(Meta meta, Object data) {
        return new Body(meta, data);
    }

    /*
     * public static Body newInstance(Meta meta) { return new Body(meta); }
     */

    public static Body newInstance(int code, String msg) {
        return new Body(new Meta(code, msg));
    }

    public Meta getMeta() {
        return meta;
    }

    public Object getData() {
        return data;
    }
}
