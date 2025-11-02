package com.sangui.sanguimall.admin.web;


import com.sangui.sanguimall.admin.model.entity.SysUser;
import com.sangui.sanguimall.result.R;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: sangui
 * @CreateTime: 2025-11-02
 * @Description: SysUserController
 * @Version: 1.0
 */
@RestController
@RequestMapping("/sysUser")
public class SysUserController {
    /**
     * 判断是否可以免密登录
     * @return 判断结果
     */
    @GetMapping("/freeLogin")
    public R freeLogin(){
        // TokenVerifyFilter 会自动验证，这里不需要验证
        return  R.ok();
    }

    /**
     * 获取登录人信息
     * @param authentication 注入的 SpringSecurity 的信息
     * @return 包含当前登录人 tUser 对象信息的响应
     */
    @GetMapping("/info")
    public R getLoginInfo(Authentication authentication){
        System.out.println("login info 's authentication:" + authentication);
        SysUser sysUser = (SysUser) authentication.getPrincipal();
        return R.ok(sysUser);
    }
}
