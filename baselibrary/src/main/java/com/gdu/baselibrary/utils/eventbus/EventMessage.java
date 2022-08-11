package com.gdu.baselibrary.utils.eventbus;

/**
 * EventBus发送公共消息的对象.
 * @author wixche
 */
public class EventMessage {
    private int msgType;
    private String message;
    private Object content;

    public EventMessage() {
    }

    public EventMessage(int msgType) {
        this.msgType = msgType;
        message = "";
    }

    public EventMessage(String message) {
        this.message = message;
    }

    public EventMessage(int msgType, String message) {
        this.message = message;
        this.msgType = msgType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }
}
