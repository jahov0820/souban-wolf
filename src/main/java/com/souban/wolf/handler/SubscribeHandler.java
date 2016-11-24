package com.souban.wolf.handler;

import com.souban.wolf.config.WeixinKeyConstants;
import com.souban.wolf.persistence.WolfMapper;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpInMemoryConfigStorage;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import com.souban.wolf.builder.TextBuilder;
import java.util.Map;

/**
 * Created by Apple on 11/9/16.
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SubscribeHandler extends AbstractHandler {

    @Autowired
    private WolfMapper wolfMapper;

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage,
                                    Map<String, Object> context, WxMpService weixinService,
                                    WxSessionManager sessionManager) throws WxErrorException {

        this.logger.info("新关注用户 OPENID: " + wxMessage.getFromUser());

        WxMpInMemoryConfigStorage config = new WxMpInMemoryConfigStorage();
        config.setAppId(WeixinKeyConstants.APPID);
        config.setSecret(WeixinKeyConstants.SECRET);
        weixinService.setWxMpConfigStorage(config);
        // 获取微信用户基本信息
        WxMpUser userWxInfo = weixinService.getUserService()
                .userInfo(wxMessage.getFromUser(), "zh_CN");

        if (userWxInfo != null) {
            // TODO 可以添加关注用户到本地
            if (wolfMapper.isWechatUserExist(userWxInfo.getOpenId()) == 0){
                wolfMapper.insertWechatUserInfo(userWxInfo.getOpenId(),userWxInfo.getHeadImgUrl(),userWxInfo.getNickname());
            }
        }
        WxMpXmlOutMessage responseResult = null;
        try {
            responseResult = handleSpecial(wxMessage);
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
        }

        if (responseResult != null) {
            return responseResult;
        }

        try {
            return new TextBuilder().build("欢迎关注搜办,加入狼人杀游戏请输入房间号", wxMessage, weixinService);
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
        }

        return null;
    }

    /**
     * 处理特殊请求，比如如果是扫码进来的，可以做相应处理
     */
    private WxMpXmlOutMessage handleSpecial(WxMpXmlMessage wxMessage)
            throws Exception {
        //TODO
        return null;
    }

}
