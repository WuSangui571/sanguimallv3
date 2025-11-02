package com.sangui.sanguimall.admin.util;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.sangui.sanguimall.admin.model.entity.SysUser;
import com.sangui.sanguimall.constant.Constants;
import com.sangui.sanguimall.util.JsonUtils;

/**
 * @Author: sangui
 * @CreateTime: 2025-11-02
 * @Description:
 * @Version: 1.0
 */
public class SysUserUtil {
    public static SysUser parseUserFromJwt(String jwt) {
        try {
            // 使用秘钥创建一个验证器对象
            JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(Constants.SECRET)).build();

            // 验证 jwt，得到一个解码后的 jwt 对象
            DecodedJWT decodedJwt = jwtVerifier.verify(jwt);

            // 通过解码后的 jwt 对象，就可以获取里面的负载数据
            Claim userClaim = decodedJwt.getClaim("user");

            String userJson = userClaim.asString();

            return JsonUtils.toBean(userJson, SysUser.class);
        } catch (TokenExpiredException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
