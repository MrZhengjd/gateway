package com.game.common.util;


import com.alibaba.fastjson.JSON;
import com.game.common.exception.TokenException;
import io.jsonwebtoken.*;

import java.util.Date;

public class JWTUtil {
    private final static String TOKEN_SCRETE = "zhengScret";
    private final static Long TOKEN_EXPIRE = DateUtil.ONEDAY_MILLISECOND * 7;

    public static TokenBody getTokenBody(String token)throws TokenException {
        try {
            Claims claims = Jwts.parser().setSigningKey(TOKEN_SCRETE).parseClaimsJws(token).getBody();
            String subject = claims.getSubject();
            TokenBody body = JSON.parseObject(subject,TokenBody.class);
            return body;
        }catch (Throwable e){
            e.printStackTrace();
            TokenException exception = new TokenException("error ",e);
            if (e instanceof ExpiredJwtException){
                exception.setExpired(true);
            }
            throw  exception;
        }
    }
    public static String getUserToken(String openId,long userId,long playerId,String serverId,String... params){
        SignatureAlgorithm algorithm = SignatureAlgorithm.HS256;
        long nowMills = System.currentTimeMillis();
        Date now = new Date(nowMills);
        TokenBody body = new TokenBody(openId,userId,playerId,serverId,params);
        String subject = JSON.toJSONString(body);
        JwtBuilder builder = Jwts.builder().setId(String.valueOf(nowMills))
                .setIssuedAt(now).setSubject(subject).signWith(algorithm,TOKEN_SCRETE);
        long expMills = nowMills + TOKEN_EXPIRE;
        Date expired = new Date(expMills);
        builder.setExpiration(expired);
        return builder.compact();

    }
    public static class TokenBody{
//        public TokenBody() {
//        }

        public TokenBody(String openId, long userId, long playerId, String serverId, String[] param) {
            this.openId = openId;
            this.userId = userId;
            this.playerId = playerId;
            this.serverId = serverId;
            this.param = param;
        }

        private String openId;
        private long userId;
        private long playerId;
        private String serverId = "1";
        private String[] param;

        public String getOpenId() {
            return openId;
        }

        public void setOpenId(String openId) {
            this.openId = openId;
        }

        public long getUserId() {
            return userId;
        }

        public void setUserId(long userId) {
            this.userId = userId;
        }

        public long getPlayerId() {
            return playerId;
        }

        public void setPlayerId(long playerId) {
            this.playerId = playerId;
        }

        public String getServerId() {
            return serverId;
        }

        public void setServerId(String serverId) {
            this.serverId = serverId;
        }

        public String[] getParam() {
            return param;
        }

        public void setParam(String[] param) {
            this.param = param;
        }
    }
}
