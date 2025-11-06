package com.sangui.sanguimall.admin.manager.impl;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sangui.sanguimall.admin.manager.SysRoleManager;
import com.sangui.sanguimall.admin.mapper.SysRoleMapper;
import com.sangui.sanguimall.admin.mapper.SysUserMapper;
import com.sangui.sanguimall.admin.mapper.SysUserRoleMapper;
import com.sangui.sanguimall.admin.model.converter.SysUserConverter;
import com.sangui.sanguimall.admin.model.entity.SysRole;
import com.sangui.sanguimall.admin.model.entity.SysUser;
import com.sangui.sanguimall.admin.model.vo.SysRoleDetailVo;
import com.sangui.sanguimall.admin.model.vo.UserVoForRole;
import com.sangui.sanguimall.constant.Constants;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: sangui
 * @CreateTime: 2025-11-06
 * @Description:
 * @Version: 1.0
 */
@Service
public class SysRoleManagerImpl implements SysRoleManager {
    @Resource
    SysRoleMapper sysRoleMapper;

    @Resource
    SysUserMapper sysUserMapper;

    @Resource
    SysUserRoleMapper sysUserRoleMapper;

    @Resource
    SysUserConverter sysUserConverter;


    @Override
    public PageInfo<SysRoleDetailVo> getRolesByPage(Integer current) {
        // 1. 设置 PageHelper
        PageHelper.startPage(current, Constants.PAGE_SIZE);
        // 2. 查询
        List<SysRole> roleList = sysRoleMapper.selectAll();

        List<SysRoleDetailVo> list = new ArrayList<>();

        for (SysRole sysRole: roleList){
            // 查询所有该种 RoleId 的用户
            List<SysUser> thisTypeUserList = sysUserMapper.selectUsersByRoleId(sysRole.getRoleId());
            // 循环讲这些用户转化为 UserVoForRole ，待交给前端
            List<UserVoForRole> userVoList = new ArrayList<>();

                for (SysUser sysUser: thisTypeUserList){
                    userVoList.add(sysUserConverter.doToVo2(sysUser));
                }


            // 完善 SysRoleDetailVo 信息
            SysRoleDetailVo sysRoleDetailVo = new SysRoleDetailVo();
            sysRoleDetailVo.setId(sysRole.getRoleId());
            sysRoleDetailVo.setRoleName(sysRole.getRoleName());
            sysRoleDetailVo.setRemark(sysRole.getRemark());
            sysRoleDetailVo.setUserList(userVoList);
            long userCount = 0L;
            if (!userVoList.isEmpty()){
                userCount = userVoList.size();
            }
            sysRoleDetailVo.setUserCount(userCount);

            list.add(sysRoleDetailVo);
        }
        // 3. 封装分页数据到 PageInfo
        //System.out.println("myOutPutList=" + list);
        return new PageInfo<>(list);
    }

    @Override
    public SysRoleDetailVo getRoleDetailByRoleId(Long id) {

        SysRole sysRole = sysRoleMapper.selectByPrimaryKey(id);

        // 查询所有该种 RoleId 的用户
        List<SysUser> thisTypeUserList = sysUserMapper.selectUsersByRoleId(id);
        // 循环将这些用户转化为 UserVoForRole ，待交给前端
        List<UserVoForRole> userVoList = new ArrayList<>();
        for (SysUser sysUser: thisTypeUserList){
            userVoList.add(sysUserConverter.doToVo2(sysUser));
        }

        // 完善 SysRoleDetailVo 信息
        SysRoleDetailVo sysRoleDetailVo = new SysRoleDetailVo();
        sysRoleDetailVo.setId(id);
        sysRoleDetailVo.setRoleName(sysRole.getRoleName());
        sysRoleDetailVo.setRemark(sysRole.getRemark());
        sysRoleDetailVo.setUserList(userVoList);
        long userCount = 0L;
        if (!userVoList.isEmpty()){
            userCount = (long) userVoList.size();
        }
        sysRoleDetailVo.setUserCount(userCount);


        return sysRoleDetailVo;
    }

    @Override
    public int delRoleById(Long roleId) {
        List<SysRole> sysRoleList = sysUserRoleMapper.selectByRoleId(roleId);
        if (!sysRoleList.isEmpty()){
            return 0;
        }
        return sysRoleMapper.deleteByPrimaryKey(roleId);
    }
}
