package com.gdu.mqttchat;


public class NetResult{
    /**
     * 状态码
     * 0  获取成功
     * 1  获取失败
     */
    public int code;

    /**
     * 错误信息 ,成功则返回空
     */
    public String msg;

    /**
     * 数据
     */
    public String data;

    /**
     * 备用数据
     */
    public String extra;


    @Override
    public String toString() {
        return "BaseBean{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", extra='" + extra + '\'' +
                ", data=" + data +
                '}';
    }
}
