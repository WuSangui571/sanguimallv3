<template>
  <div class="page-container member-page">
    <div class="toolbar">
      <div class="toolbar-actions">
        <el-button type="primary" plain @click="openCreate">新增会员</el-button>
        <el-button plain @click="reload">刷新</el-button>
      </div>
      <el-form :inline="true" :model="searchForm" class="toolbar-form">
        <el-form-item>
          <el-input
              v-model.trim="searchForm.keyword"
              placeholder="用户名 / 昵称 / 手机 / 邮箱"
              clearable
              style="width: 260px"
              @keyup.enter="handleSearch"/>
        </el-form-item>
        <el-form-item>
          <el-select
              v-model="searchForm.levelId"
              placeholder="会员等级"
              clearable
              style="width: 180px"
              @change="handleFilterChange">
            <el-option label="全部等级" :value="''"/>
            <el-option
                v-for="item in levelOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"/>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-select
              v-model="searchForm.status"
              placeholder="状态"
              clearable
              style="width: 140px"
              @change="handleFilterChange">
            <el-option label="全部状态" :value="''"/>
            <el-option label="启用" :value="1"/>
            <el-option label="禁用" :value="0"/>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">搜索</el-button>
          <el-button @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <el-card class="content-card" shadow="hover">
      <div class="section-header">
        <div class="section-title-group">
          <div class="section-title">会员列表</div>
          <div class="section-subtitle">基于 ums_member 表同步的会员档案，可新增/编辑信息</div>
        </div>
        <el-tag effect="plain" type="info">共 {{ page.total }} 位会员</el-tag>
      </div>
      <el-table
          :data="memberList"
          border
          stripe
          v-loading="loading"
          style="width: 100%">
        <el-table-column type="index" width="50" label="#"/>
        <el-table-column label="状态" width="80">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'danger'" effect="dark">
              {{ row.status === 1 ? "启用" : "禁用" }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="头像" width="90">
          <template #default="{ row }">
            <el-image
                v-if="row.headerPreview"
                :src="row.headerPreview"
                fit="cover"
                style="width: 60px;height:60px;border-radius: 50%"
                :preview-src-list="[row.headerPreview]"
                hide-on-click-modal/>
            <el-avatar v-else :size="56">
              {{ row.username?.slice(0, 1)?.toUpperCase() || "?" }}
            </el-avatar>
          </template>
        </el-table-column>
        <el-table-column prop="username" label="账号" width="110"/>
        <el-table-column prop="nickname" label="昵称" width="100"/>
        <el-table-column label="等级" width="90">
          <template #default="{ row }">
            <el-tag effect="plain">{{ row.levelName || "未分配" }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="mobile" label="手机号" width="140"/>
        <el-table-column prop="email" label="邮箱" width="200"/>
        <el-table-column label="性别" width="70">
          <template #default="{ row }">{{ formatGender(row.gender) }}</template>
        </el-table-column>
        <el-table-column prop="city" label="城市" width="120"/>
        <el-table-column prop="job" label="职业" width="120"/>
        <el-table-column label="来源" width="110">
          <template #default="{ row }">
            <el-tag effect="plain" :type="row.sourceType === 1 ? 'info' : 'primary'">
              {{ formatSource(row.sourceType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="integration" label="积分" width="90"/>
        <el-table-column prop="growth" label="成长值" width="100"/>
        <el-table-column prop="sign" label="个性签名" min-width="220">
          <template #default="{ row }">
            <el-text line-clamp="2">{{ row.sign || "-" }}</el-text>
          </template>
        </el-table-column>
        <el-table-column label="注册时间" width="180">
          <template #default="{ row }">{{ row.createTimeText || "-" }}</template>
        </el-table-column>
        <el-table-column label="操作" width="100" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" plain size="small" @click="openEdit(row)">编辑</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="pagination-bar">
        <el-pagination
            background
            layout="prev, pager, next"
            :page-size="pageSize"
            :total="page.total"
            :current-page="page.current"
            @current-change="handlePageChange"/>
      </div>
    </el-card>

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="780px" draggable>
      <el-form
          ref="memberFormRef"
          :model="memberForm"
          :rules="rules"
          label-width="110px"
          class="member-form">
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="会员等级" prop="levelId">
              <el-select
                  v-model="memberForm.levelId"
                  placeholder="请选择等级"
                  :disabled="isEdit">
                <el-option
                    v-for="item in levelOptions"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"/>
              </el-select>
              <el-text v-if="isEdit" type="info" size="small">
                等级会随着成长值自动调整
              </el-text>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="登录账号" prop="username">
              <el-input v-model.trim="memberForm.username" maxlength="30"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="登录密码" prop="password">
              <el-input
                  v-model.trim="memberForm.password"
                  show-password
                  maxlength="20"
                  :placeholder="memberForm.id ? '留空表示不修改' : ''"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="昵称">
              <el-input v-model.trim="memberForm.nickname" maxlength="50"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="手机号" prop="mobile">
              <el-input v-model.trim="memberForm.mobile" maxlength="20"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="邮箱" prop="email">
              <el-input v-model.trim="memberForm.email" maxlength="60"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="性别">
              <el-select v-model="memberForm.gender">
                <el-option v-for="item in genderOptions" :key="item.value" :label="item.label" :value="item.value"/>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="生日">
              <el-date-picker
                  v-model="memberForm.birth"
                  type="date"
                  value-format="YYYY-MM-DD"
                  placeholder="请选择生日"
                  style="width: 100%"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="所在城市">
              <el-input v-model.trim="memberForm.city" maxlength="60"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="职业">
              <el-input v-model.trim="memberForm.job" maxlength="60"/>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="个性签名">
              <el-input v-model.trim="memberForm.sign" type="textarea" :rows="2" maxlength="200"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="积分" prop="integration">
              <template v-if="!isEdit">
                <el-input-number v-model="memberForm.integration" :min="0" :max="999999" controls-position="right" style="width: 100%"/>
              </template>
              <template v-else>
                <div class="value-adjust">
                  <div class="value-adjust__current">
                    <span class="value-adjust__label">当前积分</span>
                    <span class="value-adjust__number">{{ memberForm.integration }}</span>
                  </div>
                  <div class="value-adjust__controls">
                    <el-radio-group v-model="integrationAdjust.mode" size="small">
                      <el-radio-button label="plus">增加</el-radio-button>
                      <el-radio-button label="minus">扣减</el-radio-button>
                    </el-radio-group>
                    <el-input-number
                        v-model="integrationAdjust.amount"
                        :min="1"
                        :max="999999"
                        controls-position="right"
                        style="width: 150px"/>
                    <el-button type="primary" plain size="small" @click="applyIntegrationAdjust">
                      应用调整
                    </el-button>
                  </div>
                  <el-text type="info" size="small">调整后需保存才能写入记录</el-text>
                </div>
              </template>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="成长值" prop="growth">
              <template v-if="!isEdit">
                <el-input-number v-model="memberForm.growth" :min="0" :max="999999" controls-position="right" style="width: 100%"/>
              </template>
              <template v-else>
                <div class="value-adjust">
                  <div class="value-adjust__current">
                    <span class="value-adjust__label">当前成长值</span>
                    <span class="value-adjust__number">{{ memberForm.growth }}</span>
                  </div>
                  <div class="value-adjust__controls clearfix">
                    <el-radio-group v-model="growthAdjust.mode" size="small">
                      <el-radio-button label="plus">增加</el-radio-button>
                      <el-radio-button label="minus">扣减</el-radio-button>
                    </el-radio-group>
                    <el-input-number
                        v-model="growthAdjust.amount"
                        :min="1"
                        :max="999999"
                        controls-position="right"
                        style="width: 150px"/>
                    <el-button type="primary" plain size="small" @click="applyGrowthAdjust">
                      应用调整
                    </el-button>
                  </div>
                  <el-text type="info" size="small">成长值变化将自动同步会员等级</el-text>
                </div>
              </template>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态" prop="status">
              <el-switch
                  v-model="memberForm.status"
                  :active-value="1"
                  :inactive-value="0"
                  active-text="启用"
                  inactive-text="禁用"/>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="头像" prop="header">
              <div class="avatar-field">
                <template v-if="!avatarPreview">
                  <el-upload
                      ref="avatarUploadRef"
                      class="avatar-uploader"
                      action="#"
                      :show-file-list="false"
                      :limit="1"
                      :http-request="customUpload"
                      :before-upload="beforeUpload"
                      :on-exceed="handleUploadExceed">
                    <el-button type="primary">上传头像</el-button>
                  </el-upload>
                </template>
                <div class="avatar-preview" v-else>
                  <el-image
                      :src="avatarPreview"
                      fit="cover"
                      style="width: 120px;height:120px;border-radius: var(--radius-md)"
                      :preview-src-list="[avatarPreview]"/>
                  <div class="avatar-actions">
                    <el-button link type="danger" @click="clearAvatar">移除</el-button>
                  </div>
                </div>
                <el-text type="info" size="small">
                  支持 jpg/png/webp，大小不超过 5MB，上传后自动存储到 OSS
                </el-text>
              </div>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="submitForm">
          {{ memberForm.id ? "保存修改" : "提交" }}
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import {computed, nextTick, onMounted, reactive, ref} from "vue";
import {doGet, doPost, doPut} from "../../http/HttpRequest.js";
import {getUUID, messageTip} from "../../util/util.js";

const memberList = ref([]);
const loading = ref(false);
const levelOptions = ref([]);
const pageSize = 10;
const page = reactive({current: 1, total: 0});

const searchForm = reactive({
  keyword: "",
  levelId: "",
  status: "",
});

const dialogVisible = ref(false);
const dialogTitle = ref("新增会员");
const memberFormRef = ref(null);
const avatarUploadRef = ref(null);
const submitLoading = ref(false);
const avatarPreview = ref("");
const avatarCache = new Map();

const genderOptions = [
  {label: "未知", value: 0},
  {label: "男", value: 1},
  {label: "女", value: 2},
];

const memberForm = reactive(createDefaultForm());
const isEdit = computed(() => !!memberForm.id);

const createAdjustState = () => ({
  mode: "plus",
  amount: 1,
});

const integrationAdjust = reactive(createAdjustState());
const growthAdjust = reactive(createAdjustState());

const rules = {
  levelId: [{required: true, message: "请选择会员等级", trigger: "change"}],
  username: [
    {required: true, message: "登录账号不能为空", trigger: "blur"},
    {min: 3, max: 30, message: "账号需在 3-30 个字符之间", trigger: "blur"},
  ],
  password: [
    {
      validator: (_, value, callback) => {
        if (!memberForm.id && !value) {
          callback(new Error("密码不能为空"));
          return;
        }
        if (value && (value.length < 6 || value.length > 20)) {
          callback(new Error("密码需在 6-20 位字符之间"));
          return;
        }
        callback();
      },
      trigger: "blur"
    }
  ],
  mobile: [
    {required: true, message: "手机号不能为空", trigger: "blur"},
    {pattern: /^1\d{10}$/, message: "请输入 11 位手机号", trigger: "blur"}
  ],
  email: [
    {required: true, message: "邮箱不能为空", trigger: "blur"},
    {
      pattern: /^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$/,
      message: "邮箱格式不正确",
      trigger: "blur"
    }
  ],
  integration: [{type: "number", min: 0, message: "积分不能小于 0", trigger: "change"}],
  growth: [{type: "number", min: 0, message: "成长值不能小于 0", trigger: "change"}],
  status: [{required: true, message: "请选择状态", trigger: "change"}],
  header: [{required: true, message: "请上传头像", trigger: "change"}],
};

function createDefaultForm() {
  return {
    id: null,
    levelId: "",
    username: "",
    password: "",
    nickname: "",
    mobile: "",
    email: "",
    header: "",
    gender: 0,
    birth: "",
    city: "",
    job: "",
    sign: "",
    sourceType: 0,
    integration: 0,
    growth: 0,
    status: 1,
  };
}

const resetAdjustments = () => {
  integrationAdjust.mode = "plus";
  integrationAdjust.amount = 1;
  growthAdjust.mode = "plus";
  growthAdjust.amount = 1;
};

const clampValue = (value) => {
  if (value > 999999) {
    return 999999;
  }
  if (value < -999999) {
    return -999999;
  }
  return value;
};

const applyValueAdjust = (field, adjustState, label) => {
  const amount = Number(adjustState.amount);
  if (!amount || amount <= 0) {
    messageTip(`请输入要调整的${label}`, "warning");
    return;
  }
  const delta = adjustState.mode === "minus" ? -amount : amount;
  const current = Number(memberForm[field]) || 0;
  const next = clampValue(current + delta);
  memberForm[field] = next;
  adjustState.amount = 1;
  messageTip(`${label}已更新为 ${next}`, "success");
};

const applyIntegrationAdjust = () => applyValueAdjust("integration", integrationAdjust, "积分");
const applyGrowthAdjust = () => applyValueAdjust("growth", growthAdjust, "成长值");

const loadMemberLevels = async () => {
  const resp = await doGet("/api/member/level/list", {});
  if (resp?.data?.code === 200) {
    levelOptions.value = (resp.data.data || []).map(item => ({
      label: item.name,
      value: item.id,
    }));
  }
};

const loadMembers = async () => {
  loading.value = true;
  try {
    const params = {
      current: page.current,
    };
    if (searchForm.keyword) {
      params.keyword = searchForm.keyword;
    }
    if (searchForm.levelId !== "" && searchForm.levelId !== null) {
      params.levelId = searchForm.levelId;
    }
    if (searchForm.status !== "" && searchForm.status !== null) {
      params.status = searchForm.status;
    }
    const resp = await doGet("/api/member/member/list", params);
    if (resp?.data?.code === 200) {
      const data = resp.data.data || {};
      memberList.value = data.list || [];
      page.total = data.total || 0;
      page.current = data.pageNum || page.current;
      await resolveAvatarPreview(memberList.value);
    } else {
      messageTip(resp?.data?.msg || "获取会员列表失败", "error");
    }
  } catch (e) {
    messageTip("获取会员列表失败", "error");
  } finally {
    loading.value = false;
  }
};

const resolveAvatarPreview = async (rows) => {
  const tasks = rows
      .filter(row => row.header)
      .map(async row => {
        const url = await fetchSignedUrl(row.header);
        if (url) {
          row.headerPreview = url;
        }
      });
  await Promise.all(tasks);
};

const fetchSignedUrl = async (path) => {
  if (!path) {
    return "";
  }
  if (avatarCache.has(path)) {
    return avatarCache.get(path);
  }
  try {
    const resp = await doGet("/api/thirdParty/oss/getSignedUrl", {uploadedImageUrl: path});
    if (resp?.data?.code === 200) {
      avatarCache.set(path, resp.data.data);
      return resp.data.data;
    }
  } catch (_) {
    // ignore
  }
  return "";
};

const openCreate = () => {
  Object.assign(memberForm, createDefaultForm());
  resetAdjustments();
  avatarPreview.value = "";
  dialogTitle.value = "新增会员";
  dialogVisible.value = true;
  nextTick(() => memberFormRef.value?.clearValidate());
};

const openEdit = async (row) => {
  Object.assign(memberForm, createDefaultForm(), {
    id: row.id,
    levelId: row.levelId,
    username: row.username,
    password: "",
    nickname: row.nickname,
    mobile: row.mobile,
    email: row.email,
    header: row.header,
    gender: row.gender ?? 0,
    birth: row.birth ? String(row.birth).slice(0, 10) : "",
    city: row.city,
    job: row.job,
    sign: row.sign,
    sourceType: row.sourceType ?? 0,
    integration: row.integration ?? 0,
    growth: row.growth ?? 0,
    status: row.status ?? 1,
  });
  resetAdjustments();
  avatarPreview.value = row.headerPreview || "";
  if (!avatarPreview.value && row.header) {
    avatarPreview.value = await fetchSignedUrl(row.header);
  }
  dialogTitle.value = "编辑会员";
  dialogVisible.value = true;
  nextTick(() => memberFormRef.value?.clearValidate());
};

const buildPayload = () => {
  const payload = {
    levelId: memberForm.levelId,
    username: memberForm.username,
    password: memberForm.password || undefined,
    nickname: memberForm.nickname,
    mobile: memberForm.mobile,
    email: memberForm.email,
    header: memberForm.header,
    gender: memberForm.gender,
    birth: memberForm.birth || null,
    city: memberForm.city,
    job: memberForm.job,
    sign: memberForm.sign,
    sourceType: memberForm.sourceType ?? 0,
    integration: memberForm.integration,
    growth: memberForm.growth,
    status: memberForm.status,
  };
  if (!payload.birth) {
    delete payload.birth;
  }
  if (!payload.password) {
    delete payload.password;
  }
  return payload;
};

const submitForm = () => {
  if (!memberFormRef.value) {
    return;
  }
  memberFormRef.value.validate(async (valid) => {
    if (!valid) {
      return;
    }
    submitLoading.value = true;
    try {
      const payload = buildPayload();
      let resp;
      if (memberForm.id) {
        resp = await doPut(`/api/member/member/${memberForm.id}`, payload);
      } else {
        resp = await doPost("/api/member/member", payload);
      }
      if (resp?.data?.code === 200) {
        messageTip(memberForm.id ? "修改成功" : "新增成功", "success");
        dialogVisible.value = false;
        await reload();
      } else {
        messageTip(resp?.data?.msg || "提交失败", "error");
      }
    } catch (e) {
      messageTip("提交失败，请稍后重试", "error");
    } finally {
      submitLoading.value = false;
    }
  });
};

const reload = async () => {
  page.current = 1;
  await loadMembers();
};

const handleSearch = () => {
  page.current = 1;
  loadMembers();
};

const resetSearch = () => {
  searchForm.keyword = "";
  searchForm.levelId = "";
  searchForm.status = "";
  handleSearch();
};

const handleFilterChange = () => {
  page.current = 1;
  loadMembers();
};

const handlePageChange = (val) => {
  page.current = val;
  loadMembers();
};

const beforeUpload = (file) => {
  const isImg = file.type.startsWith("image/");
  if (!isImg) {
    messageTip("仅支持图片格式", "warning");
    return false;
  }
  const isLt5M = file.size / 1024 / 1024 < 5;
  if (!isLt5M) {
    messageTip("图片不能大于 5MB", "warning");
    return false;
  }
  return true;
};

const customUpload = async (options) => {
  const {file, onError, onSuccess} = options;
  try {
    const policyResp = await doGet("/api/thirdParty/oss/getPolicy", {dir: "member/header/"});
    if (policyResp?.data?.code !== 200) {
      throw new Error(policyResp?.data?.msg || "获取上传凭证失败");
    }
    const data = policyResp.data.data;
    const uploadUrl = `//${data.host.split("://")[1]}`;
    const fileExt = file.name.split(".").pop();
    const objectKey = `${getUUID()}.${fileExt}`;
    const formData = new FormData();
    formData.append("key", data.dir + objectKey);
    formData.append("OSSAccessKeyId", data.accessKeyId);
    formData.append("policy", data.policy);
    formData.append("signature", data.signature);
    formData.append("success_action_status", "200");
    formData.append("file", file);
    const resp = await doPost(uploadUrl, formData);
    if (resp.status === 200) {
      const savedPath = `${data.dir}${objectKey}`;
      memberForm.header = savedPath;
      avatarPreview.value = await fetchSignedUrl(savedPath);
      if (typeof onSuccess === "function") {
        onSuccess(resp, file);
      }
      messageTip("上传成功", "success");
    } else {
      throw new Error("上传失败");
    }
  } catch (err) {
    messageTip(err?.message || "上传失败", "error");
    if (typeof onError === "function") {
      onError(err);
    }
  }
};

const handleUploadExceed = () => {
  messageTip("一次仅能上传一个头像", "warning");
};

const clearAvatar = () => {
  memberForm.header = "";
  avatarPreview.value = "";
  avatarUploadRef.value?.clearFiles?.();
};

const formatGender = (value) => {
  switch (value) {
    case 1:
      return "男";
    case 2:
      return "女";
    default:
      return "未知";
  }
};

const formatSource = (value) => {
  return value === 1 ? "用户注册" : "后台创建";
};

onMounted(async () => {
  await Promise.all([loadMemberLevels(), loadMembers()]);
});
</script>

<style scoped>

.member-page .toolbar {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  gap: 12px;
  margin-bottom: 16px;
}

.toolbar-actions {
  display: flex;
  align-items: center;
  gap: 12px;
  flex-wrap: wrap;
}

.toolbar-form {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
  align-items: center;
}

.toolbar-form :deep(.el-form-item) {
  margin-bottom: 0;
}

.section-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 12px;
}

.section-title-group {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.section-title {
  font-size: 18px;
  font-weight: 600;
}

.section-subtitle {
  color: var(--text-secondary);
  font-size: 13px;
}

.pagination-bar {
  display: flex;
  justify-content: flex-end;
  padding: 16px 0 4px;
}

.avatar-field {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.avatar-preview {
  display: flex;
  align-items: center;
  gap: 16px;
}

.avatar-actions {
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.value-adjust {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.value-adjust__current {
  display: flex;
  align-items: baseline;
  gap: 8px;
}

.value-adjust__label {
  color: var(--text-secondary);
  font-size: 13px;
}

.value-adjust__number {
  font-size: 20px;
  font-weight: 600;
  color: var(--text-primary);
}

.value-adjust__controls {
  display: flex;
  align-items: center;
  gap: 8px;
  flex-wrap: wrap;
}

.member-form :deep(.el-form-item) {
  margin-bottom: 18px;
}
</style>
