package com.sangui.sanguimall.admin.web;


import com.github.pagehelper.PageInfo;
import com.sangui.sanguimall.admin.model.entity.SysUser;
import com.sangui.sanguimall.admin.service.SysUserService;
import com.sangui.sanguimall.result.R;
import jakarta.annotation.Resource;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: sangui
 * @CreateTime: 2025-11-02
 * @Description: SysUserController
 * @Version: 1.0
 */
@RestController
@RequestMapping("/sysUser")
public class SysUserController {
    @Resource
    SysUserService sysUserService;

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
        //System.out.println("login info 's authentication:" + authentication);
        SysUser sysUser = (SysUser) authentication.getPrincipal();
        return R.ok(sysUser);
    }

    /**
     * 获取全部用户的信息
     * @param current 当前页数
     * @return 当前页数的用户信息
     */
    @GetMapping("/sysUsers")
    public R getUsers(@RequestParam(value = "current",required = false)Integer current){
        if (current == null){
            current = 1;
        }
        PageInfo<SysUser> pageInfo = sysUserService.getSysUsersByPage(current);

        return R.ok(pageInfo);
    }

    /**
     * 响应给前端指定 id 的用户信息
     * @param id 用户 id
     * @return 用户信息
     */
    @GetMapping("/{id}")
    public R getUserDetail(@PathVariable("id")Long id){
        SysUser sysUser = sysUserService.getUserDetailById(id);
        return R.ok(sysUser);
    }
}
