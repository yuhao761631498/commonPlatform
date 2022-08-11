package com.gdu.command.ui.message;

/**
 * Copyright (C), 2020-2030, 武汉中旗生物医疗电子有限公司
 * <p>
 * 功能描述:
 * <p>
 * 创建时间: 2022/6/17 15:44
 *
 * @author yuhao
 */
public class NoticeMessageEvent {

    private String topic;  //Mqtt主题
    private String message;  //Mqtt消息
    public NoticeMessageEvent(String topic, String message){
        this.topic = topic;
        this.message = message;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
