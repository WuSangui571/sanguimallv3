<template>
  <div class="page-container">
    <div class="toolbar">
      <el-select
          v-model="selectedOneOptionsVo.id"
          placeholder="请选择一级分类"
          filterable
          style="width: 240px"
          @change="oneOptionsChange">
        <el-option
            v-for="item in oneOptionsData"
            :key="item.id"
            :label="item.name"
            :value="item.id"/>
      </el-select>
      <el-select
          v-model="selectedTwoOptionsVo.id"
          placeholder="请选择二级分类"
          filterable
          style="width: 240px"
          v-if="seeTwoOptionsFlag"
          @change="twoOptionsChange">
        <el-option
            v-for="item in twoOptionsData"
            :key="item.id"
            :label="item.name"
            :value="item.id"/>
      </el-select>
      <el-select
          v-model="selectedThreeOptionsVo.id"
          placeholder="请选择三级分类"
          filterable
          style="width: 240px"
          v-if="seeThreeOptionsFlag"
          @change="threeOptionsChange">
        <el-option
            v-for="item in threeOptionsData"
            :key="item.id"
            :label="item.name"
            :value="item.id"/>
      </el-select>
      <el-button type="primary" plain v-if="seeTwoOptionsFlag" @click="reset">重置</el-button>
    </div>
    <el-card class="content-card" shadow="hover" v-if="seeTable">
      <div class="section-title">属性分组</div>
      <div class="toolbar" style="gap: 10px; margin-bottom: 8px;">
        <el-button type="primary" plain @click="add">添加属性分组</el-button>
        <el-button type="danger" plain @click="batchDel">批量删除</el-button>
      </div>
      <el-table
          :data="attrGroupList"
          border
          stripe
          style="width: 100%"
          @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="60"/>
        <el-table-column type="index" label="序号" width="60"/>
        <el-table-column property="attrGroupName" label="组名" width="120"/>
        <el-table-column property="sort" label="排序" width="120"/>
        <el-table-column property="descript" label="描述" width="120"/>
        <el-table-column property="icon" label="组图标" width="240"/>
        <el-table-column label="操作">
          <template #default="scope">
            <el-button type="primary">关联</el-button>
            <el-button type="warning" @click="edit(scope.row.id)">编辑</el-button>
            <el-button type="danger" @click="del(scope.row.id,scope.row.attrGroupName)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
          background
          layout="prev, pager, next"
          :page-size=myPageSize
          :total=myTotal
          @prev-click="toPage"
          @current-change="toPage"
          @next-click="toPage"/>
    </el-card>
  </div>
<!--这是新增属性分组的弹窗-->
  <el-dialog v-model="addAttrGroupWindows" :title="addAttrGroup.id>0?'编辑属性分组':'添加属性分组'" width="600"
             draggable>
    <el-form :model="addAttrGroup" label-width="110px" :rules="addAttrGroupRules" ref="addAttrGroupRefForm">
      <el-form-item label="组名" prop="attrGroupName">
        <el-input v-model="addAttrGroup.attrGroupName"/>
      </el-form-item>

      <el-form-item label="排序" prop="sort">
        <el-input v-model="addAttrGroup.sort"/>
      </el-form-item>

      <el-form-item label="组图标" prop="icon">
        <el-input v-model="addAttrGroup.icon"/>
      </el-form-item>

      <el-form-item label="描述" prop="descript">
        <el-input v-model="addAttrGroup.descript"/>
      </el-form-item>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="addAttrGroupWindows = false">取消</el-button>
        <el-button type="primary" @click="addAttrGroupSubmit">
          {{ addAttrGroup.id > 0 ? '编 辑' : '添 加' }}
        </el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script>
import {doDelete, doGet, doPost, doPut} from "../../../http/HttpRequest.js";
import {messageConfirm, messageTip} from "../../../util/util.js";

export default {
  name: "ProductAttrGroupView",
  data() {
    return {
      seeTwoOptionsFlag: false,
      seeThreeOptionsFlag: false,
      seeTable: false,

      selectedOneOptionsVo: {
        id: "",
        name: "",
      },
      oneOptionsData: [{
        id: 0,
        name: "",
      }],
      selectedTwoOptionsVo: {
        id: "",
        name: "",
      },
      twoOptionsData: [{
        id: 0,
        name: "",
      }],
      selectedThreeOptionsVo: {
        id: "",
        name: "",
      },
      threeOptionsData: [{
        id: 0,
        name: "",
      }],
      attrGroupList: [{
        id: 0,
        attrGroupName: "",
        sort: 0,
        descript: "",
        icon: "",
      }],
      myPageSize: 0,
      myTotal: 0,
      addAttrGroupWindows: false,
      addAttrGroup: {
        id: 0,
        attrGroupName: "",
        sort: 0,
        descript: "",
        icon: "",
      },
      addAttrGroupRules: {
        attrGroupName: [
          {required: true, message: '请输入组名！', trigger: 'blur'},
          {max: 20, message: '组名的长度在 20 之内！', trigger: 'blur'},
        ],
        sort: [
          {required: true, message: '请输入排序！', trigger: 'blur'},
          {pattern: /^(?:0|[1-9][0-9]{0,2}|1000)$/, message: '排序必须是不小于0，不大于 1000的整数！', trigger: 'blur'},
        ],
        descript: [
          {required: true, message: '请输入组描述！', trigger: 'blur'},
          {max: 255, message: '组描述的长度在 255 之内！', trigger: 'blur'},
        ],
        icon: [
          {max: 255, message: '组图标的长度在 255 之内！', trigger: 'blur'},
        ],
      },
      selectedIds: [],
      selectedAttrGroupNames: [],
    }
  },
  methods: {
    getOneOptionData() {
      doGet("/api/product/category/one", {}).then(resp => {
        //console.log(resp.data.data);
        if (resp.data.code === 200) {
          this.oneOptionsData = resp.data.data;
        }
      })
    },
    oneOptionsChange() {
      if (this.seeTwoOptionsFlag === true) {
        this.selectedTwoOptionsVo = {
          id: 0,
          name: "",
        };
        this.twoOptionsData = [{
          id: 0,
          name: "",
        }];
        this.selectedThreeOptionsVo = {
          id: 0,
          name: "",
        };
        this.threeOptionsData = [{
          id: 0,
          name: "",
        }];
        this.seeTable = false;
      }
      this.seeTwoOptionsFlag = true;
      this.getTwoOptionsData();
    },
    getTwoOptionsData() {
      doGet("/api/product/category/two", {
        oneOptionsId: this.selectedOneOptionsVo.id
      }).then(resp => {
        //console.log(resp.data.data);
        if (resp.data.code === 200) {
          this.twoOptionsData = resp.data.data;
        }
      })
    },
    twoOptionsChange() {
      if (this.seeThreeOptionsFlag === true) {
        this.selectedThreeOptionsVo = {
          id: 0,
          name: "",
        };
        this.threeOptionsData = [{
          id: 0,
          name: "",
        }];
        this.seeTable = false;
      }
      this.seeThreeOptionsFlag = true;
      this.getThreeOptionsData();
    },
    getThreeOptionsData() {
      doGet("/api/product/category/three", {
        twoOptionsId: this.selectedTwoOptionsVo.id
      }).then(resp => {
        //console.log(resp.data.data);
        if (resp.data.code === 200) {
          this.threeOptionsData = resp.data.data;
        }
      })
    },
    threeOptionsChange() {
      this.seeTable = true;
      this.getTableData(1);
    },
    reset() {
      this.seeTwoOptionsFlag = false;
      this.seeThreeOptionsFlag = false;
      this.selectedOneOptionsVo = {
        id: 0,
        name: "",
      };
      this.selectedTwoOptionsVo = {
        id: 0,
        name: "",
      };
      this.twoOptionsData = [{
        id: 0,
        name: "",
      }];
      this.selectedThreeOptionsVo = {
        id: 0,
        name: "",
      };
      this.threeOptionsData = [{
        id: 0,
        name: "",
      }];
      this.seeTable = false;
    },
    // 勾选或者取消勾选时触发该函数
    handleSelectionChange(selectionDataArray) {
      // console.log(selectionDataArray)
      // 清空 Ids、Names 数组
      this.selectedIds = [];
      this.selectedAttrGroupNames = [];
      // 遍历数组
      selectionDataArray.forEach(data => {
        // 遍历数组中的元素，将 id、names 加入统一的数组
        this.selectedIds.push(data.id);
        this.selectedAttrGroupNames.push(data.attrGroupName);
      })
      // console.log(selectionDataArray)
    },
    // 查询用户列表数据
    getTableData(current) {
      doGet("/api/product/attrGroup/attrGroups", {
        // 当前页
        current: current,
        catelogId: this.selectedThreeOptionsVo.id,
      }).then(resp => {
        if (resp.data.code === 200) {
          // console.log(resp)
          this.attrGroupList = resp.data.data.list;
          this.myTotal = resp.data.data.total;
          this.myPageSize = resp.data.data.pageSize;
        }
      })
    },
    toPage(current) {
      this.getData(current)
    },
    // 提交新增 属性分组
    addAttrGroupSubmit() {
      this.$refs.addAttrGroupRefForm.validate((isValid) => {
        if (isValid) {
          // console.log("passed");
          let formData = new FormData();
          // 以键值对的形式写入数据
          formData.append('attrGroupName', this.addAttrGroup.attrGroupName);
          formData.append('sort', this.addAttrGroup.sort);
          formData.append('descript', this.addAttrGroup.descript);
          formData.append('icon', this.addAttrGroup.icon);
          formData.append('catelogId', this.selectedThreeOptionsVo.id);
          // console.log(formData);
          if (this.addAttrGroup.id > 0){
            formData.append('id', this.addAttrGroup.id);
            doPut("/api/product/attrGroup/attrGroup", formData).then((resp) => {
              // console.log(resp.data.data);
              if (resp.data.code === 200) {
                messageTip("编辑属性分组成功！", "success");
                this.addAttrGroupWindows = false;
                this.getTableData(1);
              } else {
                messageTip("编辑属性分组失败！请检查输入的条件！", "error");
              }
            })
          }else {
            doPost("/api/product/attrGroup/attrGroup", formData).then((resp) => {
              if (resp.data.code === 200) {
                messageTip("添加属性分组成功！", "success");
                this.addAttrGroupWindows = false
                this.getTableData(1);
              } else {
                messageTip("添加属性分组失败！", "error");
              }
            })
          }
        }
      })
    },
    // 新增属性分组
    add() {
      this.addAttrGroup.id = 0;
      this.addAttrGroup.attrGroupName = "";
      this.addAttrGroup.sort = 0;
      this.addAttrGroup.descript = "";
      this.addAttrGroup.icon = "";
      this.addAttrGroupWindows = true
    },
    // 编辑指定 id 的属性分类
    edit(id) {
      this.addAttrGroupWindows = true;
      this.loadEditData(id);
    },
    // 编辑时加载对应 id 属性分类的数据
    loadEditData(id) {
      let url = "/api/product/attrGroup/attrGroup/" + id
      doGet(url, {}).then((resp) => {
        if (resp.data.code === 200) {
          this.addAttrGroup.id = resp.data.data.id;
          this.addAttrGroup.attrGroupName = resp.data.data.attrGroupName;
          this.addAttrGroup.sort = resp.data.data.sort;
          this.addAttrGroup.descript = resp.data.data.descript;
          this.addAttrGroup.icon = resp.data.data.icon;
        }
      })
    },
    // 删除指定用户
    del(id, attrGroupName) {
      messageConfirm("确认删除 " + attrGroupName + " 吗？", "温馨提示").then(() => {
        let url = "/api/product/attrGroup/attrGroup/" + id
        //alert(url)
        doDelete(url, {}).then((resp) => {
          if (resp.data.code === 200) {
            messageTip("已删除" + attrGroupName, "success")
            this.getTableData(1);
          } else {
            messageTip("删除失败！", "error")
          }
        })
      }).catch(() => {
        // 用户点击取消就会触发 catch 里
        messageTip("已取消删除！", "error")
      })
    },
    // 批量删除用户
    batchDel() {
      if (this.selectedAttrGroupNames.length === 0) {
        messageTip("请勾选批量删除的属性分组！", "error")
        return;
      }
      messageConfirm("确认批量删除 " + this.selectedAttrGroupNames + " 吗？", "温馨提示").then(() => {
        // 将数组变成字符串，用逗号相隔
        let ids = this.selectedIds.join(",");
        // alert(ids)
        doDelete("/api/product/attrGroup/attrGroups", {ids: ids}).then((resp) => {
          if (resp.data.code === 200) {
            messageTip("已批量删除" + this.selectedAttrGroupNames, "success")
            this.getTableData(1);
          } else {
            messageTip("批量删除失败！原因：" + resp.data.msg, "error")
          }
        })
      }).catch(() => {
        // 用户点击取消就会触发 catch 里
        messageTip("已取消批量删除！", "warning")
      })
    },
  },
  mounted() {
    this.getOneOptionData();
  },
  inject: ['reload'],
}
</script>

<style scoped>
.myTable {
  margin-top: 20px;
}

.el-table {
  margin-top: 15px;
}

.el-pagination {
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
