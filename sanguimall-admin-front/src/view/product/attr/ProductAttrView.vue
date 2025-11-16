<template>
  <!--两个按钮-->
  <el-button type="primary">添加规格参数</el-button>
  <el-button type="danger">批量删除</el-button>
  <!--表格开始-->
  <el-table
      :data="attrList"
      style="width: 100%"
      @selection-change="handleSelectionChange">
    <el-table-column type="selection" width="60"/>
    <!--若 type 为 id，则该字段会自动增长-->
    <el-table-column type="index" label="序号" width="60"/>
    <el-table-column property="attrName" label="属性名" width="120"/>
    <el-table-column property="searchType" label="能否检索" width="100">
      <template #default="scope">
        <el-switch
            :model-value="scope.row.searchType === 1"
            @change="val => handleSearchTypeChange(scope.row.attrId, val,scope.row.attrName)"
            class="ml-2"
            style="--el-switch-on-color: #13ce66; --el-switch-off-color: #ff4949"
        />
      </template>
    </el-table-column>
    <el-table-column property="valueType" label="值类型" width="100">
      <template #default="scope">
        {{ getValueType(scope.row.valueType) }}
<!--        单值-->
<!--        <el-switch-->
<!--            :model-value="scope.row.valueType === 1"-->
<!--            @change="val => handleShowStatusChange(scope.row.brandId, val,scope.row.name)"-->
<!--            class="ml-2"-->
<!--            style="&#45;&#45;el-switch-on-color: #336666; &#45;&#45;el-switch-off-color: #C4E1E1"-->
<!--        />-->
<!--        多值-->
      </template>
    </el-table-column>
    <el-table-column property="icon" label="属性图标" width="100"/>
    <el-table-column property="valueSelect" label="可选值列表" width="240"/>
    <el-table-column property="attrType" label="属性类型" width="100">
      <template #default="scope">
        {{ getAttrTypeValue(scope.row.attrType) }}
      </template>
    </el-table-column>

    <el-table-column property="categoryVo.label" label="所属分类" width="200">
      <template #default="scope">
        {{ getCategoryDetailPath(scope.row.categoryVo.id) }}
      </template>
    </el-table-column>
    <el-table-column property="showDesc" label="是否快速展示" width="110">
      <template #default="scope">
        <el-switch
            :model-value="scope.row.showDesc === 1"
            @change="val => handleShowDescChange(scope.row.attrId, val,scope.row.attrName)"
            class="ml-2"
            style="--el-switch-on-color: #13ce66; --el-switch-off-color: #ff4949"
        />
      </template>
    </el-table-column>
    <el-table-column property="enable" label="启用状态" width="100">
      <template #default="scope">
        <el-switch
            :model-value="scope.row.enable === 1"
            @change="val => handleEnableChange(scope.row.attrId, val,scope.row.attrName)"
            class="ml-2"
            style="--el-switch-on-color: #13ce66; --el-switch-off-color: #ff4949"
        />
      </template>
    </el-table-column>
    <el-table-column label="操作">
      <el-button type="warning">编辑</el-button>
      <el-button type="danger">删除</el-button>
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
</template>

<script>
import {doGet, doPut} from "../../../http/HttpRequest.js";
import {messageTip} from "../../../util/util.js";

export default {
  name: "ProductAttrView",
  data() {
    return {
      // 定义 List 对象
      attrList: [
      //     {
      //   attrId: 0,
      //   attrName: "",
      //   searchType: 0,
      //   valueType: 0,
      //   icon: "",
      //   valueSelect: "",
      //   attrType: "",
      //   enable: 0,
      //   categoryVo: {
      //     id: 0,
      //     label: "",
      //   },
      //   showDesc: "",
      // }
      ],
      myPageSize: 0,
      myTotal: 0,
      // 新增：用来存 categoryId -> 分类路径
      // 比如 { 3: "手机/手机通讯/手机", 5: "家用电器/..." }
      categoryPathMap: {},
    }
  },
  methods: {
    // 勾选或者取消勾选时触发该函数
    handleSelectionChange() {
      // 完成批量删除模块功能时再写这个方法
    },
    // 查询用户列表数据
    getData(current) {
      doGet("/api/product/attr/attrs", {
        // 当前页
        current: current
      }).then(resp => {
        if (resp.data.code === 200) {
          console.log(resp)
          this.attrList = resp.data.data.list;
          this.myTotal = resp.data.data.total;
          this.myPageSize = resp.data.data.pageSize;
        }
      })
    },
    toPage(current) {
      this.getData(current)
    },
    getAttrTypeValue(type) {
      if (type === 0) {
        return "销售";
      } else if (type === 1) {
        return "基本";
      } else if (type === 2) {
        return "销售+基本"
      }
    },
    getCategoryDetailPath(categoryId) {
      if (!categoryId || categoryId === 0) {
        return "加载中...";
      }

      // 先发起异步加载（如果没加载过）
      this.loadCategoryDetailPath(categoryId);

      // 再返回当前已知的值，没有就先显示“加载中...”
      return this.categoryPathMap[categoryId] || "加载中...";
    },
    // 触发异步加载（不直接给模板用）
    loadCategoryDetailPath(categoryId) {
      if (!categoryId) return;

      // 如果已经有了就不用再请求
      if (this.categoryPathMap[categoryId]) return;

      let url = "/api/product/category/getCategoryPath/" + categoryId;

      doGet(url, {}).then(resp => {
        if (resp.data.code === 200) {

          // 原始值，例如："手机类;手机通讯;手机"
          const raw = resp.data.data;

          // ⭐ 在这里格式化为 “手机类 -> 手机通讯 -> 手机”
          const formatted = raw.split(";").join(" -> ");

          // 把格式化后的结果写进响应式对象里
          if (this.$set) {
            this.$set(this.categoryPathMap, categoryId, formatted);
          } else {
            this.categoryPathMap[categoryId] = formatted;
          }
        }
      })
    },
    handleSearchTypeChange(attrId, flag, attrName) {
      let formData = new FormData();
      formData.append("attrId", attrId)
      formData.append("flag", flag)
      doPut("/api/product/attr/searchType", formData).then((resp) => {
        if (resp.data.code === 200) {
          if (flag) {
            let messageStr = "[" + attrName + "] 已改为 [能够检索] 状态！"
            messageTip(messageStr, "success");
          } else {
            let messageStr = "[" + attrName + "] 已改为 [不能检索] 状态！"
            messageTip(messageStr, "error");
          }
          this.reload();
        } else {
          messageTip("修改失败！未知错误！", "error");
        }
      });
    },
    handleShowDescChange(attrId, flag, attrName) {
      let formData = new FormData();
      formData.append("attrId", attrId)
      formData.append("flag", flag)
      doPut("/api/product/attr/showDesc", formData).then((resp) => {
        if (resp.data.code === 200) {
          if (flag) {
            let messageStr = "[" + attrName + "] 已改为 [能够快速监视] 状态！"
            messageTip(messageStr, "success");
          } else {
            let messageStr = "[" + attrName + "] 已改为 [不能快速监视] 状态！"
            messageTip(messageStr, "error");
          }
          this.reload();
        } else {
          messageTip("修改失败！未知错误！", "error");
        }
      });
    },
    handleEnableChange(attrId, flag, attrName) {
      let formData = new FormData();
      formData.append("attrId", attrId)
      formData.append("flag", flag)
      doPut("/api/product/attr/enable", formData).then((resp) => {
        if (resp.data.code === 200) {
          if (flag) {
            let messageStr = "[" + attrName + "] 已改为 [启用] 状态！"
            messageTip(messageStr, "success");
          } else {
            let messageStr = "[" + attrName + "] 已改为 [不启用] 状态！"
            messageTip(messageStr, "error");
          }
          this.reload();
        } else {
          messageTip("修改失败！未知错误！", "error");
        }
      });
    },

    getValueType(valueType){
      if (valueType === 1){
        return "多值"
      }else {
        return "单值"
      }
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