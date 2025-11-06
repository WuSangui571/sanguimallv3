package com.sangui.sanguimall.admin.web;


import com.github.pagehelper.PageInfo;
import com.sangui.sanguimall.admin.model.query.SysRoleQuery;
import com.sangui.sanguimall.admin.model.vo.SysRoleDetailVo;
import com.sangui.sanguimall.admin.model.vo.SysRoleVo;
import com.sangui.sanguimall.admin.service.SysRoleService;
import com.sangui.sanguimall.result.R;
import jakarta.annotation.Resource;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: sangui
 * @CreateTime: 2025-11-04
 * @Description:
 * @Version: 1.0
 */
@RestController
@RequestMapping("/sysRole")
public class SysRoleController {
    @Resource
    SysRoleService sysRoleService;

    @GetMapping("/roles")
    public R getRoles(){
        List<SysRoleVo> sysRoleVoList = sysRoleService.getAllRoles();
        return R.ok(sysRoleVoList);
    }


    /**
     * 获取全部角色的信息
     * @param current 当前页数
     * @return 当前页数的角色信息
     */
    @GetMapping("/sysRoles")
    public R getRolesByPage(@RequestParam(value = "current",required = false)Integer current){
        if (current == null){
            current = 1;
        }
        PageInfo<SysRoleDetailVo> pageInfo = sysRoleService.getRolesByPage(current);

        return R.ok(pageInfo);
    }

    /**
     * 响应给前端指定 id 的角色信息
     * @param id 角色 id
     * @return 角色信息
     */
    @GetMapping("/sysRole/{id}")
    public R getRoleDetailByRoleId(@PathVariable("id")Long id){
        SysRoleDetailVo sysRoleDetailVo = sysRoleService.getRoleDetailByRoleId(id);
        return R.ok(sysRoleDetailVo);
    }

    @PostMapping("/sysRole")
    public R addRole(SysRoleQuery sysRoleQuery, Authentication authentication) {
        int count = sysRoleService.addRole(sysRoleQuery,authentication);
        return count >= 1 ? R.ok() : R.fail();
    }

    /**
     * 编辑角色
     * @param sysRoleQuery 前端传过来的用户信息
     * @return 响应前端 o 不 ok
     */
    @PutMapping("/sysRole")
    public R editRole(SysRoleQuery sysRoleQuery,Authentication authentication) {
        int count = sysRoleService.editRole(sysRoleQuery,authentication);
        return count >= 1 ? R.ok() : R.fail();
    }

    /**
     * 删除角色
     * @param id 前端传过来的指定 id 的角色
     * @return 响应前端 o 不 ok
     */
    @DeleteMapping("/sysRole/{id}")
    public R delRoleById(@PathVariable("id") Long id) {
        int count = sysRoleService.delRoleById(id);
        return count >= 1 ? R.ok() : R.fail();
    }
}
