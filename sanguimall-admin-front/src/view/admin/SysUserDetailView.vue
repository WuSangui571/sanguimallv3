<template>
  <div class="detail-layout">
    <div class="toolbar">
      <el-button type="primary" plain @click="goBack">返回</el-button>
    </div>
    <el-form :model="userDetail" class="my-form detail-card" label-width="130px">
      <div class="detail-header">
        <div>
          <div class="detail-title">用户详情</div>
          <div class="detail-subtitle">查看账号基础信息</div>
        </div>
      </div>
      <el-form-item label="ID">
        <div class="div-item">
          &nbsp;{{ userDetail.id }}
        </div>
      </el-form-item>

      <el-form-item label="账户">
        <div class="div-item">
          &nbsp;{{ userDetail.username }}
        </div>
      </el-form-item>

      <el-form-item label="密码">
        <div class="div-item">
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

      <el-form-item label="角色">
        <div class="div-item">
          &nbsp;{{ userDetail.roleVo.typeValue }}
        </div>
      </el-form-item>

      <el-form-item label="账户是否启用">
        <div class="div-item">
          &nbsp;{{ userDetail.status == '1' ? '启用' : '未启用' }}
        </div>
      </el-form-item>

      <el-form-item label="账户创建时间">
        <div class="div-item">
          &nbsp;{{ userDetail.createTime }}
        </div>
      </el-form-item>

      <el-form-item label="创建人">
        <div class="div-item">
          &nbsp;{{ userDetail.createByVo.username }}
        </div>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import {doGet} from "../../http/HttpRequest.js";

export default {
  name: "SysUserDetailView",
  data() {
    return {
      userDetail: {
        id: '',
        username: '',
        password: '',
        mobile: '',
        email: '',
        status: '',
        createTime: '',
        createUserId: '',
        createByVo: {
          id: '',
          username: '',
        },
        roleVo: {
          id: '',
          typeValue: "",
        }
      },
    }
  },
  mounted() {
    this.getData();
  },
  methods: {
    getData() {
      // 获取 id。这里的 params 后面�?id 的名称，要与动态路由里设置的动态名称一�?
      let id = this.$route.params.id
      let url = "/api/admin/sysUser/" + id
      doGet(url, {}).then(resp => {
        console.log("this下面")
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
.detail-layout {
  padding: 12px 14px 18px;
}

.detail-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
  margin-bottom: 12px;
}

.detail-title {
  font-size: 20px;
  font-weight: 700;
  color: var(--text-primary);
}

.detail-subtitle {
  font-size: 13px;
  color: var(--text-secondary);
  margin-top: 2px;
}

.my-form {
  padding: 4px 6px 2px;
}

.detail-card {
  background: var(--bg-card);
  border: 1px solid var(--border-color);
  border-radius: var(--radius-lg);
  box-shadow: var(--shadow-soft);
  padding: 12px 16px 4px;
  color: var(--text-primary);
}

.my-form :deep(.el-form-item__label) {
  color: var(--text-secondary);
}

.div-item {
  background-color: var(--bg-aside);
  color: var(--text-primary);
  width: 100%;
  padding: 10px 12px;
  border-radius: var(--radius-sm);
  border: 1px solid var(--border-color);
  line-height: 1.7;
}

.toolbar {
  justify-content: flex-start;
  background: var(--bg-card);
  border: 1px solid var(--border-color);
  border-radius: var(--radius-md);
  padding: 12px 12px;
  box-shadow: var(--shadow-soft);
  margin-bottom: 12px;
}
</style>
