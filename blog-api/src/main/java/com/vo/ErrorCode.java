package com.vo;

public enum ErrorCode {
    PARAMS_ERROR(10001,"参数有误"),
    ACCOUNT_PWD_NOT_EXIST(10002,"用户名或密码不存在"),
    TOKEN_ERROR(10003,"token不合法"),
    ACCOUNT_EXIST(10004,"账号已存在"),
    NOT_ATTENTION_YOURSELF(10005,"这是你自己的文章"),
    NOT_ATTENTION_AGAIN(10006,"重复关注"),
    CODE_ERROR(10007,"验证码错误"),
    NOT_YOU_QUESTION(10008,"对不起，这不是您发布的问题，没有权限确认解决"),
    NO_PERMISSION(70001,"无访问权限"),
    SESSION_TIME_OUT(90001,"会话超时"),
    NO_LOGIN(90002,"未登录"),
    UPLOAD_FAIL(20001,"上传失败");


    private int code;
    private String msg;

    ErrorCode(int code, String msg){
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
