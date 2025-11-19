<template>
  <div class="login-page">
    <div class="login-layout">
      <section class="login-hero">
        <div class="hero-badge">Sanguimall Admin</div>
        <h1>欢迎回到商城运营中台</h1>
        <p class="hero-desc">
          集中管理商品、会员与权限系统，稳定运维每一条业务链路。登录后即可延续上次的主题偏好和操作上下文。
        </p>
        <ul class="hero-highlights">
          <li>
            <strong>统一的多业务视图</strong>
            <span>实时掌握分类、品牌、会员的关键状态</span>
          </li>
          <li>
            <strong>亮暗主题自动同步</strong>
            <span>登录页与控制台保持一致的视觉体验</span>
          </li>
        </ul>
        <div class="hero-metrics">
          <div>
            <span class="metric-value">3+</span>
            <span class="metric-label">业务集群</span>
          </div>
          <div>
            <span class="metric-value">24/7</span>
            <span class="metric-label">安全巡检</span>
          </div>
          <div>
            <span class="metric-value">99.9%</span>
            <span class="metric-label">可用性</span>
          </div>
        </div>
        <div class="hero-glow"></div>
      </section>
      <el-card class="login-card" shadow="hover">
        <div class="card-header">
          <div class="card-meta">
            <p class="card-badge">账号登录</p>
            <h2>开启今日的运营排班</h2>
            <p class="card-desc">使用企业账号进入控制台，系统会自动沿用最近一次的主题偏好。</p>
          </div>
        </div>
        <el-form
            ref="loginRefForm"
            :model="loginForm"
            :rules="loginRules"
            label-position="top"
            status-icon
            class="login-form"
            @keyup.enter="login">
          <el-form-item label="账号" prop="loginAct">
            <el-input v-model="loginForm.loginAct" placeholder="请输入账号">
              <template #prefix>
                <el-icon>
                  <User/>
                </el-icon>
              </template>
            </el-input>
          </el-form-item>
          <el-form-item label="密码" prop="loginPwd">
            <el-input
                v-model="loginForm.loginPwd"
                type="password"
                show-password
                placeholder="请输入密码">
              <template #prefix>
                <el-icon>
                  <Lock/>
                </el-icon>
              </template>
            </el-input>
          </el-form-item>
          <div class="form-footer">
            <el-checkbox v-model="loginForm.rememberMe">
              记住我的登录状态
            </el-checkbox>
            <span class="form-helper">当前为{{ isDarkTheme ? '暗色' : '亮色' }}主题</span>
          </div>
          <el-button class="submit-button" type="primary" size="large" @click="login">
            登录控制台
          </el-button>
        </el-form>
      </el-card>
    </div>
  </div>
</template>

<script>
import {defineComponent} from 'vue'
import {doGet, doPost} from "../http/HttpRequest.js";
import {getTokenName, messageTip, removeToken} from "../util/util.js";

export default defineComponent({
  name: "LoginView",
  data() {
    return {
      loginForm: {
        loginAct: "",
        loginPwd: "",
        rememberMe: false,
      },
      loginRules: {
        loginAct: [
          {required: true, message: '请输入账号！', trigger: 'blur'},
        ],
        loginPwd: [
          {required: true, message: '请输入密码！', trigger: 'blur'},
          {min: 6, max: 16, message: '密码的长度在 6-16 之间', trigger: 'blur'},
        ],
      },
      isDarkTheme: false,
    }
  },
  mounted() {
    this.applySavedTheme();
    this.freeLogin();
  },
  methods: {
    applySavedTheme() {
      const saved = window.localStorage.getItem("sanguimall-theme");
      this.isDarkTheme = saved ? saved === "dark" : false;
      document.documentElement.setAttribute("data-theme", this.isDarkTheme ? "dark" : "light");
    },
    freeLogin() {
      const token = window.localStorage.getItem(getTokenName());
      if (token) {
        doGet("/api/admin/sysUser/freeLogin", {}).then(resp => {
          if (resp.data.code === 200) {
            window.location.href = "/dashboard";
          }
        })
      }
    },
    login() {
      this.$refs.loginRefForm.validate((isValid) => {
        if (!isValid) {
          return;
        }
        const formData = new FormData();
        formData.append('username', this.loginForm.loginAct);
        formData.append('password', this.loginForm.loginPwd);
        formData.append('rememberMe', this.loginForm.rememberMe);
        doPost("/api/admin/sysUser/login", formData).then((resp) => {
          if (resp.data.code === 200) {
            removeToken();
            messageTip("登录成功，欢迎回来！", "success");
            if (this.loginForm.rememberMe) {
              window.localStorage.setItem(getTokenName(), resp.data.data);
            } else {
              window.sessionStorage.setItem(getTokenName(), resp.data.data);
            }
            window.location.href = "/dashboard";
          } else {
            messageTip("登录失败，账号或密码错误", "error");
          }
        });
      })
    }
  }
})
</script>

<style scoped>
.login-page {
  min-height: 100vh;
  padding: 40px 24px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: radial-gradient(circle at 18% 20%, rgba(79, 139, 255, 0.18), transparent 45%),
  radial-gradient(circle at 80% 0%, rgba(56, 189, 248, 0.12), transparent 50%),
  var(--bg-body);
  transition: background 0.3s ease;
}

.login-layout {
  width: 100%;
  max-width: 1100px;
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(320px, 1fr));
  gap: 32px;
  align-items: stretch;
}

.login-hero {
  position: relative;
  border-radius: var(--radius-lg);
  padding: 48px 40px;
  background: linear-gradient(135deg, rgba(79, 139, 255, 0.12), rgba(79, 139, 255, 0.02));
  border: 1px solid rgba(79, 139, 255, 0.25);
  overflow: hidden;
  color: var(--text-primary);
  box-shadow: var(--shadow-soft);
}

.login-hero > * {
  position: relative;
  z-index: 1;
}

.hero-badge {
  display: inline-flex;
  align-items: center;
  padding: 4px 14px;
  font-size: 12px;
  letter-spacing: 0.08em;
  text-transform: uppercase;
  border-radius: 999px;
  background: rgba(79, 139, 255, 0.15);
  color: var(--accent);
  border: 1px solid rgba(79, 139, 255, 0.25);
}

.login-hero h1 {
  margin: 18px 0 10px;
  font-size: 30px;
  font-weight: 700;
  line-height: 1.25;
}

.hero-desc {
  margin: 0;
  color: var(--text-secondary);
  font-size: 15px;
  line-height: 1.6;
}

.hero-highlights {
  list-style: none;
  padding: 0;
  margin: 28px 0 32px;
  display: flex;
  flex-direction: column;
  gap: 14px;
}

.hero-highlights li {
  background: rgba(255, 255, 255, 0.6);
  border: 1px solid rgba(79, 139, 255, 0.16);
  border-radius: var(--radius-md);
  padding: 12px 14px;
}

.hero-highlights strong {
  display: block;
  font-size: 16px;
  color: var(--text-primary);
}

.hero-highlights span {
  font-size: 13px;
  color: var(--text-secondary);
}

.hero-metrics {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(120px, 1fr));
  gap: 14px;
}

.hero-metrics div {
  background: rgba(255, 255, 255, 0.8);
  border: 1px solid rgba(79, 139, 255, 0.2);
  border-radius: var(--radius-md);
  padding: 18px 12px;
  text-align: center;
}

.metric-value {
  display: block;
  font-size: 22px;
  font-weight: 600;
  color: var(--text-primary);
}

.metric-label {
  display: block;
  font-size: 13px;
  color: var(--text-secondary);
  margin-top: 4px;
}

.hero-glow {
  position: absolute;
  width: 220px;
  height: 220px;
  border-radius: 50%;
  background: radial-gradient(circle, rgba(79, 139, 255, 0.35), transparent 60%);
  top: -60px;
  right: -30px;
  filter: blur(0);
  pointer-events: none;
  z-index: 0;
}

.login-card {
  border-radius: var(--radius-lg);
  border: 1px solid var(--border-color);
  background: var(--bg-card);
  box-shadow: var(--shadow-soft);
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.login-card :deep(.el-card__body) {
  padding: 36px;
}

.card-badge {
  font-size: 12px;
  text-transform: uppercase;
  letter-spacing: 0.08em;
  color: var(--accent);
  margin: 0 0 8px;
}

.card-header h2 {
  margin: 0;
  font-size: 24px;
  font-weight: 600;
  color: var(--text-primary);
}

.card-desc {
  margin: 10px 0 0;
  color: var(--text-secondary);
  font-size: 14px;
  line-height: 1.6;
}

.login-form {
  margin-top: 20px;
}

.login-form :deep(.el-form-item) {
  margin-bottom: 18px;
}

.login-form :deep(.el-form-item__label) {
  font-weight: 600;
  color: var(--text-secondary);
}

.login-form :deep(.el-input__wrapper) {
  background: var(--bg-body);
  box-shadow: none;
  border: 1px solid var(--border-color);
}

.form-footer {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 12px;
  color: var(--text-secondary);
  font-size: 13px;
}

.form-helper {
  color: var(--text-secondary);
}

.submit-button {
  width: 100%;
  height: 44px;
  font-size: 16px;
  font-weight: 600;
}

:global([data-theme="dark"]) .login-page {
  background: radial-gradient(circle at 18% 20%, rgba(79, 139, 255, 0.25), transparent 45%),
  radial-gradient(circle at 80% 0%, rgba(56, 189, 248, 0.18), transparent 50%),
  var(--bg-body);
}

:global([data-theme="dark"]) .login-hero {
  background: linear-gradient(135deg, rgba(17, 24, 39, 0.95), rgba(15, 23, 42, 0.85));
  border-color: rgba(255, 255, 255, 0.08);
}

:global([data-theme="dark"]) .hero-highlights li {
  background: rgba(17, 24, 39, 0.85);
  border-color: rgba(255, 255, 255, 0.08);
}

:global([data-theme="dark"]) .hero-metrics div {
  background: rgba(17, 24, 39, 0.75);
  border-color: rgba(255, 255, 255, 0.08);
}

:global([data-theme="dark"]) .login-form :deep(.el-input__wrapper) {
  background: rgba(15, 23, 42, 0.85);
}

@media (max-width: 768px) {
  .login-layout {
    grid-template-columns: 1fr;
  }

  .login-hero,
  .login-card :deep(.el-card__body) {
    padding: 28px;
  }

  .login-page {
    padding: 20px;
  }
}
</style>
