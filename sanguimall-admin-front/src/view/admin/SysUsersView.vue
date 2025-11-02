<template>
  <!--两个按钮-->
  <el-button type="primary">添加用户</el-button>
  <el-button type="danger">批量删除</el-button>
  <!--表格开始-->
  <el-table
      :data="sysUserList"
      style="width: 100%"
      @selection-change="handleSelectionChange">

    <el-table-column type="selection" :selectable="selectable" width="60"/>
    <!--若 type 为 id，则该字段会自动增长-->
    <el-table-column type="index" label="序号" width="100"/>
    <el-table-column property="username" label="账号" width="180"/>
    <el-table-column property="email" label="邮箱" width="200"/>
    <el-table-column property="mobile" label="手机" width="240"/>
    <el-table-column label="操作">
      <template #default="scope">
        <el-button type="primary" @click="view(scope.row.userId)">详情</el-button>
        <el-button type="warning">编辑</el-button>
        <el-button type="danger">删除</el-button>
      </template>
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
      @next-click="toPage" />
</template>

<script>
import {defineComponent} from "vue";
import {doGet} from "../../http/HttpRequest.js";

export default defineComponent({
  name: "SysUsersView",
  data() {
    return {
      // 定义 List 对象
      sysUserList: [{}],
      myPageSize: 0,
      myTotal: 0,
    }
  },
  methods: {
    // 跳转到指定 id 的用户信息界面
    view(id) {
      let url = "/dashboard/admin/sysUser/" + id
      // alert(url)
      this.$router.push(url)
    },
    toPage(current){
      this.getData(current)
    },
    // 勾选或者取消勾选时触发该函数
    handleSelectionChange() {
      // 完成批量删除模块功能时再写这个方法
    },
    // 查询用户列表数据
    getData(current) {
      doGet("/api/admin/sysUser/sysUsers", {
        // 当前页
        current: current
      }).then(resp => {
        console.log(resp)
        if (resp.data.code === 200) {
          this.sysUserList = resp.data.data.list;
          this.myTotal = resp.data.data.total;
          this.myPageSize = resp.data.data.pageSize;
        }
      })
    },
  },
  mounted() {
    this.getData(1);
  },

})

</script>

<style scoped>
.el-table {
  margin-top: 15px;
}
.el-pagination{
  margin-top: 20px;
}
</style>