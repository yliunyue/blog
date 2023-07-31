package com.utils;

import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JWTUtils {
    // 密钥
    private static final String jwtToken="123456dragon!@#$$";
    public static String createToken(Long userId){
        Map<String,Object> claims=new HashMap<>();
        claims.put("userId",userId);
        JwtBuilder jwtBuilder= Jwts.builder()
                .signWith(SignatureAlgorithm.HS256,jwtToken)//签发算法，密钥为jwtToken
                .setClaims(claims)//body数据，要唯一，自己设置
                .setIssuedAt(new Date())//设置签发时间
                .setExpiration(new Date(System.currentTimeMillis()+24*60*60*1000));//一天的时间
        String token=jwtBuilder.compact();
        return token;
    }

    public static Map<String,Object> checkToken(String token){
        try {
            Jwt parse = Jwts.parser().setSigningKey(jwtToken).parse(token);
            return (Map<String, Object>) parse.getBody();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /*public static void main(String[] args) {
        //加密
        String token = JWTUtils.createToken(100L);
        System.out.println(token);
        //解析
        Map<String, Object> map = JWTUtils.checkToken(token);
        System.out.println(map.get("userId"));
    }*/
}
