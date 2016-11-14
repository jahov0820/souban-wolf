package com.souban.wolf.handler;

import com.souban.wolf.config.WeixinKeyConstants;
import com.souban.wolf.handler.AbstractHandler;
import com.souban.wolf.model.Token;
import com.souban.wolf.model.wolf.Game;
import com.souban.wolf.model.wolf.GameIdentify;
import com.souban.wolf.persistence.WolfMapper;
import com.souban.wolf.util.CommonUtil;
import com.souban.wolf.util.JsonUtils;
import com.souban.wolf.util.ResponseJson;
import com.souban.wolf.util.WechatSendKFMessage;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import com.souban.wolf.builder.TextBuilder;

import java.io.IOException;
import java.util.Map;

/**
 * Created by Apple on 11/9/16.
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class MsgHandler extends AbstractHandler {


    @Autowired
    private WolfMapper wolfMapper;

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage,
                                    Map<String, Object> context, WxMpService weixinService,
                                    WxSessionManager sessionManager) {

        if (!wxMessage.getMsgType().equals(WxConsts.XML_MSG_EVENT)) {
            //TODO 可以选择将消息保存到本地
        }

        String openId = wxMessage.getFromUser();
        String messageType = wxMessage.getMsgType();
        String message = "";
        if (messageType.equals(WxConsts.XML_MSG_TEXT)){
            Integer roomId = Integer.parseInt(wxMessage.getContent());
            Integer isGameAvaliable = wolfMapper.isGameAvaliable(roomId);
            if (isGameAvaliable == 0){
                message = "该房间已经过期或房间号错误";
            }
            Integer userCount = wolfMapper.userCount(roomId);
            Integer alreadyInGame = wolfMapper.alreadyInGame(openId, roomId);
            Integer isGod = wolfMapper.isGod(openId,roomId);
            if (isGod != 0){
                Game game = wolfMapper.getGame(roomId);
                message = String.format("房间号%s,你是本场游戏的法官，共12人,配置%s <a href='https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx4daccaf144b416c4&redirect_uri=%s?roomId=%s&response_type=code&scope=snsapi_base&state=State#wechat_redirect'>点击查看身份列表</a> ",roomId,game.getDescription(),WeixinKeyConstants.REDIRECT_IDENTIFYLIST,roomId);
            }else if (alreadyInGame != 0){
                GameIdentify gameIdentify = wolfMapper.getGameIdentifyByOpenId(openId,roomId);
                message = String.format("你已加入游戏，你是%s号玩家,<a href='https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx4daccaf144b416c4&redirect_uri=%s?roomId=%s&response_type=code&scope=snsapi_base&state=State#wechat_redirect'>点击查看身份</a>",gameIdentify.getGameId(),WeixinKeyConstants.REDIRECT_GETIDENTIFY,roomId);
            }
            else if (userCount >= 12){
                message = "游戏已经开始或结束";
            }else{
                int  succress = wolfMapper.addToGame(openId,roomId,userCount+1);
                if (succress != 1){
                    message = "加入游戏失败";
                }
                GameIdentify gameIdentify = wolfMapper.getGameIdentify(userCount+1,roomId);
                if (userCount +1 == 12){ //游戏开始 给法官推送消息
                    Game game = wolfMapper.getGame(roomId);
                    WechatSendKFMessage.sendKfMessage(String.format("房间号:%s,游戏开始,<a href='https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx4daccaf144b416c4&redirect_uri=%s?roomId=%s&response_type=code&scope=snsapi_base&state=State#wechat_redirect'>点击查看身份列表</a>",roomId,WeixinKeyConstants.REDIRECT_IDENTIFYLIST,roomId),game.getGodId());
                }
                message = String.format("加入游戏成功，你是%s号玩家,<a href='https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx4daccaf144b416c4&redirect_uri=%s?roomId=%s&response_type=code&scope=snsapi_base&state=State#wechat_redirect'>点击查看身份</a>",gameIdentify.getGameId(),WeixinKeyConstants.REDIRECT_GETIDENTIFY,roomId);
            }

        }
        return new TextBuilder().build(message, wxMessage, weixinService);

    }

}
