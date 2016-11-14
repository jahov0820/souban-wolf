package com.souban.wolf.persistence;

import com.souban.wolf.model.wolf.Game;

import com.souban.wolf.model.wolf.GameIdentify;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Apple on 11/9/16.
 */
@Mapper
public interface WolfMapper {

    int insertWechatUserInfo(@Param("openId") String openId,@Param("headImgUrl") String headImgUrl,@Param("nickName") String nickName);
    int isWechatUserExist(@Param("openId") String openId);

    int createGame(Game game);
    int updateRoomNumber(@Param("id") Integer id,@Param("roomId") Integer roomId,@Param("description") String description);
    int addToGame(@Param("openId") String openId,@Param("roomId") Integer roomId,@Param("gameId") Integer gameId);
    int userCount(Integer roomId);
    int alreadyInGame(@Param("openId") String openId,@Param("roomId") Integer roomId);
    int isGod(@Param("openId") String openId,@Param("roomId") Integer roomId);
    int isGameAvaliable(@Param("roomId") Integer roomId);
    int insertGameIdentify(@Param("gameIds") Integer[] gameIds, @Param("roomId") Integer roomId, @Param("identify") Integer identify);
    Game getGame(@Param("roomId") Integer roomId);
    GameIdentify getGameIdentify(@Param("gameId") Integer gameId, @Param("roomId") Integer roomId);
    GameIdentify getGameIdentifyByOpenId(@Param("openId") String openId, @Param("roomId") Integer roomId);
    List<GameIdentify> getIdentityList(@Param("roomId") Integer roomId);
}
