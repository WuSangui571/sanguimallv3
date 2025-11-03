package com.sangui.sanguimall.admin.model.entity;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.ObjectUtils;

/**
 * 系统用户
 * sys_user
 * @author sangui
 */
@Data
public class SysUser implements Serializable, UserDetails {
    private Long userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 盐
     */
    private String salt;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 状态  0：禁用   1：正常
     */
    private Byte status;

    /**
     * 创建者ID
     */
    private Long createUserId;

    /**
     * 创建时间
     */
    private Date createTime;

    @Serial
    private static final long serialVersionUID = 1L;

    // 角色 List
    private List<String> roleList;
    // 权限之中的按钮权限的权限 Code 的 List
    private List<String> buttonPermissionList;
    // 权限之中的菜单权限的 List
    private List<SysMenu> menuPermissionList;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> list = new ArrayList<GrantedAuthority>();
        // 角色
        if (!ObjectUtils.isEmpty(this.getRoleList())){
            this.getRoleList().forEach(role -> {
                list.add(new SimpleGrantedAuthority(role));
            });
        }

        // 权限标识符
        if (!ObjectUtils.isEmpty(this.getButtonPermissionList())){
            this.getButtonPermissionList().forEach(permission -> {
                list.add(new SimpleGrantedAuthority(permission));
            });
        }
        return list;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return this.getStatus() == 1;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return this.getStatus() == 1;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return this.getStatus() == 1;
    }

    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return this.getStatus() == 1;
    }

    // 两个一对一关联的属性
    private SysUser createByDo;
}