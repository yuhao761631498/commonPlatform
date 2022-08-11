package com.gdu.baselibrary.network;

/**
 * Created by Administrator on 2016/11/11 0011.
 */

public class BaseBean<T> {
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
    public T data;

    /**
     * 备用数据
     */
    public T extra;


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
