<template>
  <el-container>
    <!--左侧导航栏开始-->
    <el-aside :width="isCollapse?'64px':'200px'">
      <div class="menu-title">
        @sanguimall 后台管理系统
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
        <el-menu-item index="/dashboard">
          <el-icon><HomeFilled /></el-icon>
          <span>系统首页</span>
        </el-menu-item>
        <el-sub-menu index="3">
          <template #title>
            <el-icon><Box /></el-icon>
            <span>商品系统</span>
          </template>
          <el-menu-item index="/dashboard/product/category">
            <el-icon><Menu /></el-icon>
            分类维护
          </el-menu-item>
          <el-menu-item index="3-2">
            <el-icon><CollectionTag /></el-icon>
            品牌管理
          </el-menu-item>
          <el-sub-menu index="3-3">
            <template #title>
              <el-icon><Monitor /></el-icon>
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
              <el-icon><Management /></el-icon>
              商品维护
            </template>
            <el-menu-item index="3-4-1">spu维护</el-menu-item>
            <el-menu-item index="3-4-2">发表商品</el-menu-item>
            <el-menu-item index="3-4-3">商品管理</el-menu-item>
          </el-sub-menu>
        </el-sub-menu>

      </el-menu>
    </el-aside>
    <!--左侧导航栏结束-->
    <!--右侧三栏开始-->
    <el-container class="right-side">
      <!--上导航条开始-->
      <el-header>
        <el-icon class="fold-icon" @click="foldLeftSide"><Fold /></el-icon>
        Header
      </el-header>
      <!--上导航条结束-->
      <!--主区域开始-->
      <el-main>
        <router-view/>
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
export default defineComponent({
  name: "DashboardView",
  data(){
    return {
      isCollapse: false,
      // 当前访问路径，默认为空
      currentRouterPath:"",
    }
  },
  mounted() {
    // 执行这个方法，获取当前路径地址
    this.loadCurrentRouterPath();
  },
  methods: {
    // 折叠左侧菜单的方法
    foldLeftSide(){
      this.isCollapse = !this.isCollapse;
    },
    // 获取当前路径地址
    loadCurrentRouterPath(){
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
.el-header{
  /* 设置背景颜色*/
  background-color: azure;
  /* 设置高度*/
  height: 35px;
  /* 设置行高与高度一致，即可上下居中*/
  line-height: 35px;
}
/* 底部版权信息条的样式*/
.el-footer{
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
.right-side{
  /* 设置高度是撑满*/
  height: calc(100vh);
}
/* 设置菜单标题的样式*/
.menu-title{
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
.el-menu{
  /* 发现菜单天然有个 1px 的右边框，设置无边框*/
  border-right: 0;
}
/* 设置折叠图标的样式*/
.fold-icon{
  /* 使鼠标悬停图标时，鼠标变成手*/
  cursor: pointer;
}
</style>