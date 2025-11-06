package com.sangui.sanguimall.admin.web;


import com.github.pagehelper.PageInfo;
import com.sangui.sanguimall.admin.model.entity.SysUser;
import com.sangui.sanguimall.admin.model.query.SysUserQuery;
import com.sangui.sanguimall.admin.model.vo.SysUserVo;
import com.sangui.sanguimall.admin.service.SysUserService;
import com.sangui.sanguimall.result.R;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
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
    //@PreAuthorize("hasAuthority('sys:user:info')")
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
    @PreAuthorize("hasAuthority('sys:user:list')")
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
        SysUserVo sysUserVo = sysUserService.getUserDetailById(id);
        System.out.println("sysUserVo= "+ sysUserVo);
        return R.ok(sysUserVo);
    }

    /**
     * 新增用户
     * @param sysUserQuery 前端传过来的用户信息
     * @return 响应前端 o 不 ok
     */
    @PostMapping("/sysUser")
    public R addUser(SysUserQuery sysUserQuery, Authentication authentication) {
        //System.out.println("后端收到前端的新增用户请求");
        int count = sysUserService.addUser(sysUserQuery,authentication);
        return count >= 2 ? R.ok() : R.fail();
    }

    /**
     * 编辑用户
     * @param sysUserQuery 前端传过来的用户信息
     * @return 响应前端 o 不 ok
     */
    @PutMapping("/sysUser")
    public R editUser(SysUserQuery sysUserQuery,Authentication authentication) {
        //System.out.println("后端收到前端的编辑用户请求");
        int count = sysUserService.editUser(sysUserQuery,authentication);
        return count >= 1 ? R.ok() : R.fail();
    }
    /**
     * 删除用户
     * @param id 前端传过来的指定 id 的用户
     * @return 响应前端 o 不 ok
     */
    @DeleteMapping("/sysUser/{id}")
    public R delUser(@PathVariable("id") Long id) {
        int count = sysUserService.delUserById(id);
        return count >= 2 ? R.ok() : R.fail();
    }

    /**
     * 批量删除用户
     * @param ids id 字符串，类似 "2,4,5"
     * @return 响应前端 o 不 ok
     */
    @DeleteMapping("/sysUsers")
    public R batchDelUser(@RequestParam(value = "ids", required = false) String ids) {
        int count = sysUserService.delUserByIds(ids);
        // System.out.println(ids);
        int len = ids.split(",").length;
        return count >= 2 * len ? R.ok() : R.fail();
    }
}
