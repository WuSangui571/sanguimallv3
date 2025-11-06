<template>
  <el-button type="success" @click="goBack">返回</el-button>
  <el-form :model="roleDetail" class="my-form" label-width="130px">
    <el-form-item label="角色名称">
      <div class="div-item">
        &nbsp;{{ roleDetail.roleName }}
      </div>
    </el-form-item>

    <el-form-item label="角色标记">
      <div class="div-item">
        &nbsp;{{ roleDetail.remark }}
      </div>
    </el-form-item>

    <el-form-item label="人数">
      <div class="div-item">
        &nbsp;{{ roleDetail.userCount }}
      </div>
    </el-form-item>

    <el-form-item label="成员">
      <div class="div-item">
        &nbsp;
        <span v-for="(user, index) in roleDetail.userList" :key="user.userId">

      <router-link :to="`/dashboard/admin/sysUser/${user.userId}`">
          <el-button
              key="plain"
              type=""
              text>
            {{ user.username }}
          </el-button>
        </router-link>
        <span v-if="index < roleDetail.userList.length - 1">, </span>
    </span>
      </div>
    </el-form-item>
  </el-form>
</template>

<script>
import {doGet} from "../../http/HttpRequest.js";

export default {
  name: "SysRoleDetailView",
  data() {
    return {
      roleDetail:
          {
            id: 0,
            roleId: 0,
            roleName: "",
            remark: "",
            userList: [{
              userId: 0,
              username: "",
            }],
            userCount: 0,
          }
      ,
    }
  },
  methods: {
    getData() {
      // 获取 id。这里的 params 后面的 id 的名称，要与动态路由里设置的动态名称一样
      let id = this.$route.params.id
      let url = "/api/admin/sysRole/sysRole/" + id
      doGet(url, {}).then(resp => {
        if (resp.data.code === 200) {
          console.log(resp.data.data);
          this.roleDetail = resp.data.data;
        }
      })
    },
    goBack() {
      this.$router.go(-1);
    },
  },
  mounted() {
    this.getData();
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