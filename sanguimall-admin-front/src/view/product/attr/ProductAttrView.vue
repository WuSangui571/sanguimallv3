<template>
  <!--两个按钮-->
  <el-button type="primary">添加用户</el-button>
  <el-button type="danger">批量删除</el-button>
  <!--表格开始-->
  <el-table
      :data="attrList"
      style="width: 100%"
      @selection-change="handleSelectionChange">
    <el-table-column type="selection"  width="60"/>
    <!--若 type 为 id，则该字段会自动增长-->
    <el-table-column type="index" label="序号" width="60"/>
    <el-table-column property="attrName" label="属性名" width="120"/>
    <el-table-column property="searchType" label="是否需要检索" width="120"/>
    <el-table-column property="valueType" label="值类型[0-为单个值，1-可以选择多个值]" width="120"/>
    <el-table-column property="icon" label="属性图标" width="240"/>
    <el-table-column property="value_select" label="可选值列表[用逗号分隔]"/>
    <el-table-column property="attrType" label="属性类型"/>
    <el-table-column property="enable" label="启用状态"/>
    <el-table-column property="catelogId" label="所属分类"/>
    <el-table-column property="showDesc" label="快速展示"/>
    <el-table-column label="操作">
      <el-button type="primary">详情</el-button>
      <el-button type="warning">编辑</el-button>
      <el-button type="danger">删除</el-button>
    </el-table-column>
  </el-table>
  <!--表格结束-->
  <el-pagination
      background
      layout="prev, pager, next"
      :page-size=myPageSize
      :total=myTotal
      @prev-click="toPage"
      @current-change="toPage"
      @next-click="toPage"/>
</template>

<script>
import {doGet} from "../../../http/HttpRequest.js";

export default {
  name: "ProductAttrView",
  data() {
    return {
      // 定义 List 对象
      attrList: [{
        attrId:0,
        attrName:"",
        searchType:"",
        valueType:"",
        icon:"",
        valueSelect:"",
        attrType:"",
        enable:"",
        catelogId:"",
        showDesc:"",
      }],
      myPageSize: 0,
      myTotal: 0,
    }
  },
  methods:{
    // 勾选或者取消勾选时触发该函数
    handleSelectionChange(){
      // 完成批量删除模块功能时再写这个方法
    },
    // 查询用户列表数据
    getData(current){
      doGet("/api/users",{
        // 当前页
        current: current
      }).then(resp => {
        if (resp.data.code === 200){
          // console.log(resp)
          this.attrList = resp.data.data.list;
          this.myTotal = resp.data.data.total;
          this.myPageSize = resp.data.data.pageSize;
        }
      })
    },
    toPage(current){
      this.getData(current)
    },
  },
  mounted(){
    this.getData(1);
  },
}
</script>

<style scoped>
.el-table {
  margin-top: 15px;
}
.el-pagination{
  margin-top: 20px;
}
</style>