<template>
  <!--两个按钮-->
  <el-button type="primary" @click="add">添加用户</el-button>
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
      @next-click="toPage"/>
  <!--这是新增用户的弹窗-->
  <el-dialog v-model="addUserWindows" title="添加用户" width="600" draggable>
    <el-form :model="addUser" label-width="110px" :rules="addUserRules" ref="addUserRefForm">
      <el-form-item label="账号" prop="username">
        <el-input v-model="addUser.username"/>
      </el-form-item>

      <el-form-item label="密码" prop="password">
        <el-input type="password" v-model="addUser.password"/>
      </el-form-item>

      <el-form-item label="邮箱" prop="email">
        <el-input v-model="addUser.email"/>
      </el-form-item>

      <el-form-item label="手机" prop="mobile">
        <el-input v-model="addUser.mobile"/>
      </el-form-item>

      <el-form-item label="账户状态" prop="status">
        <el-select v-model="addUser.status" placeholder="请选择" style="width: 100%">
          <el-option label="正常" value="1"/>
          <el-option label="禁用" value="0"/>
        </el-select>
      </el-form-item>

      <el-form-item label="账户角色" prop="roleDo.id">
        <el-select
            v-model="addUser.roleDo.id"
            placeholder="请选择账户角色"
            clearable
            filterable>
          <!--注意下面，key 代表的是传给后端的值，label 代表的是展示给前端的值，value 表示的是下拉框改变时，绑定到 v-model 的值-->
          <el-option
              v-for="item in roleOptions"
              :key="item.id"
              :label="item.typeValue"
              :value="item.id"/>
        </el-select>
      </el-form-item>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="addUserWindows = false">取消</el-button>
        <el-button type="primary" @click="addUserSubmit">
          添加
        </el-button>
      </div>
    </template>
  </el-dialog>
</template>


<script>
import {defineComponent} from "vue";
import {doGet, doPost} from "../../http/HttpRequest.js";
import {messageTip} from "../../util/util.js";

export default defineComponent({
  name: "SysUsersView",
  data() {
    return {
      // 定义 List 对象
      sysUserList: [{}],
      myPageSize: 0,
      myTotal: 0,
      addUserWindows: false,
      addUser: {
        id: 0,
        username: "",
        password: "",
        email: "",
        mobile: "",
        status: "1",
        roleDo: {
          id: "",
          typeValue: "",
        },
      },
      roleOptions: [],
      addUserRules: {
        username: [
          {required: true, message: '请输入账号！', trigger: 'blur'},
          {min: 1, max: 16, message: '账号的长度在 16 之间！', trigger: 'blur'},
          {
            pattern: /^[a-z]+$/,
            message: '账号只能小写英文字母！',
            trigger: 'blur'   // 失去焦点时校验
          }
        ],
        password: [
          {required: true, message: '请输入密码！', trigger: 'blur'},
          {min: 6, max: 16, message: '密码的长度在 6-16 之间！', trigger: 'blur'},
        ],
        email: [
          {required: true, message: '请输入邮箱！', trigger: 'blur'},
          {
            pattern: /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+.[a-zA-Z]{2,}$/,
            message: '请输入正确的邮箱格式！',
            trigger: 'blur'
          },
        ],
        mobile: [
          {required: true, message: '请输入手机！', trigger: 'blur'},
          {pattern: /^1[3-9]\d{9}$/, message: '请输入正确的中国手机格式！', trigger: 'blur'},
        ],
        status: [
          {required: true, message: '请选择账户状态！', trigger: 'blur'},
        ],
        'roleDo.id': [
          {required: true, message: '请选择账户状态！', trigger: 'blur'},
        ],
      },
    }
  },
  methods: {
    loadRoleOptions() {
      doGet("/api/admin/sysRole/roles", {}).then(resp => {
        console.log(resp.data.data)
        if (resp.data.code === 200) {
          this.roleOptions = resp.data.data;
        }
      })
    },
    // 提交新增用户
    addUserSubmit() {
      this.$refs.addUserRefForm.validate((isValid) => {
        if (isValid) {
          // console.log("passed");
          let formData = new FormData();
          // 以键值对的形式写入数据
          formData.append('username', this.addUser.username);
          formData.append('password', this.addUser.password);
          formData.append('email', this.addUser.email);
          formData.append('mobile', this.addUser.mobile);
          formData.append('status', this.addUser.status);
          formData.append('roleId', this.addUser.roleDo.id);

          // console.log(formData);
          doPost("/api/admin/sysUser/role", formData).then((resp) => {
            if (resp.data.code === 200){
              messageTip("添加用户成功！","success");
              this.addUserWindows = false;
              this.reload();
            }else{
              messageTip("添加用户失败！","error");
            }
          })
        }
      })
    },
    // 新增用户
    add() {
      this.addUserWindows = true
    },
    // 跳转到指定 id 的用户信息界面
    view(id) {
      let url = "/dashboard/admin/sysUser/" + id
      // alert(url)
      this.$router.push(url)
    },
    toPage(current) {
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
    this.loadRoleOptions();
  },
  inject:['reload'],
})

</script>

<style scoped>
.el-table {
  margin-top: 15px;
}

.el-pagination {
  margin-top: 20px;
}
</style>