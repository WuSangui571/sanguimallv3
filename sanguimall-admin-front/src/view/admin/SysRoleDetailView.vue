<template>
  <div class="detail-layout">
    <div class="toolbar">
      <el-button type="primary" plain @click="goBack">返回</el-button>
    </div>
    <el-form :model="roleDetail" class="my-form detail-card" label-width="130px">
      <div class="detail-header">
        <div>
          <div class="detail-title">角色详情</div>
          <div class="detail-subtitle">查看角色基础信息</div>
        </div>
        <div class="detail-meta">成员数：{{ roleDetail.userCount }}</div>
      </div>
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

      <el-form-item label="成员">
        <div class="div-item">
          <div v-if="roleDetail.userList && roleDetail.userList.length" class="member-list">
            <router-link
                v-for="user in roleDetail.userList"
                :key="user.userId"
                :to="`/dashboard/admin/sysUser/${user.userId}`"
                class="member-link">
              <el-tag class="member-tag" effect="plain" size="small">{{ user.username }}</el-tag>
            </router-link>
          </div>
          <div v-else class="member-empty">暂无成员</div>
        </div>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import {doGet} from "../../http/HttpRequest.js";

export default {
  name: "SysRoleDetailView",
  data() {
    return {
      roleDetail: {
        id: 0,
        roleId: 0,
        roleName: "",
        remark: "",
        userList: [
          {
            userId: 0,
            username: "",
          }
        ],
        userCount: 0,
      },
    }
  },
  methods: {
    getData() {
      // Get id from route params; the name must match the dynamic segment
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
.detail-layout {
  padding: 12px 14px 18px;
}

.detail-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
  margin-bottom: 12px;
  flex-wrap: wrap;
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

.detail-meta {
  font-size: 13px;
  color: var(--text-secondary);
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
  display: flex;
  align-items: center;
  justify-content: flex-start;
  background: var(--bg-card);
  border: 1px solid var(--border-color);
  border-radius: var(--radius-md);
  padding: 12px 12px;
  box-shadow: var(--shadow-soft);
  margin-bottom: 12px;
}

.member-list {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.member-link {
  text-decoration: none;
}

.member-tag {
  --el-tag-text-color: var(--text-primary);
  --el-tag-border-color: var(--border-color);
  background-color: var(--bg-header);
}

.member-empty {
  color: var(--text-secondary);
}
</style>
