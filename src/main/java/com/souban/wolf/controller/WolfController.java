package com.souban.wolf.controller;

import com.souban.wolf.config.WeixinKeyConstants;
import com.souban.wolf.model.wolf.Game;
import com.souban.wolf.model.wolf.GameIdentify;
import com.souban.wolf.persistence.WolfMapper;
import com.souban.wolf.util.ResponseJson;
import com.souban.wolf.util.ShuffleUtil;
import com.souban.wolf.util.WechatSendKFMessage;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpInMemoryConfigStorage;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.*;

/**
 * Created by Apple on 11/8/16.
 */

@RestController
public class WolfController {
    private static Logger logger = LoggerFactory.getLogger(WolfController.class);

    @Autowired
    private WolfMapper wolfMapper;

    @RequestMapping(value = "user_info",method = RequestMethod.POST)
    public Map<String, Object> wolf(@RequestParam(required = false) String code, HttpServletRequest request, HttpServletResponse response)throws IOException, WxErrorException {

        HttpSession session = request.getSession();
        WxMpService wxMpService = new WxMpServiceImpl();
        WxMpInMemoryConfigStorage config = new WxMpInMemoryConfigStorage();
        config.setAppId(WeixinKeyConstants.APPID);
        config.setSecret(WeixinKeyConstants.SECRET);
        wxMpService.setWxMpConfigStorage(config);
        if (StringUtils.isNotBlank(code)) {
            WxMpOAuth2AccessToken wxMpOAuth2AccessToken = wxMpService.oauth2getAccessToken(code);
            Map map =new HashMap();

            logger.info("userInfo请求,微信同意授权, code = " + code);
            logger.info("userInfo请求,微信同意授权, openId = " + wxMpOAuth2AccessToken.getOpenId());
            if (wxMpOAuth2AccessToken.getOpenId() == null){
                map.put("status",0);
                map.put("errMsg","openId过期");
            }
            map.put("status",1);
            map.put("errMsg","success");
            map.put("data",wxMpOAuth2AccessToken.getOpenId());
            return map;
        }

        return null;
    }


    @Autowired
    private WxMpMessageRouter router;


    @RequestMapping(value = "createGame",method = RequestMethod.POST)
    public ResponseJson createGame(@RequestParam(name = "openId",
            required = true) String openId) throws IOException{

        Game game = new Game();
        game.setMemberCount(12);
        game.setGodId(openId);
        int success = wolfMapper.createGame(game);
        if (game.getId() == null || success != 1){
            return new ResponseJson(0,"创建游戏失败");
        }
        game.setRoomNumber(game.getId() + 100);

        Integer[] shuffle = ShuffleUtil.randomArray(1,12,12);
        Integer[] wolf = new Integer[4];
        Integer[] villager = new Integer[4];
        Integer[] hunter = new Integer[1];
        Integer[] hag = new Integer[1];
        Integer[] defend = new Integer[1];
        Integer[] prophet = new Integer[1];
        System.arraycopy(shuffle,0,wolf,0,4);
        System.arraycopy(shuffle,4,villager,0,4);
        System.arraycopy(shuffle,8,hunter,0,1);
        System.arraycopy(shuffle,9,hag,0,1);
        System.arraycopy(shuffle,10,defend,0,1);
        System.arraycopy(shuffle,11,prophet,0,1);

        wolfMapper.insertGameIdentify(wolf,game.getRoomNumber(),6);
        wolfMapper.insertGameIdentify(villager,game.getRoomNumber(),1);
        wolfMapper.insertGameIdentify(hunter,game.getRoomNumber(),4);
        wolfMapper.insertGameIdentify(hag,game.getRoomNumber(),3);
        wolfMapper.insertGameIdentify(defend,game.getRoomNumber(),5);
        wolfMapper.insertGameIdentify(prophet,game.getRoomNumber(),2);

        String description = String.format("%s号是狼人,%s号是村民,%s号是猎人,%s号是女巫,%s号是白痴,%s号是预言家,", Arrays.toString(wolf), Arrays.toString(villager), Arrays.toString(hunter), Arrays.toString(hag), Arrays.toString(defend), Arrays.toString(prophet));
        wolfMapper.updateRoomNumber(game.getId(),game.getId() + 100,description);
        game.setDescription(description);

        String  message = String.format("房间号%s,你是本场游戏的法官，共12人,配置%s <a href='https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=%s?roomId=%s&response_type=code&scope=snsapi_base&state=State#wechat_redirect'>点击查看身份列表</a> ",game.getRoomNumber(),game.getDescription(),WeixinKeyConstants.APPID,WeixinKeyConstants.REDIRECT_IDENTIFYLIST,game.getRoomNumber());
        WechatSendKFMessage.sendKfMessage(message,openId);

        return new ResponseJson(1, "SUCCESS",game);
    }




    @RequestMapping(value = "joinGame",method = RequestMethod.POST)
    public ResponseJson joinGame(@RequestParam(name = "openId",
            required = true) String openId,@RequestParam(name = "roomId",
            required = true) Integer roomId) throws IOException{

        Integer isGameAvaliable = wolfMapper.isGameAvaliable(roomId);
        if (isGameAvaliable == 0){
            return new ResponseJson(0,"该房间已经过期或房间号错误");
        }
        Integer userCount = wolfMapper.userCount(roomId);
        Integer alreadyInGame = wolfMapper.alreadyInGame(openId, roomId);
        Integer isGod = wolfMapper.isGod(openId,roomId);
        if (isGod != 0){
            Game game = wolfMapper.getGame(roomId);
            return new ResponseJson(1,String.format("你是本场游戏的法官，共12人,配置%s",game.getDescription()));
        }
        if (alreadyInGame != 0){
            GameIdentify gameIdentify = wolfMapper.getGameIdentifyByOpenId(openId,roomId);
            return new ResponseJson(0,String.format("你已加入游戏，你是%s号玩家,身份是%s",gameIdentify.getGameId(),gameIdentify.getIdentifyName()));
        }
        if (userCount >= 12){
            return new ResponseJson(0,"游戏已经开始或结束");
        }
        int  succress = wolfMapper.addToGame(openId,roomId,userCount+1);
        if (succress != 1){
            return new ResponseJson(0,"加入游戏失败");
        }
        GameIdentify gameIdentify = wolfMapper.getGameIdentify(userCount+1,roomId);
        return new ResponseJson(1, String.format("加入游戏成功，你是%s号玩家,身份是%s",userCount+1,gameIdentify.getIdentifyName()));
    }



    @RequestMapping(value = "identityList",method = RequestMethod.POST)
    public ResponseJson identityList(@RequestParam(name = "openId",
            required = true) String openId,@RequestParam(name = "roomId",
            required = true) Integer roomId){
        Integer isGod = wolfMapper.isGod(openId,roomId);
        List<GameIdentify> identifyList = wolfMapper.getIdentityList(roomId);

        if (isGod == 0){
            for (GameIdentify gameIdentify : identifyList) {
                gameIdentify.setIdentifyName("xxx");
            }
            return new ResponseJson(1, "success",identifyList);
        }else{
            for (GameIdentify gameIdentify : identifyList) {
                try {
                    String nickName = URLDecoder.decode(gameIdentify.getNickName(), "utf-8") ;
                    if (nickName != null){
                        gameIdentify.setNickName(nickName);
                    }
                }catch (Exception e){

                }
            }

        }
        return new ResponseJson(1, "success",identifyList);
    }



    @RequestMapping(value = "getIdentity",method = RequestMethod.POST)
    public ResponseJson getIdentity(@RequestParam(name = "openId",
            required = true) String openId,@RequestParam(name = "roomId",
            required = true) Integer roomId){
        GameIdentify gameIdentify = wolfMapper.getGameIdentifyByOpenId(openId,roomId);

        if (gameIdentify != null){
            try {
                String nickName = URLDecoder.decode(gameIdentify.getNickName(), "utf-8") ;
                if (nickName != null){
                    gameIdentify.setNickName(nickName);
                }
            }catch (Exception e){

            }
            return new ResponseJson(1, "success",gameIdentify);
        }
        return new ResponseJson(0, "fail");
    }


    @RequestMapping(value = "roleAssign",method = RequestMethod.POST)
    public ResponseJson roleAssign(@RequestParam(name = "openId",
            required = true) String openId){


        return new ResponseJson(0, "fail");
    }



}
