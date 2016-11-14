package com.souban.wolf.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.souban.wolf.config.WeixinKeyConstants;
import com.souban.wolf.model.Token;
import okhttp3.*;
import org.apache.commons.collections.map.HashedMap;
import org.json.JSONException;

import java.io.IOException;
import java.util.Map;

/**
 * Created by Apple on 11/14/16.
 */
public class WechatSendKFMessage {

    public static void sendKfMessage(String message,String openId) {
        OkHttpClient client = new OkHttpClient();
        Token token = CommonUtil.getToken(WeixinKeyConstants.APPID,WeixinKeyConstants.SECRET);
        String url = String.format("https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=%s",token.getAccessToken());
        Map<Object,Object> param = new HashedMap();
        Map<Object,Object> subParam = new HashedMap();
        subParam.put("content",message);
        param.put("touser",openId);
        param.put("msgtype","text");
        param.put("text",subParam);
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),gson.toJson(param));
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        try {

            Response response = client.newCall(request).execute();
        }catch(IOException io)
        {

        }
    }



}
