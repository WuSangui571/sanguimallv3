<template>
<!--  <div style="display: flex; align-items: center; gap: 10px;">-->
    <div class="toolbar">
      <!--两个按钮-->
      <el-button type="primary" @click="add">添加用户</el-button>
      <el-button type="danger" @click="batchDel">批量删除</el-button>
      <div class="mySearch">
        <el-form :model="searchUser" :rules="searchUserRules" ref="searchUserRefForm">
          <!--      <el-form :model="searchUser">-->
          <el-form-item prop="selectValue">
            <el-input
                v-model="searchUser.selectValue"
                style="max-width: 600px"
                placeholder="请输入具体的模糊查询"
                class="input-with-select"
                @keydown.enter.prevent
                @keyup.enter="onKeyupEnter"
                @compositionstart="onCompStart"
                @compositionend="onCompEnd"
            >
              <template #prepend>
                <el-select v-model="searchUser.selectKey" placeholder="请选择" style="width: 115px">
                  <el-option label="账号" value="username"/>
                  <el-option label="邮箱" value="email"/>
                  <el-option label="手机" value="mobile"/>
                </el-select>
              </template>
              <template #append>
                <el-button :icon="Search" @click="submitSearch"/>
              </template>
            </el-input>
          </el-form-item>
        </el-form>
      </div>
      <el-button type="success" @click="reFlash">重置</el-button>
    </div>
<!--  </div>-->
  <el-card class="content-card" shadow="hover">
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
      <el-table-column property="status" label="状态" width="120">
        <template #default="scope">
          <el-text class="mx-1" type="success" v-if="scope.row.status == 1">
            正常
          </el-text>
          <el-text class="mx-1" type="danger" v-else>
            禁用
          </el-text>
        </template>
      </el-table-column>
      <el-table-column label="操作">
        <template #default="scope">
          <el-button type="primary" @click="view(scope.row.userId)">详情</el-button>
          <el-button type="warning" @click="edit(scope.row.userId)">编辑</el-button>
          <el-button type="danger" @click="del(scope.row.userId,scope.row.username)">删除</el-button>
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
  </el-card>
  <!--这是新增用户的弹窗-->
  <el-dialog v-model="addUserWindows" :title="addUser.id>0?'编辑用户':'添加用户'" width="600" draggable>
    <el-form :model="addUser" label-width="110px" :rules="addUserRules" ref="addUserRefForm">
      <el-form-item label="账号" prop="username">
        <el-input v-model="addUser.username"/>
      </el-form-item>

      <!--编辑时，将密码设置为不验证-->
      <el-form-item label="密码" v-if="addUser.id > 0">
        <el-input type="password" v-model="addUser.password" placeholder="空值代表不修改密码"/>
      </el-form-item>

      <!--非编辑时-->
      <el-form-item label="密码" prop="password" v-else>
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
          {{ addUser.id > 0 ? '编 辑' : '添 加' }}
        </el-button>
      </div>
    </template>
  </el-dialog>
</template>


<script>
import {defineComponent} from "vue";
import {doDelete, doGet, doPost, doPut} from "../../http/HttpRequest.js";
import {messageConfirm, messageTip} from "../../util/util.js";
import {Search} from "@element-plus/icons-vue";

export default defineComponent({
  name: "SysUsersView",
  computed: {
    Search() {
      return Search
    }
  },
  data() {
    return {
      select: [],
      searchUser: {
        selectKey: "",
        selectValue: "",
      },
      searchUserRules: {
        selectValue: [
          {max: 16, message: '查询条件在 16 个字符之内', trigger: 'blur'},
          {
            pattern: /^[\u4e00-\u9fa5A-Za-z0-9_ -]+$/,
            message: '只允许输入中文、英文、数字、下划线或空格！',
            trigger: 'blur'
          }
        ],
      },
      isSearch: false,
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
        status: "",
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
      selectedIds: [],
      selectedNames: [],
      // 是否处于中文输入法合成中
      isComposing: false,
    }
  },
  methods: {
    submitSearch() {
      let selectKey = this.searchUser.selectKey;
      let selectValue = this.searchUser.selectValue;

      if (selectKey === "" && selectValue === "") {
        messageTip("请输入查询条件！", "error")
        return;
      }
      if (selectKey === "") {
        messageTip("请输入要查询的字段！", "error")
        return;
      }
      if (selectValue === "") {
        messageTip("请输入具体的模糊查询！", "error")
        return;
      }
      this.$refs.searchUserRefForm.validate((isValid) => {
        if (isValid) {
          this.isSearch = true;
          this.getData(1);
        }
      })
    },
    reFlash() {
      this.$router.go(0);
    },
    // 删除指定用户
    del(id, name) {
      messageConfirm("确认删除 " + name + " 吗？", "温馨提示").then(() => {
        let url = "/api/admin/sysUser/sysUser/" + id
        //alert(url)
        doDelete(url, {}).then((resp) => {
          if (resp.data.code === 200) {
            messageTip("已删除" + name, "success")
            this.reload();
          } else {
            messageTip("删除失败！", "error")
          }
        })
      }).catch(() => {
        // 用户点击取消就会触发 catch 里
        messageTip("已取消删除！", "error")
      })
    },
    // 编辑指定 id 的用户
    edit(id) {
      this.addUserWindows = true;
      this.loadEditData(id);
    },
    // 编辑时加载对应 id 用户的数据
    loadEditData(id) {
      let url = "/api/admin/sysUser/" + id
      doGet(url, {}).then((resp) => {
        //console.log("this！")
        //console.log(resp.data.data);
        if (resp.data.code === 200) {
          // 简单一点，也可以这样：
          // this.addUser = resp.data.data;
          this.addUser.id = resp.data.data.id;
          this.addUser.username = resp.data.data.username;
          this.addUser.password = "";
          this.addUser.email = resp.data.data.email;
          this.addUser.mobile = resp.data.data.mobile;
          this.addUser.status = resp.data.data.status == 1 ? "正常" : "禁用";
          this.addUser.roleDo = resp.data.data.roleVo;
        }
      })
    },
    loadRoleOptions() {
      doGet("/api/admin/sysRole/roles", {}).then(resp => {
        //console.log(resp.data.data)
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

          formData.append('roleId', this.addUser.roleDo.id);
          console.log("status:" + this.addUser.status)
          if (this.addUser.id > 0) {
            console.log("走编辑！")
            formData.append('status', this.addUser.status == "正常" ? '1' : '0');
            // 修改用户
            formData.append('id', this.addUser.id);
            console.log(formData)
            // 将编辑用户代码在此处写
            doPut("/api/admin/sysUser/sysUser", formData).then((resp) => {
              // console.log(resp.data.data);
              if (resp.data.code === 200) {
                messageTip("编辑用户成功！", "success");
                this.addUserWindows = false;
                this.reload();
              } else {
                messageTip("编辑用户失败！请检查输入的条件！", "error");
              }
            })
          } else {
            console.log("走添加！")
            formData.append('status', this.addUser.status);
            // 添加用户
            // console.log(formData);
            doPost("/api/admin/sysUser/sysUser", formData).then((resp) => {
              if (resp.data.code === 200) {
                messageTip("添加用户成功！", "success");
                this.addUserWindows = false;
                this.reload();
              } else {
                messageTip("添加用户失败！请检查输入的条件!", "error");
              }
            })
          }
        }
      })
    },
    // 新增用户
    add() {
      this.addUser = {
        id: 0,
        username: "",
        password: "",
        email: "",
        mobile: "",
        status: "",
        roleDo: {
          id: "",
          typeValue: "",
        },
      };
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
    handleSelectionChange(selectionDataArray) {
      // console.log(selectionDataArray)
      // 清空 Ids、Names 数组
      this.selectedIds = [];
      this.selectedNames = [];
      // 遍历数组
      selectionDataArray.forEach(data => {
        // 遍历数组中的元素，将 id、names 加入统一的数组
        this.selectedIds.push(data.userId);
        this.selectedNames.push(data.username);
      })
      // console.log(selectionDataArray)
    },
    // 批量删除用户
    batchDel() {
      if (this.selectedNames.length == 0) {
        messageTip("请勾选批量删除的用户！", "error")
        return;
      }
      messageConfirm("确认批量删除删除 " + this.selectedNames + " 吗？", "温馨提示").then(() => {
        // 将数组变成字符串，用逗号相隔
        let ids = this.selectedIds.join(",");
        // alert(ids)
        doDelete("/api/admin/sysUser/sysUsers", {ids: ids}).then((resp) => {
          if (resp.data.code === 200) {
            messageTip("已批量删除" + this.selectedNames, "success")
            this.reload();
          } else {
            messageTip("批量删除失败！原因：" + resp.data.msg, "error")
          }
        })
      }).catch(() => {
        // 用户点击取消就会触发 catch 里
        messageTip("已取消批量删除！", "warning")
      })
    },
    // 查询用户列表数据
    getData(current) {
      if (this.isSearch) {
        // console.log(formData);
        doGet("/api/admin/sysUser/searchUser", {
          current: current,
          selectKey: this.searchUser.selectKey,
          selectValue: this.searchUser.selectValue,
        }).then((resp) => {
          if (resp.data.code === 200) {
            console.log(resp.data.data.list);
            this.sysUserList = resp.data.data.list;
            this.myTotal = resp.data.data.total;
            this.myPageSize = resp.data.data.pageSize;
            messageTip("查询成功！", "success");
          } else {
            messageTip("查询失败！", "error");
          }
        })
      } else {
        doGet("/api/admin/sysUser/sysUsers", {
          // 当前页
          current: current
        }).then(resp => {
          console.log("!!!!!!!!!")
          console.log(resp)
          if (resp.data.code === 200) {
            this.sysUserList = resp.data.data.list;
            this.myTotal = resp.data.data.total;
            this.myPageSize = resp.data.data.pageSize;
          }
        })
      }
    },
    onCompStart() {
      this.isComposing = true;
    },
    onCompEnd() {
      // 结束合成后，下一次回车才算真正提交
      this.isComposing = false;
    },
    onKeyupEnter() {
      if (this.isComposing) return; // 中文输入法合成阶段的回车不触发搜索
      this.submitSearch();          // 等价于点击搜索按钮
    },
  },
  mounted() {
    this.getData(1);
    this.loadRoleOptions();
  },
  inject: ['reload'],
})

</script>

<style scoped>
.el-table {
  margin-top: 15px;
}

.el-pagination {
  margin-top: 20px;
}

.input-with-select .el-input-group__prepend {
  background-color: var(--el-fill-color-blank);
}

.mySearch {
  margin-left: 10px;
  margin-top: 20px;
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