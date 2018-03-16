package com.cheelem.interpreter.entity;

/**
 * Created by 黄宇航 on 2018/2/28.
 */

public class SocketInfo {
    private int code;
    private Object obj = null;

    public SocketInfo(int code, Object obj) {
        this.code = code;
        this.obj = obj;
    }

    public SocketInfo(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }
}
