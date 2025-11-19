<template>
  <div class="page-container member-history-page">
    <el-card class="content-card" shadow="hover">
      <div class="history-toolbar">
        <el-input
            v-model.trim="keyword"
            placeholder="用户名 / 昵称 / 手机 / 邮箱"
            clearable
            style="width: 300px"
            @keyup.enter="handleSearch"/>
        <el-button type="primary" @click="handleSearch">搜索</el-button>
        <el-button @click="resetSearch">重置</el-button>
      </div>

      <el-tabs v-model="activeTab" @tab-change="handleTabChange">
        <el-tab-pane label="积分变化" name="integration">
          <div class="summary-bar">
            <el-tag type="info" effect="plain">共 {{ state.integration.page.total }} 条记录</el-tag>
          </div>
          <el-table
              :data="state.integration.list"
              border
              stripe
              v-loading="state.integration.loading"
              style="width: 100%">
            <el-table-column type="index" label="#" width="60"/>
            <el-table-column label="会员" min-width="220">
              <template #default="{ row }">
                <div class="member-info">
                  <div class="member-name">{{ row.username || "-" }}</div>
                  <div class="member-sub">
                    {{ row.nickname || "-" }} · {{ row.mobile || "-" }}
                  </div>
                </div>
              </template>
            </el-table-column>
            <el-table-column label="变化值" width="140">
              <template #default="{ row }">
                <el-tag :type="formatChangeTag(row.changeCount)" effect="dark">
                  {{ formatChangeText(row.changeCount) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="note" label="备注" min-width="280">
              <template #default="{ row }">
                <el-text line-clamp="2">{{ row.note || "—" }}</el-text>
              </template>
            </el-table-column>
            <el-table-column label="来源" width="140">
              <template #default="{ row }">
                <el-tag effect="plain">{{ formatSource(row.sourceType) }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column label="记录时间" width="200">
              <template #default="{ row }">
                {{ row.createTimeText || "—" }}
              </template>
            </el-table-column>
          </el-table>
          <div class="pagination-bar">
            <el-pagination
                background
                layout="prev, pager, next"
                :page-size="pageSize"
                :current-page="state.integration.page.current"
                :total="state.integration.page.total"
                @current-change="val => handlePageChange('integration', val)"/>
          </div>
        </el-tab-pane>

        <el-tab-pane label="成长值变化" name="growth">
          <div class="summary-bar">
            <el-tag type="info" effect="plain">共 {{ state.growth.page.total }} 条记录</el-tag>
          </div>
          <el-table
              :data="state.growth.list"
              border
              stripe
              v-loading="state.growth.loading"
              style="width: 100%">
            <el-table-column type="index" label="#" width="60"/>
            <el-table-column label="会员" min-width="220">
              <template #default="{ row }">
                <div class="member-info">
                  <div class="member-name">{{ row.username || "-" }}</div>
                  <div class="member-sub">
                    {{ row.nickname || "-" }} · {{ row.mobile || "-" }}
                  </div>
                </div>
              </template>
            </el-table-column>
            <el-table-column label="变化值" width="140">
              <template #default="{ row }">
                <el-tag :type="formatChangeTag(row.changeCount)" effect="dark">
                  {{ formatChangeText(row.changeCount) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="note" label="备注" min-width="280">
              <template #default="{ row }">
                <el-text line-clamp="2">{{ row.note || "—" }}</el-text>
              </template>
            </el-table-column>
            <el-table-column label="来源" width="140">
              <template #default="{ row }">
                <el-tag effect="plain">{{ formatSource(row.sourceType) }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column label="记录时间" width="200">
              <template #default="{ row }">
                {{ row.createTimeText || "—" }}
              </template>
            </el-table-column>
          </el-table>
          <div class="pagination-bar">
            <el-pagination
                background
                layout="prev, pager, next"
                :page-size="pageSize"
                :current-page="state.growth.page.current"
                :total="state.growth.page.total"
                @current-change="val => handlePageChange('growth', val)"/>
          </div>
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>

<script setup>
import {onMounted, reactive, ref} from "vue";
import {doGet} from "../../http/HttpRequest.js";
import {messageTip} from "../../util/util.js";

const apiMap = {
  integration: "/api/member/history/integration",
  growth: "/api/member/history/growth",
};

const activeTab = ref("integration");
const keyword = ref("");
const pageSize = 10;

const createTableState = () => ({
  list: [],
  loading: false,
  page: {
    current: 1,
    total: 0,
  },
});

const state = reactive({
  integration: createTableState(),
  growth: createTableState(),
});

const handleTabChange = (name) => {
  const type = name || activeTab.value;
  loadHistory(type);
};

const handleSearch = () => {
  const type = activeTab.value;
  const target = state[type];
  if (!target) {
    return;
  }
  target.page.current = 1;
  loadHistory(type);
};

const resetSearch = () => {
  keyword.value = "";
  handleSearch();
};

const handlePageChange = (type, page) => {
  const target = state[type];
  if (!target) {
    return;
  }
  target.page.current = page;
  loadHistory(type);
};

const loadHistory = async (type) => {
  const target = state[type];
  if (!target) {
    return;
  }
  target.loading = true;
  try {
    const params = {
      current: target.page.current,
    };
    if (keyword.value) {
      params.keyword = keyword.value;
    }
    const resp = await doGet(apiMap[type], params);
    if (resp?.data?.code === 200) {
      const data = resp.data.data || {};
      target.list = Array.isArray(data.list) ? data.list : [];
      target.page.total = data.total || 0;
      target.page.current = data.pageNum || target.page.current;
    } else {
      target.list = [];
      messageTip(resp?.data?.msg || "查询失败", "error");
    }
  } catch (err) {
    messageTip("查询失败，请稍后重试", "error");
  } finally {
    target.loading = false;
  }
};

const formatChangeTag = (value) => (value >= 0 ? "success" : "danger");
const formatChangeText = (value) => {
  if (value === undefined || value === null) {
    return "0";
  }
  return value > 0 ? `+${value}` : String(value);
};
const formatSource = (value) => {
  switch (value) {
    case 0:
      return "购物";
    case 1:
      return "管理员修改";
    case 2:
      return "活动奖励";
    default:
      return "其他";
  }
};

onMounted(() => {
  loadHistory(activeTab.value);
});
</script>

<style scoped>
.history-toolbar {
  display: flex;
  gap: 12px;
  flex-wrap: wrap;
  align-items: center;
  margin-bottom: 16px;
}

.summary-bar {
  margin-bottom: 12px;
}

.member-info {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.member-name {
  font-weight: 600;
  color: var(--text-primary);
}

.member-sub {
  font-size: 12px;
  color: var(--text-secondary);
}

.pagination-bar {
  display: flex;
  justify-content: flex-end;
  padding: 16px 0 4px;
}
</style>
