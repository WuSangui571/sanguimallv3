package com.sangui.sanguimall.constant;


/**
 * @Author: sangui
 * @CreateTime: 2025-10-29
 * @Description: 常量类
 * @Version: 1.0
 */
public class Constants {
    /**
     * 自定义的密钥，新项目中，修改自定义密钥
     */
    public static final String SECRET = "ysysljjmtdsxxx";
    // 后端验证登录的 URI 路径
    public static final String LOGIN_URI = "/sysUser/login";
    // 在 user 表中，登录账号的属性名
    public static final String NAME_OF_USERNAME_IN_USER = "username";
    // 在 user 表中，密码的属性名
    public static final String NAME_OF_PASSWORD_IN_USER = "password";

    // redis 的 key 的命名规范： 项目名:模块名:功能名:唯一业务参数(比如用户 id )
    public static final String REDIS_JWT_KEY = "sanguimall:sysuser:login:";

    // redis 中负责人的 key
    public static final String REDIS_OWNER_KEY = "sanguimall:sysuser:owner";

    // jwt过期时间 7 天
    public static final Long EXPIRE_TIME = 7 * 24 * 60 * 60L;

    // jwt 过期时间 30 分钟
    public static final Long DEFAULT_EXPIRE_TIME = 30 * 60L;

    //分页时每页显示 10 条数据
    public static final int PAGE_SIZE = 10;

    // 请求 token 的名称
    public static final String TOKEN_NAME = "Authorization";

    public static final String EMPTY = "";

    // 导出 Excel 的接口路径
    public static final String EXPORT_EXCEL_URI = "/api/exportExcel";

    public static final String EXCEL_FILE_NAME = "客户信息数据";

}
