package com.sangui.sanguimall.admin.web;


import com.sangui.sanguimall.admin.model.vo.SysRoleVo;
import com.sangui.sanguimall.admin.service.SysRoleService;
import com.sangui.sanguimall.result.R;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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


}
