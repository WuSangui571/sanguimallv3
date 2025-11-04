package com.sangui.sanguimall.admin.util;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.sangui.sanguimall.admin.mapper.SysMenuMapper;
import com.sangui.sanguimall.admin.mapper.SysRoleMapper;
import com.sangui.sanguimall.admin.mapper.SysUserMapper;
import com.sangui.sanguimall.admin.model.entity.SysMenu;
import com.sangui.sanguimall.admin.model.entity.SysRole;
import com.sangui.sanguimall.admin.model.entity.SysUser;
import com.sangui.sanguimall.constant.Constants;
import com.sangui.sanguimall.util.JsonUtils;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: sangui
 * @CreateTime: 2025-11-02
 * @Description:
 * @Version: 1.0
 */
@Component
public class SysUserUtil {
    @Resource
    SysUserMapper sysUserMapper;
    @Resource
    SysRoleMapper sysRoleMapper;
    @Resource
    SysMenuMapper sysMenuMapper;

    public  SysUser parseUserFromJwt(String jwt) {
        try {
            // 使用秘钥创建一个验证器对象
            JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(Constants.SECRET)).build();

            // 验证 jwt，得到一个解码后的 jwt 对象
            DecodedJWT decodedJwt = jwtVerifier.verify(jwt);

            // 通过解码后的 jwt 对象，就可以获取里面的负载数据
            Claim userClaim = decodedJwt.getClaim("user");

            String userIdStr = userClaim.asString();
            Long userId = JsonUtils.toBean(userIdStr, Long.class);
            SysUser sysUser = sysUserMapper.selectByPrimaryKey(userId);
            // 查询一下当前用户的角色信息
            List<SysRole> roleList = sysRoleMapper.selectByUserId(sysUser.getUserId());
            // 字符串的角色列表
            List<String> stringRoleList = new ArrayList<>();
            roleList.forEach(role -> {
                stringRoleList.add(role.getRoleName());
            });
            // 设置用户的角色
            sysUser.setRoleList(stringRoleList);

            // 查询一下该用户有哪些菜单权限
            List<SysMenu> menuPermissionList = sysMenuMapper.selectMenuPermissionByUserId(sysUser.getUserId());
            System.out.println("menuPermissionList=" + menuPermissionList);
            sysUser.setMenuPermissionList(menuPermissionList);

            return sysUser;
        } catch (TokenExpiredException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
