<template>
  <el-container>
    <!--左侧导航栏开始-->
    <el-aside :width="isCollapse?'64px':'200px'">
      <div class="menu-title">
        <el-icon class="fold-icon" @click="foldLeftSide">
          <Fold/>
        </el-icon>
      </div>
      <!--侧导航条-->
      <el-menu
          default-active="2"
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
          <el-menu-item index="3-2">
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
            <el-menu-item index="3-3-1">
              属性分组
            </el-menu-item>
            <el-menu-item index="3-3-2">
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
            <el-icon><UserFilled /></el-icon>
            <span>用户管理</span>
          </template>
          <el-menu-item index="/dashboard/admin/sysUsers">
            <el-icon><Grid /></el-icon>
            所有用户
          </el-menu-item>
          <el-menu-item :index='personalUrl'>
            <el-icon><User /></el-icon>
            个人信息
          </el-menu-item>
          <el-menu-item index='/dashboard/admin/roles'>
            <el-icon><Avatar /></el-icon>
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
        <el-dropdown :hide-on-click="false">
          <span class="el-dropdown-link">
<!--            此处动态获取用户姓名-->
            {{user.username}}
            <el-icon class="el-icon--right"><arrow-down/></el-icon>
          </span>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item>我的资料</el-dropdown-item>
              <el-dropdown-item>修改密码</el-dropdown-item>
              <el-dropdown-item divided @click="logout">退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
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
      user: {
        username: "",
      },
      // 当前访问路径，默认为空
      currentRouterPath: "",
      // 控制该区域页面内容是否显示
      isRouterAlive: true,
      personalUrl:'',
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
  },
  methods: {

    // 退出登录的方法
    logout(){
      doGet("/api/admin/sysUser/logout",{}).then((resp) =>{
        // 看看响应的形式是怎么样的
        console.log(resp);
        if (resp.data.code === 200){
          messageTip("退出成功！","success");
          removeToken();
          window.location.href = "/";
        }else{
          messageConfirm("退出异常，是否强制退出？","温馨提示").then(() =>{
            removeToken();
            window.location.href = "/";
          }).catch(() => {
            // 用户点击取消就会触发 catch 里
            messageTip("取消强制退出","warning")
          })
        }
      })
    },
    loadLoginUser(){
      // 发送给后端请求，请求当前登录用户
      doGet("/api/admin/sysUser/info",{}).then((resp) =>{
        // 看看响应的形式是怎么样的
        console.log(resp);
        // console.log(resp.data.data.name);
        this.user = resp.data.data;
        this.personalUrl = "/dashboard/admin/sysUser/" + resp.data.data.userId;
        console.log(this.personalUrl);
      })
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
      //alert(pathArr)
      let simplePath = '/' + pathArr[1] + '/' + pathArr[2];
      //alert(simplePath)
      this.currentRouterPath = simplePath;
    },
  },
})
</script>

<style scoped>
/* 设置上导航条的样式*/
.el-header {
  /* 设置背景颜色*/
  background-color: azure;
  /* 设置高度*/
  height: 35px;
  /* 设置行高与高度一致，即可上下居中*/
  line-height: 35px;
}

/* 底部版权信息条的样式*/
.el-footer {
  /* 设置背景颜色*/
  background-color: azure;
  /* 设置高度*/
  height: 35px;
  /* 设置行高与高度一致，即可上下居中*/
  line-height: 35px;
  /* 设置文本左右居中*/
  text-align: center;
}

/* 设置右侧三栏的样式*/
.right-side {
  /* 设置高度是撑满*/
  height: calc(100vh);
}

/* 设置菜单标题的样式*/
.menu-title {
  /* 设置背景颜色*/
  background-color: azure;
  /* 设置高度和右边一样*/
  height: 35px;
  /* 设置文本左右居中*/
  text-align: center;
  /* 设置行高与高度一致，即可上下居中*/
  line-height: 35px;
}

/* 设置菜单的样式*/
.el-menu {
  /* 发现菜单天然有个 1px 的右边框，设置无边框*/
  border-right: 0;
}
/* 设置用户姓名下拉列表的样式*/
.el-dropdown{
  /* 往右漂移，即放在最右边*/
  float: right;
  /* 设置行高与高度一致，即可上下居中*/
  line-height: 35px;
}
/* 设置折叠图标的样式*/
.fold-icon {
  /* 使鼠标悬停图标时，鼠标变成手*/
  cursor: pointer;
}
</style>