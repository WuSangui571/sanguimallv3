package com.sangui.sanguimall.util;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: sangui
 * @CreateTime: 2025-10-29
 * @Description: jwt 工具类
 * @Version: 1.0
 */
public class JwtUtils {
    /**
     * 自定义的密钥，新项目中，修改自定义密钥
     */
    public static final String SECRET = "ysysljjmtdsxxx";


    /**
     * 生成 jwt，即 token
     * @param userJson tUser 对象的 json
     * @return 返回 jwt，即 token
     */
    public static String createJwt(String userJson) {
        // 组装头数据
        Map<String, Object> header = new HashMap<>();
        header.put("alg", "HS256");
        header.put("typ", "JWT");

        return JWT.create()
                //头部
                .withHeader(header)
                //负载
                .withClaim("user", userJson)
                //签名
                .sign(Algorithm.HMAC256(SECRET));
    }

    /**
     * 验证 jwt
     * @param jwt 验证的 jwt 的字符串
     * @return jwt 是否正确
     */
    public static Boolean verifyJwt(String jwt) {
        try {
            // 使用秘钥创建一个 jwt 验证器对象
            JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(SECRET)).build();

            //验证 jwt ，如果没有抛出异常，说明验证通过，否则验证不通过
            jwtVerifier.verify(jwt);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 解析 jwt 的数据
     *
     */
    public static void parseJwt(String jwt) {
        try {
            // 使用秘钥创建一个验证器对象
            JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(SECRET)).build();

            //验证JWT，得到一个解码后的jwt对象
            DecodedJWT decodedJwt = jwtVerifier.verify(jwt);

            // 通过解码后的 jwt 对象，就可以获取里面的负载数据
            Claim nickClaim = decodedJwt.getClaim("nick");
            Claim ageClaim = decodedJwt.getClaim("age");
            Claim phoneClaim = decodedJwt.getClaim("phone");
            Claim birthDayClaim = decodedJwt.getClaim("birthDay");


            String nick = nickClaim.asString();
            int age = ageClaim.asInt();
            String phone = phoneClaim.asString();
            Date birthDay = birthDayClaim.asDate();

            System.out.println(nick + " -- " + age + " -- " + phone + " -- " + birthDay);
        } catch (TokenExpiredException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

//    public static TUser parseUserFromJwt(String jwt) {
//        try {
//            // 使用秘钥创建一个验证器对象
//            JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(SECRET)).build();
//
//            // 验证 jwt，得到一个解码后的 jwt 对象
//            DecodedJWT decodedJwt = jwtVerifier.verify(jwt);
//
//            // 通过解码后的 jwt 对象，就可以获取里面的负载数据
//            Claim userClaim = decodedJwt.getClaim("user");
//
//            String userJson = userClaim.asString();
//
//            return JsonUtils.toBean(userJson, TUser.class);
//        } catch (TokenExpiredException e) {
//            e.printStackTrace();
//            throw new RuntimeException(e);
//        }
//    }
}
