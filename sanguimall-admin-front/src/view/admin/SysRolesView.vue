<template>
  <!--两个按钮-->
  <el-button type="primary" @click="add">添加角色</el-button>
  <!--表格开始-->
  <el-table
      :data="roleList"
      style="width: 100%">
    <!--若 type 为 id，则该字段会自动增长-->
    <el-table-column type="index" label="序号" width="80"/>
    <el-table-column property="roleName" label="角色名字" width="150"/>
    <el-table-column property="remark" label="角色标记" width="150"/>
    <el-table-column property="userCount" label="人数" width="150"/>
    <el-table-column property="userList.username" label="成员" width="300">
      <template #default="scope">
        <span>
          {{ getUsernames(scope.row.userList) }}
        </span>
      </template>
    </el-table-column>
    <el-table-column label="操作">
      <template #default="scope">
        <el-button type="primary" @click="view(scope.row.id)">详情</el-button>
        <el-button type="warning" @click="edit(scope.row.id)">编辑</el-button>
        <el-button type="danger"  @click="del(scope.row.id,scope.row.roleName,scope.row.userCount)">删除</el-button>
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
  <!--这是新增角色的弹窗-->
  <el-dialog v-model="addRoleWindows" :title="addRole.id>0?'编辑角色':'添加角色'" width="600" draggable>
    <el-form :model="addRole" label-width="110px" :rules="addRoleRules" ref="addRoleRefForm">
      <el-form-item label="角色名字" prop="roleName">
        <el-input v-model="addRole.roleName" placeholder="纯大写英文，如：ADMIN 、SALER"/>
      </el-form-item>

      <el-form-item label="角色标记" prop="remark">
        <el-input v-model="addRole.remark" placeholder="角色名字对应的中文标记，如：管理员、销售"/>
      </el-form-item>

    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="addRoleWindows = false">取消</el-button>
        <el-button type="primary" @click="addRoleSubmit">
          {{ addRole.id > 0 ? '编 辑' : '添 加' }}
        </el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script>
import {doDelete, doGet, doPost, doPut} from "../../http/HttpRequest.js";
import {messageConfirm, messageTip} from "../../util/util.js";

export default {
  name: "SysRoles",
  data() {
    return {
      // 定义 List 对象
      roleList: [
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
      ],
      myPageSize: 0,
      myTotal: 0,
      addRoleWindows: false,
      addRole: {
        id: 0,
        roleName: "",
        remark: "",
      },
      addRoleRules: {
        roleName: [
          {required: true, message: '请输入角色名字！', trigger: 'blur'},
          {max: 10, message: '角色名字长度不能超过10！', trigger: 'blur'},
          {pattern: /^[A-Z]+$/, message: '角色名字必须为纯英文大写字母！', trigger: 'blur'}
        ],
        remark: [
          {required: true, message: '请输入角色标记！', trigger: 'blur'},
          {max: 10, message: '角色标记长度不能超过10！', trigger: 'blur'},
          {pattern: /^[\u4e00-\u9fa5]+$/, message: '角色标记必须为纯中文！', trigger: 'blur'}
        ],
      },
    }
  },
  methods: {
    // 删除指定角色
    del(id, roleName,userCount) {
      if (userCount !== 0){
        messageTip("仅可删除人数为 0 的角色，删除失败！", "error")
        return;
      }
      messageConfirm("确认删除角色：" + roleName + " 吗？", "温馨提示").then(() => {
        let url = "/api/admin/sysRole/sysRole/" + id
        //alert(url)
        doDelete(url, {}).then((resp) => {
          if (resp.data.code === 200) {
            messageTip("已删除" + roleName, "success")
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
    // 编辑指定 id 的角色
    edit(id) {
      this.addRoleWindows = true;
      this.loadEditData(id);
    },
    // 编辑时加载对应 id 角色的数据
    loadEditData(id) {
      let url = "/api/admin/sysRole/sysRole/" + id
      console.log("编辑")
      doGet(url, {}).then((resp) => {
        if (resp.data.code === 200) {

          console.log(resp.data.data);
          this.addRole.id = resp.data.data.id;
          this.addRole.roleName = resp.data.data.roleName;
          this.addRole.remark = resp.data.data.remark;
        }
      })
    },
    // 提交新增角色
    addRoleSubmit() {
      this.$refs.addRoleRefForm.validate((isValid) => {
        if (isValid) {
          let formData = new FormData();
          // 以键值对的形式写入数据
          formData.append('roleName', this.addRole.roleName);
          formData.append('remark', this.addRole.remark);
          // console.log(formData);
          if (this.addRole.id > 0) {
            // console.log("走这里")
            formData.append('id', this.addRole.id);
            // 将编辑用户代码在此处写
            // doPut("/api/user", formData).then((resp) => {
            doPut("/api/admin/sysRole/sysRole", formData).then((resp) => {
              // console.log(resp.data.data);
              if (resp.data.code === 200) {
                messageTip("编辑角色成功！", "success");
                this.addRoleWindows = false;
                this.reload();
              } else {
                messageTip("编辑用户失败！请检查输入的条件！", "error");
              }
            })
          }else {
            doPost("/api/admin/sysRole/sysRole", formData).then((resp) => {
              if (resp.data.code === 200) {
                messageTip("添加角色成功！", "success");
                this.addRoleWindows = false;
                this.reload();
              } else {
                messageTip("添加角色失败！", "error");
              }
            })
          }
        }
      })
    },
    // 新增角色
    add() {
      this.addRole = {
        id: 0,
        roleName: "",
        remark: "",
      };
      this.addRoleWindows = true
    },
    // 跳转到指定 id 的用户信息界面
    view(roleId) {
      //alert(roleId);
      let url = "/dashboard/admin/role/" + roleId
      // alert(url)
      this.$router.push(url)
    },
    // 获取成员名称，超长截断
    getUsernames(userList) {
      const maxLength = 30; // 超过20个字符就截断
      const names = userList.map(user => user.username).join(', ');
      if (names.length > maxLength) {
        return names.slice(0, maxLength) + '...';
      }
      return names;
    },
    // 获取 current 页的角色信息，默认第一页
    getData(current) {
      doGet("/api/admin/sysRole/sysRoles", {
        // 当前页
        current: current
      }).then(resp => {
        if (resp.data.code === 200) {
          console.log(resp)
          this.roleList = resp.data.data.list;
          this.myTotal = resp.data.data.total;
          this.myPageSize = resp.data.data.pageSize;
        }
      })
    },
    // 前往 current 页的角色信息页
    toPage(current) {
      this.getData(current)
    },
  },
  mounted() {
    this.getData(1);
  },
  inject: ['reload'],
}
</script>

<style scoped>
.el-table {
  margin-top: 15px;
}

.el-pagination {
  margin-top: 20px;
}
</style>