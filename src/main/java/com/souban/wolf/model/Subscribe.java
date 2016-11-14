package com.souban.wolf.model;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by wufei on 5/20/16.
 */
public class Subscribe {

    private String toUserName;
    private String fromUserName;
    private String createTime;
    private String msgType;
    private String event;

    public String getToUserName() {
        return toUserName;
    }

    public void setToUserName(String toUserName) {
        this.toUserName = toUserName;
    }

    public String getFromUserName() {
        return fromUserName;
    }

    public void setFromUserName(String fromUserName) {
        this.fromUserName = fromUserName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    @Override
    public String toString() {
        return "Subscribe{" +
                "toUserName=" + toUserName +
                ", fromUserName='" + fromUserName  +
                ", createTime='" + createTime  +
                ", msgType=" + msgType  +
                ", event=" + event  +
                '}';
    }
}
