package com.souban.wolf.model;

/**
 * Created by wufei on 5/25/16.
 */
public class WechatEvent {

    private String toUserName;
    private String fromUserName;
    private String createTime;
    private String msgType;
    private String event;
    private String key;
    private String content;

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

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "wechat_event{" +
                "toUserName=" + toUserName +
                ", fromUserName=" + fromUserName +
                ", createTime=" + createTime +
                ", msgType=" + msgType +
                ", event=" + event +
                ", key=" + key +
                ", content=" + content +
                '}';
    }
}
