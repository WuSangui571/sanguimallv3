<template>
  <el-form :model="loginForm" :rules="loginRules" ref="loginRefForm">
    <!--表单的标题-->
    <el-form-item class="form-title">
      <h2>sanguimall 后台管理系统登录</h2>
    </el-form-item>

    <!--表单的账号-->
    <el-form-item label="账号" prop="loginAct">
      <el-input v-model="loginForm.loginAct" />
    </el-form-item>

    <!--表单的密码-->
    <el-form-item label="密码" prop="loginPwd">
      <!--为该字段添加密码类型-->
      <el-input type="password" v-model="loginForm.loginPwd" />
    </el-form-item>

    <!--前端添加验证码-->

    <!--表单的注册按钮-->
    <el-form-item>
      <!--添加按钮点击函数：login-->
      <el-button type="primary" @click="login" class="form-button">登录</el-button>
    </el-form-item>

    <!--表单的记住我选项-->
    <el-form-item>
      <el-checkbox v-model="loginForm.rememberMe">
        记住我
      </el-checkbox>
    </el-form-item>
  </el-form>
</template>

<script>
import {defineComponent} from 'vue'
import {doGet, doPost} from "../http/HttpRequest.js";
import {getTokenName, messageTip, removeToken} from "../util/util.js";
export default defineComponent({
  name: "LoginView",
  // 所有的变量都需要注册在 data 里
  data(){
    return {
      // loginForm 是对象，所以注册为 {}
      loginForm: {},
      // loginAct 是字符串，所以注册为 ""
      loginAct: "",
      // loginPwd 是字符串，所以注册为 ""
      loginPwd: "",
      // rememberMe 是布尔类型，所以注册为 false
      rememberMe: false,
      // loginRules 是对象，所以注册为 {}
      loginRules: {
        // 定义 loginAct 的规则，规则可以有多个，所以是数组，用 []
        loginAct: [
          // 添加账号不能为空的验证
          {required: true, message: '请输入账号！', trigger: 'blur'},
        ],
        // 定义 loginPwd 的规则，规则可以有多个，所以是数组，用 []
        loginPwd: [
          // 添加密码不能为空的验证
          {required: true, message: '请输入密码！', trigger: 'blur'},
          // 添加密码长度的验证
          {min: 6, max: 16, message: '密码的长度在 6-16 之间！', trigger: 'blur'},
        ],
      },
    }
  },
  // 页面渲染完 dom 元素后会触发调用该函数（函数钩子）
  mounted() {
    this.freeLogin();
  },
  // 所有的方法都需要注册在 methods 里
  methods: {
    freeLogin(){
      // 检查有没有选择记住我，（通过检查是否有 token 来检查之前是否有选择记住我）
      let token = window.localStorage.getItem(getTokenName());
      // 判断 token 是否有值
      if (token){
        doGet("/api/admin/sysUser/freeLogin",{}).then(resp =>{
          if (resp.data.code === 200){
            // token 验证通过，可以免登录
            window.location.href = "/dashboard";
          }else {
            // token 错误
          }
        })
      }
    },
    // 登录函数
    login() {
      // 提交前验证输入框的合法性
      this.$refs.loginRefForm.validate((isValid) => {
        if (isValid) {
          // 运行到这里说明验证通过
          // 使用 formData 上传数据
          let formData = new FormData();
          // 以键值对的形式写入数据
          formData.append('username', this.loginForm.loginAct);
          formData.append('password', this.loginForm.loginPwd);
          formData.append('rememberMe', this.loginForm.rememberMe);
          doPost("/api/admin/sysUser/login",formData).then((resp) =>{
            // 看看响应的形式是怎么样的
            // console.log(resp);
            if (resp.data.code === 200){
              // 删除历史 token
              removeToken();
              messageTip("登录成功，欢迎回来！", "success");
              // 存储 jwt
              if (this.loginForm.rememberMe === true){
                window.localStorage.setItem(getTokenName(), resp.data.data);
              }else {
                window.sessionStorage.setItem(getTokenName(), resp.data.data);
              }
              window.location.href = "/dashboard";
            }else {
              messageTip("登录失败，账号或密码错误！", "error");
            }
          });
        }
      })
    }
  }
})
</script>

<style scoped>
.el-form{
  width: 20%;
  margin: auto;
}
.form-title{
  margin-top: 200px;
}
.form-button{
  width: 100%;
}
</style>