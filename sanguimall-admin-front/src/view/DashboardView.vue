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

      </el-menu>
    </el-aside>
    <!--左侧导航栏结束-->
    <!--右侧三栏开始-->
    <el-container class="right-side">
      <!--上导航条开始-->
      <el-header>
        <div class="nav-bar">
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
      // 当前访问路径，默认为空
      currentRouterPath: "",
      // 控制该区域页面内容是否显示
      isRouterAlive: true,
      personalUrl: '',
      // 当前访问路径，默认为空
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
  mounted() {
    // 执行这个方法，获取当前路径地址
    this.loadCurrentRouterPath();
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
    // 获取当前路径地址
    loadCurrentRouterPath() {
      let path = this.$route.path;
      //alert(path)
      let pathArr = path.split("/");
      // alert(pathArr)
      // alert(pathArr[4])
      let simplePath
      if (pathArr[4] != undefined) {
        // alert("yes!value=" + pathArr[4])
        simplePath = '/' + pathArr[1] + '/' + pathArr[2] + '/' + pathArr[3] + '/' + pathArr[4];
      }else {
        // alert("no")
        simplePath = '/' + pathArr[1] + '/' + pathArr[2] + '/' + pathArr[3]
      }
      //alert(simplePath)
      this.currentRouterPath = simplePath;
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
/* ?????????*/
.el-header {
  background-color: var(--bg-header);
  height: 56px;
  line-height: 56px;
  border-bottom: 1px solid var(--border-color);
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

/* ????????? */
.menu-title {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
  background-color: var(--bg-aside);
  padding: 12px 14px;
  border-bottom: 1px solid var(--border-color);
  position: sticky;
  top: 0;
  z-index: 1;
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
  width: 38px;
  height: 38px;
  display: grid;
  place-items: center;
  border-radius: 12px;
  border: 1px solid var(--border-color);
  background: var(--bg-card);
  color: var(--text-primary);
  transition: all 0.2s ease;
  cursor: pointer;
  box-shadow: var(--shadow-soft);
}

.collapse-btn:hover {
  background: var(--bg-table-header);
  transform: translateY(-1px);
}

.nav-bar {
  display: flex;
  align-items: center;
  justify-content: flex-end;
  gap: 12px;
  height: 100%;
}
</style>
