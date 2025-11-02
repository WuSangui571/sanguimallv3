<template>
  <el-button type="success" @click="goBack">返回</el-button>
  <el-form :model="userDetail" class="my-form" label-width="130px">
    <el-form-item label="ID">
      <div class="div-item">
        <!-- 写上 &nbsp; 是为了防止后面的数据未空，div 里空值-->
        &nbsp;{{ userDetail.userId }}
      </div>
    </el-form-item>

    <el-form-item label="账户">
      <div class="div-item">
        &nbsp;{{ userDetail.username }}
      </div>
    </el-form-item>

    <el-form-item label="密码">
      <div class="div-item">
        <!--密码直接写死-->
        &nbsp;******
      </div>
    </el-form-item>

    <el-form-item label="手机">
      <div class="div-item">
        &nbsp;{{ userDetail.mobile }}
      </div>
    </el-form-item>

    <el-form-item label="邮箱">
      <div class="div-item">
        &nbsp;{{ userDetail.email }}
      </div>
    </el-form-item>


    <el-form-item label="账户是否启用">
      <div class="div-item">
        &nbsp;{{ userDetail.status == '1' ? '启用中' : '未启用' }}
      </div>
    </el-form-item>

    <el-form-item label="账户创建时间">
      <div class="div-item">
        &nbsp;{{ userDetail.createTime }}
      </div>
    </el-form-item>

    <el-form-item label="创建人">
      <div class="div-item">
        &nbsp;{{ userDetail.createByDo.username }}
      </div>
    </el-form-item>
  </el-form>
</template>

<script>
import {doGet} from "../../http/HttpRequest.js";

export default {
  name: "SysUserDetailView",
  data() {
    return {
      userDetail: {
        userId: '',
        username: '',
        password: '',
        mobile: '',
        email: '',
        status:'',
        createTime: '',
        createUserId: '',
        createByDo: {
          userId: '',
          username: '',
        },
      },
    }
  },
  mounted() {
    this.getData();
  },
  methods: {
    getData() {
      // 获取 id。这里的 params 后面的 id 的名称，要与动态路由里设置的动态名称一样
      let id = this.$route.params.id
      let url = "/api/admin/sysUser/" + id
      doGet(url, {}).then(resp => {
        console.log(resp.data.data);
        if (resp.data.code === 200) {

          this.userDetail = resp.data.data;
        }
      })
    },
    goBack() {
      this.$router.go(-1);
    },
  },
}
</script>

<style scoped>
/*设置整个表格的样式*/
.my-form {
  /*设置左边的间距*/
  padding-left: 30px;
}
/*设置每个菜单项的值的样式*/
.div-item {
  /*设置背景色*/
  background-color: azure;
  /*设置宽度比例*/
  width: 100%;
  /*设置左边的间距*/
  padding-left: 15px;
}
</style>