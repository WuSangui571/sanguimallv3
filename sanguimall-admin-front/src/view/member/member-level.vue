<template>
  <div class="page-container">
    <div class="toolbar">
      <div class="toolbar-left">
        <el-button type="primary" plain @click="openCreate">
          新增等级
        </el-button>
      </div>
      <div class="toolbar-right">
        <el-tag effect="plain" type="info">共 {{ memberLevels.length }} 个等级</el-tag>
      </div>
    </div>

    <el-card class="content-card" shadow="hover">
      <div class="section-header">
        <div>
          <div class="section-title">会员等级</div>
          <div class="section-subtitle">基于 ums_member_level 表的配置，展示成长值门槛与享受的特权。</div>
        </div>
        <el-tag v-if="defaultLevelName" type="success" effect="dark">
          默认等级：{{ defaultLevelName }}
        </el-tag>
      </div>

      <el-table
          :data="memberLevels"
          border
          stripe
          v-loading="loading"
          style="width: 100%">
        <el-table-column type="index" label="序号" width="70"/>
        <el-table-column prop="name" label="等级名称" width="140"/>
        <el-table-column label="成长值门槛" width="150">
          <template #default="{ row }">
            <el-tag type="info">{{ formatGrowthPoint(row.growthPoint) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="默认等级" width="140">
          <template #default="{ row }">
            <el-tag :type="row.defaultLevel ? 'success' : 'warning'" effect="plain">
              {{ row.defaultLevel ? '默认启用' : '非默认' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="免邮门槛" width="150">
          <template #default="{ row }">
            <el-text>{{ formatAmount(row.freeFreightPoint) }}</el-text>
          </template>
        </el-table-column>
        <el-table-column label="评价成长值" width="160">
          <template #default="{ row }">
            <el-text>{{ formatCommentGrowth(row.commentGrowthPoint) }}</el-text>
          </template>
        </el-table-column>
        <el-table-column label="权益" min-width="260">
          <template #default="{ row }">
            <el-space wrap size="small">
              <template v-if="privilegeList(row).length">
                <el-tag
                    v-for="item in privilegeList(row)"
                    :key="item.key"
                    :type="item.type"
                    effect="plain">
                  {{ item.label }}
                </el-tag>
              </template>
              <el-text v-else type="info">无特权</el-text>
            </el-space>
          </template>
        </el-table-column>
        <el-table-column prop="note" label="备注" min-width="160">
          <template #default="{ row }">
            <el-text>{{ row.note || '—' }}</el-text>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="140">
          <template #default="{ row }">
            <el-button size="small" type="primary" plain @click="openEdit(row)">编辑</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="640px" draggable>
      <el-form
          ref="levelFormRef"
          :model="form"
          :rules="rules"
          label-width="120px">
        <el-form-item label="等级名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入等级名称"/>
        </el-form-item>
        <el-form-item label="成长值门槛" prop="growthPoint">
          <el-input-number v-model="form.growthPoint" :min="0" :max="999999" controls-position="right" style="width: 220px"/>
        </el-form-item>
        <el-form-item label="默认等级" prop="defaultLevel">
          <el-switch
              v-model="form.defaultLevel"
              :disabled="defaultSwitchDisabled"
              active-text="开"
              inactive-text="关"/>
        </el-form-item>
        <el-form-item label="免邮门槛" prop="freeFreightPoint">
          <el-input-number v-model="form.freeFreightPoint" :min="0" :max="999999" :precision="2" :step="1" controls-position="right" style="width: 220px"/>
        </el-form-item>
        <el-form-item label="评价成长值" prop="commentGrowthPoint">
          <el-input-number v-model="form.commentGrowthPoint" :min="0" :max="9999" controls-position="right" style="width: 220px"/>
        </el-form-item>
        <el-form-item label="特权">
          <el-space wrap size="small">
            <el-switch
                v-model="form.priviledgeFreeFreight"
                active-text="免邮特权"
                inactive-text="无免邮"/>
            <el-switch
                v-model="form.priviledgeMemberPrice"
                active-text="会员价特权"
                inactive-text="无会员价"/>
            <el-switch
                v-model="form.priviledgeBirthday"
                active-text="生日特权"
                inactive-text="无生日礼"/>
          </el-space>
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="form.note" type="textarea" rows="2" placeholder="备注信息"/>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="submitForm">确认</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import {computed, onMounted, ref} from "vue";
import {doGet, doPost, doPut} from "../../http/HttpRequest.js";
import {messageTip} from "../../util/util.js";

const loading = ref(false);
const submitLoading = ref(false);
const memberLevels = ref([]);
const dialogVisible = ref(false);
const levelFormRef = ref(null);
const form = ref(createDefaultForm());

const rules = {
  name: [{required: true, message: "请输入等级名称", trigger: "blur"}],
  growthPoint: [{required: true, message: "请输入成长值门槛", trigger: "change"}],
  commentGrowthPoint: [{required: true, message: "请输入评价成长值", trigger: "change"}],
};

const privilegeConfig = [
  {key: "priviledgeFreeFreight", label: "免邮特权", type: "success"},
  {key: "priviledgeMemberPrice", label: "会员价特权", type: "warning"},
  {key: "priviledgeBirthday", label: "生日特权", type: "info"},
];

const defaultLevelName = computed(() => {
  const level = memberLevels.value.find(item => item.defaultLevel);
  return level ? level.name : "";
});

const hasDefaultLevel = computed(() => memberLevels.value.some(item => item.defaultLevel));

const defaultSwitchDisabled = computed(() => {
  if (!hasDefaultLevel.value) {
    return false;
  }
  if (form.value.id) {
    const current = memberLevels.value.find(item => item.id === form.value.id);
    if (current && current.defaultLevel) {
      return false;
    }
  }
  return true;
});

const dialogTitle = computed(() => form.value.id ? "编辑会员等级" : "新增会员等级");

const formatAmount = (value) => {
  if (value === null || value === undefined) {
    return "—";
  }
  const numberValue = Number(value);
  if (Number.isNaN(numberValue)) {
    return value;
  }
  return new Intl.NumberFormat("zh-CN", {
    minimumFractionDigits: 2,
    maximumFractionDigits: 2,
  }).format(numberValue) + " 元";
};

const formatGrowthPoint = (value) => value === null || value === undefined ? "—" : `${value} 分`;
const formatCommentGrowth = (value) => value === null || value === undefined ? "—" : `+${value} 分/评价`;

const privilegeList = (row) => {
  const target = row || {};
  return privilegeConfig.filter(item => Boolean(target[item.key]));
};

const loadMemberLevels = async () => {
  loading.value = true;
  try {
    const resp = await doGet("/api/member/level/list", {});
    if (resp?.data?.code === 200) {
      memberLevels.value = resp.data.data || [];
      if (!memberLevels.value.length) {
        messageTip("暂无会员等级数据", "warning");
      }
    } else {
      messageTip(resp?.data?.msg ? `获取会员等级失败：${resp.data.msg}` : "获取会员等级失败", "error");
    }
  } catch (e) {
    messageTip("获取会员等级失败，请稍后重试", "error");
  } finally {
    loading.value = false;
  }
};

const openCreate = () => {
  form.value = createDefaultForm();
  dialogVisible.value = true;
  clearValidateNextTick();
};

const openEdit = (row) => {
  form.value = {
    id: row.id,
    name: row.name,
    growthPoint: row.growthPoint,
    defaultLevel: !!row.defaultLevel,
    freeFreightPoint: row.freeFreightPoint,
    commentGrowthPoint: row.commentGrowthPoint,
    priviledgeFreeFreight: !!row.priviledgeFreeFreight,
    priviledgeMemberPrice: !!row.priviledgeMemberPrice,
    priviledgeBirthday: !!row.priviledgeBirthday,
    note: row.note,
  };
  dialogVisible.value = true;
  clearValidateNextTick();
};

const clearValidateNextTick = () => {
  requestAnimationFrame(() => {
    if (levelFormRef.value) {
      levelFormRef.value.clearValidate();
    }
  });
};

const submitForm = () => {
  if (!levelFormRef.value) {
    return;
  }
  levelFormRef.value.validate(async (valid) => {
    if (!valid) {
      return;
    }
    submitLoading.value = true;
    try {
      const payload = toPayload(form.value);
      const isEdit = !!payload.id;
      const resp = isEdit
          ? await doPut("/api/member/level", payload)
          : await doPost("/api/member/level", payload);
      if (resp?.data?.code === 200) {
        messageTip(isEdit ? "编辑成功" : "新增成功", "success");
        dialogVisible.value = false;
        await loadMemberLevels();
      } else {
        messageTip(resp?.data?.msg ? resp.data.msg : "提交失败", "error");
      }
    } catch (e) {
      messageTip("提交失败，请稍后重试", "error");
    } finally {
      submitLoading.value = false;
    }
  });
};

const toPayload = (val) => ({
  ...val,
  defaultStatus: val.defaultLevel ? 1 : 0,
  priviledgeFreeFreight: val.priviledgeFreeFreight ? 1 : 0,
  priviledgeMemberPrice: val.priviledgeMemberPrice ? 1 : 0,
  priviledgeBirthday: val.priviledgeBirthday ? 1 : 0,
});

function createDefaultForm() {
  return {
    id: null,
    name: "",
    growthPoint: 0,
    defaultLevel: false,
    freeFreightPoint: 0,
    commentGrowthPoint: 0,
    priviledgeFreeFreight: false,
    priviledgeMemberPrice: false,
    priviledgeBirthday: false,
    note: "",
  };
}

onMounted(() => {
  loadMemberLevels();
});
</script>

<style scoped>
.toolbar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  background: var(--bg-card);
  border: 1px solid var(--border-color);
  border-radius: var(--radius-md);
  padding: 12px;
  box-shadow: var(--shadow-soft);
  margin-bottom: 12px;
}

.toolbar-left {
  display: flex;
  align-items: center;
  gap: 10px;
}

.section-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 12px;
}

.section-title {
  font-size: 18px;
  font-weight: 600;
  color: var(--text-primary);
}

.section-subtitle {
  color: var(--text-secondary);
  margin-top: 4px;
  font-size: 13px;
}
</style>
