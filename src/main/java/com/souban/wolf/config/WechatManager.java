package com.souban.wolf.config;

import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpInMemoryConfigStorage;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.bean.result.WxMpUser;

/**
 * Created by Apple on 11/10/16.
 */
public class WechatManager {
    protected WxMpInMemoryConfigStorage config;



    /**
     * 获取用户微信信息
     *
     * @param openId
     * @return
     * @throws WxErrorException
     */
    public WxMpUser findUserInfo(String openId) throws WxErrorException {

        WxMpInMemoryConfigStorage config = new WxMpInMemoryConfigStorage();
        config.setAppId(WeixinKeyConstants.APPID);
        config.setSecret(WeixinKeyConstants.SECRET);
        config.setToken(WeixinKeyConstants.TOKEN);
        WxMpService wxMpService = new WxMpServiceImpl();
        wxMpService.setWxMpConfigStorage(config);

        String lang = "zh_CN"; //语言
        WxMpUser user = new WxMpUser();
        return user;
    }


}
