<template>
  <el-container>
    <!--左侧导航栏开始-->
    <el-aside :width="isCollapse ? '72px' : '220px'">
      <div class="menu-title">
        <div class="brand" v-if="!isCollapse">
          <el-icon class="brand-icon">
            <Grid/>
          </el-icon>
          <span class="brand-name">Sanguimall</span>
        </div>
        <el-tooltip :content="isCollapse ? '展开导航' : '折叠导航'" placement="right">
          <button type="button" class="collapse-btn" @click="foldLeftSide">
            <el-icon>
              <component :is="isCollapse ? Expand : Fold"/>
            </el-icon>
          </button>
        </el-tooltip>
      </div>
      <!--侧导航条-->
      <el-menu
          class="el-menu-vertical-demo"
          :unique-opened="true"
          :collapse="isCollapse"
          :collapse-transition="false"
          :default-active="currentRouterPath"
          :router="true">
        <el-menu-item index="/dashboard/index">
          <el-icon>
            <HomeFilled/>
          </el-icon>
          <span>系统首页</span>
        </el-menu-item>
        <el-sub-menu index="3">
          <template #title>
            <el-icon>
              <Box/>
            </el-icon>
            <span>商品系统</span>
          </template>
          <el-menu-item index="/dashboard/product/category">
            <el-icon>
              <Menu/>
            </el-icon>
            分类维护
          </el-menu-item>
          <el-menu-item index="/dashboard/product/brand">
            <el-icon>
              <CollectionTag/>
            </el-icon>
            品牌管理
          </el-menu-item>
          <el-sub-menu index="3-3">
            <template #title>
              <el-icon>
                <Monitor/>
              </el-icon>
              平台属性
            </template>
            <el-menu-item index="/dashboard/product/attr/group">
              属性分组
            </el-menu-item>
            <el-menu-item index="/dashboard/product/attr/attr">
              规格参数
            </el-menu-item>
            <el-menu-item index="3-3-3">
              销售属性
            </el-menu-item>
          </el-sub-menu>
          <el-sub-menu index="3-4">
            <template #title>
              <el-icon>
                <Management/>
              </el-icon>
              商品维护
            </template>
            <el-menu-item index="3-4-1">spu维护</el-menu-item>
            <el-menu-item index="3-4-2">发表商品</el-menu-item>
            <el-menu-item index="3-4-3">商品管理</el-menu-item>
          </el-sub-menu>
        </el-sub-menu>
        <el-sub-menu index="4">
          <template #title>
            <el-icon>
              <UserFilled/>
            </el-icon>
            <span>用户管理</span>
          </template>
          <el-menu-item index="/dashboard/admin/sysUsers">
            <el-icon>
              <Grid/>
            </el-icon>
            所有用户
          </el-menu-item>
          <el-menu-item :index='personalUrl'>
            <el-icon>
              <User/>
            </el-icon>
            个人信息
          </el-menu-item>
          <el-menu-item index='/dashboard/admin/roles'>
            <el-icon>
              <Avatar/>
            </el-icon>
            角色管理
          </el-menu-item>
        </el-sub-menu>

        <el-sub-menu index="5">
          <template #title>
            <el-icon>
              <Avatar />
            </el-icon>
            <span>会员管理</span>
          </template>
          <el-menu-item index="/dashboard/member/list">
            <el-icon>
              <Grid/>
            </el-icon>
            会员列表
          </el-menu-item>
          <el-menu-item index="/dashboard/member/level">
            <el-icon>
              <Histogram />
            </el-icon>
            会员等级
          </el-menu-item>
          <el-menu-item index="/dashboard/member/points">
            <el-icon>
              <TrendCharts />
            </el-icon>
            积分变化
          </el-menu-item>
        </el-sub-menu>

      </el-menu>
    </el-aside>
    <!--左侧导航栏结束-->
    <!--右侧三栏开始-->
    <el-container class="right-side">
      <!--上导航条开始-->
      <el-header>
        <div class="nav-bar">
          <div class="nav-left">
            <el-breadcrumb separator="/" class="nav-breadcrumb">
              <el-breadcrumb-item to="/dashboard/index">首页</el-breadcrumb-item>
              <el-breadcrumb-item
                  v-for="(item, index) in breadcrumbItems"
                  :key="index"
                  :to="item.to">
                {{ item.label }}
              </el-breadcrumb-item>
            </el-breadcrumb>
          </div>
          <div class="nav-actions">
            <div class="theme-switch">
              <el-switch
                  v-model="isDark"
                  :active-text="'暗色'"
                  :inactive-text="'亮色'"
                  inline-prompt
                  @change="toggleTheme"
              />
            </div>
            <el-dropdown :hide-on-click="false">
              <span class="el-dropdown-link">
                {{ user.username }}
                <el-icon class="el-icon--right"><arrow-down/></el-icon>
              </span>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item @click="gotoMyInfo">我的资料</el-dropdown-item>
                  <el-dropdown-item divided @click="logout">退出登录</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
        </div>
      </el-header>
      <!--上导航条结束-->
      <!--主区域开始-->
      <el-main>
        <router-view v-if="isRouterAlive"/>
      </el-main>
      <!--主区域结束-->
      <!--底部版权信息条开始-->
      <el-footer>
        Copyright © 2025 sanguimall All rights reserved. 站点地图 浙ICP备xxxx号
      </el-footer>
      <!--底部版权信息条结束-->
    </el-container>
    <!--右侧三栏结束-->
  </el-container>
</template>

<script>
import {defineComponent} from 'vue'
import {doGet} from "../http/HttpRequest.js";
import {messageConfirm, messageTip, removeToken} from "../util/util.js";

export default defineComponent({
  name: "DashboardView",
  data() {
    return {
      isCollapse: false,
      isDark: false,
      user: {
        username: "",
      },
      currentRouterPath: "",
      isRouterAlive: true,
      personalUrl: '',
      breadcrumbConfig: {
        "/dashboard/index": [{label: "系统首页"}],
        "/dashboard/product/category": [
          {label: "商品系统", to: "/dashboard/product/category"},
          {label: "分类维护"}
        ],
        "/dashboard/product/brand": [
          {label: "商品系统", to: "/dashboard/product/brand"},
          {label: "品牌管理"}
        ],
        "/dashboard/product/attr/group": [
          {label: "商品系统", to: "/dashboard/product/attr/group"},
          {label: "平台属性", to: "/dashboard/product/attr/group"},
          {label: "属性分组"}
        ],
        "/dashboard/product/attr/attr": [
          {label: "商品系统", to: "/dashboard/product/attr/attr"},
          {label: "平台属性", to: "/dashboard/product/attr/attr"},
          {label: "规格参数"}
        ],
        "/dashboard/admin/sysUsers": [
          {label: "用户管理", to: "/dashboard/admin/sysUsers"},
          {label: "所有用户"}
        ],
        "/dashboard/admin/roles": [
          {label: "用户管理", to: "/dashboard/admin/roles"},
          {label: "角色管理"}
        ],
        "/dashboard/member/list": [
          {label: "会员管理", to: "/dashboard/member/list"},
          {label: "会员列表"}
        ],
        "/dashboard/member/level": [
          {label: "会员管理", to: "/dashboard/member/level"},
          {label: "会员等级"}
        ],
        "/dashboard/member/points": [
          {label: "会员管理", to: "/dashboard/member/points"},
          {label: "积分变化"}
        ],
      },
    }
  },
  provide() {
    return {
      reload: () => {
        this.isRouterAlive = false;
        this.$nextTick(function () {
          this.isRouterAlive = true;
        })
      }
    }
  },
  computed: {
    breadcrumbItems() {
      return this.buildBreadcrumb(this.$route.path);
    }
  },
  watch: {
    $route: {
      immediate: true,
      handler() {
        this.syncRouteState();
      }
    },
    personalUrl() {
      this.syncRouteState();
    }
  },
  mounted() {
    this.loadLoginUser();
    this.initTheme();
  },
  methods: {

    // 退出登录的方法
    logout() {
      doGet("/api/admin/sysUser/logout", {}).then((resp) => {
        // 看看响应的形式是怎么样的
        console.log(resp);
        if (resp.data.code === 200) {
          messageTip("退出成功！", "success");
          removeToken();
          window.location.href = "/";
        } else {
          messageConfirm("退出异常，是否强制退出？", "温馨提示").then(() => {
            removeToken();
            window.location.href = "/";
          }).catch(() => {
            // 用户点击取消就会触发 catch 里
            messageTip("取消强制退出", "warning")
          })
        }
      })
    },
    loadLoginUser() {
      // 发送给后端请求，请求当前登录用户
      doGet("/api/admin/sysUser/info", {}).then((resp) => {
        // 看看响应的形式是怎么样的
        console.log(resp);
        // console.log(resp.data.data.name);
        this.user = resp.data.data;
        this.personalUrl = "/dashboard/admin/sysUser/" + resp.data.data.userId;
        console.log(this.personalUrl);
      })
    },
    gotoMyInfo() {
      this.$router.push(this.personalUrl)
    },
    // 折叠左侧菜单的方法
    foldLeftSide() {
      this.isCollapse = !this.isCollapse;
    },
    syncRouteState() {
      this.currentRouterPath = this.resolveMenuPath(this.$route.path);
    },
    resolveMenuPath(path) {
      const menuIndexes = new Set([
        "/dashboard/index",
        "/dashboard/product/category",
        "/dashboard/product/brand",
        "/dashboard/product/attr/group",
        "/dashboard/product/attr/attr",
        "/dashboard/admin/sysUsers",
        "/dashboard/admin/roles",
        "/dashboard/member/list",
        "/dashboard/member/level",
        "/dashboard/member/points",
      ]);
      if (this.personalUrl && path.startsWith(this.personalUrl)) {
        return this.personalUrl;
      }
      if (path.startsWith("/dashboard/admin/sysUser/")) {
        return "/dashboard/admin/sysUsers";
      }
      if (path.startsWith("/dashboard/admin/role/")) {
        return "/dashboard/admin/roles";
      }
      const parts = path.split("/").filter(Boolean);
      for (let len = parts.length; len >= 2; len--) {
        const candidate = "/" + parts.slice(0, len).join("/");
        if (menuIndexes.has(candidate)) {
          return candidate;
        }
      }
      return "/dashboard/index";
    },
    buildBreadcrumb(path) {
      if (this.personalUrl && path.startsWith(this.personalUrl)) {
        return [
          {label: "用户管理", to: "/dashboard/admin/sysUsers"},
          {label: "个人信息", to: this.personalUrl},
        ];
      }
      if (path.startsWith("/dashboard/admin/sysUser/")) {
        return [
          {label: "用户管理", to: "/dashboard/admin/sysUsers"},
          {label: "所有用户", to: "/dashboard/admin/sysUsers"},
          {label: "用户详情"},
        ];
      }
      if (path.startsWith("/dashboard/admin/role/")) {
        return [
          {label: "用户管理", to: "/dashboard/admin/sysUsers"},
          {label: "角色管理", to: "/dashboard/admin/roles"},
          {label: "角色详情"},
        ];
      }
      const map = this.breadcrumbConfig;
      const parts = path.split("/").filter(Boolean);
      for (let len = parts.length; len >= 2; len--) {
        const candidate = "/" + parts.slice(0, len).join("/");
        if (map[candidate]) {
          return map[candidate];
        }
      }
      return [{label: "系统首页"}];
    },
    initTheme() {
      const saved = localStorage.getItem("sanguimall-theme");
      this.isDark = saved ? saved === "dark" : false;
      this.applyTheme(this.isDark);
    },
    toggleTheme(val) {
      this.applyTheme(val);
      localStorage.setItem("sanguimall-theme", val ? "dark" : "light");
    },
    applyTheme(isDark) {
      const mode = isDark ? "dark" : "light";
      document.documentElement.setAttribute("data-theme", mode);
    },
  },
})
</script>

<style scoped>
/* 顶部导航条 */
.el-header {
  background-color: var(--bg-header);
  height: 56px;
  line-height: 56px;
  border-bottom: 1px solid var(--border-color);
  display: flex;
  align-items: center;
  padding: 0 18px;
  box-sizing: border-box;
}

/* ??????????*/
.el-footer {
  background-color: var(--bg-header);
  height: 35px;
  line-height: 35px;
  text-align: center;
  border-top: 1px solid var(--border-color);
}

/* ?????????*/
.right-side {
  height: calc(100vh);
  background: var(--bg-body);
}

/* 左侧品牌与折叠区域 */
.menu-title {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
  background-color: var(--bg-aside);
  height: 56px;
  padding: 0 14px;
  border-bottom: 1px solid var(--border-color);
  position: sticky;
  top: 0;
  z-index: 1;
  box-sizing: border-box;
}

.brand {
  display: inline-flex;
  align-items: center;
  gap: 10px;
  font-weight: 700;
  color: var(--text-primary);
  letter-spacing: 0.2px;
}

.brand-icon {
  background: rgba(79, 139, 255, 0.12);
  color: var(--accent);
  padding: 7px;
  border-radius: 12px;
  border: 1px solid rgba(79, 139, 255, 0.25);
}

.brand-name {
  font-size: 15px;
}

/* ???????*/
.el-menu {
  border-right: 0;
  padding: 6px 6px 10px;
}

/* ?????????????*/
.el-dropdown {
  float: right;
  line-height: 35px;
}

.collapse-btn {
  width: 34px;
  height: 34px;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  border-radius: 10px;
  border: 1px solid var(--border-color);
  background: var(--bg-header);
  color: var(--text-primary);
  transition: all 0.2s ease;
  cursor: pointer;
  box-shadow: none;
}

.collapse-btn:hover {
  background: var(--bg-table-header);
  transform: translateY(-1px);
  border-color: var(--border-color);
}

.nav-bar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
  height: 100%;
  width: 100%;
}

.nav-left {
  display: flex;
  align-items: center;
  min-width: 0;
}

.nav-actions {
  display: flex;
  align-items: center;
  gap: 12px;
}

.nav-breadcrumb {
  color: var(--text-secondary);
}

.nav-breadcrumb :deep(.el-breadcrumb__inner.is-link) {
  color: var(--text-secondary);
}
</style>
