package com.souban.wolf.controller;

import com.souban.wolf.config.WeixinKeyConstants;
import com.souban.wolf.handler.MenuHandler;
import com.souban.wolf.model.Token;
import com.souban.wolf.model.wolf.Game;
import com.souban.wolf.model.wolf.GameIdentify;
import com.souban.wolf.persistence.WolfMapper;
import com.souban.wolf.util.CommonUtil;
import com.souban.wolf.util.ResponseJson;
import com.sun.glass.ui.Menu;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Apple on 11/9/16.
 */

@RestController
@RequestMapping("/")
public class WechatController {

    @Autowired
    private WolfMapper wolfMapper;

    @Autowired
    private WxMpService wxService;

    @Autowired
    private WxMpMessageRouter router;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @RequestMapping(method = RequestMethod.GET,
            produces = "text/plain;charset=utf-8")
    public String authGet(@RequestParam(name = "signature",
            required = false) String signature,
                             @RequestParam(name = "timestamp",
                                     required = false) String timestamp,
                             @RequestParam(name = "nonce", required = false) String nonce,
                             @RequestParam(name = "echostr", required = false) String echostr){


//        this.logger.info("token{}",token.getAccessToken());
        return echostr;
    }


    @RequestMapping(method = RequestMethod.POST)
    public @ResponseBody
    String post(@RequestBody String requestBody,
                @RequestParam("signature") String signature,
                @RequestParam("timestamp") String timestamp,
                @RequestParam("nonce") String nonce,
                @RequestParam(name = "encrypt_type",
                        required = false) String encType,
                @RequestParam(name = "msg_signature",
                        required = false) String msgSignature) {

//        Token token = CommonUtil.getToken(WeixinKeyConstants.APPID,WeixinKeyConstants.SECRET);
//        this.logger.info("token{}",token.getAccessToken());
        WxMpXmlMessage intoMessage = WxMpXmlMessage.fromXml(requestBody);
        WxMpXmlOutMessage outMessage = this.route(intoMessage);
        if (outMessage == null) {
            return "";
        }
        String out = outMessage.toXml();
        this.logger.info("消息类型是{},openId是{}",intoMessage.getMsgType(),intoMessage.getFromUser());
        return out;
    }

    private WxMpXmlOutMessage route(WxMpXmlMessage message) {
        try {
            return this.router.route(message);
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
        }

        return null;
    }

}
