package com.gdu.model.chat;

/**
 * Mqtt接收消息事件
 */
public class ReceiveChatEvent {

    private String topic;  //Mqtt主题
    private String message;  //Mqtt消息
    public ReceiveChatEvent(String topic, String message){
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
