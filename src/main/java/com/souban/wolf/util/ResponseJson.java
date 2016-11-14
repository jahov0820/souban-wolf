package com.souban.wolf.util;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Created by Apple on 11/10/16.
 */
public class ResponseJson {

    private Integer status;
    private String errMsg;
    @JsonIgnore
    private String id;
    private Object data;

    public ResponseJson(Integer status, String errMsg, Object data) {
        super();
        this.status = status;
        this.errMsg = errMsg;
        this.data = data;
    }

    public ResponseJson(Integer status, Object data) {
        super();
        this.status = status;
        this.data = data;
    }

    public ResponseJson(Integer status, String errMsg, String id) {
        super();
        this.status = status;
        this.errMsg = errMsg;
        this.id = id;
    }

    public ResponseJson(Integer status, String errMsg, Long id) {
        this(status, errMsg, String.valueOf(id));
    }

    public ResponseJson(Integer status, String errMsg) {
        super();
        this.status = status;
        this.errMsg = errMsg;
    }

    public ResponseJson() {

    }


    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
