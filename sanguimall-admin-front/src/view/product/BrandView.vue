<template>
  <!--两个按钮-->
  <el-button type="primary">添加品牌</el-button>
  <el-button type="danger">批量删除</el-button>
  <!--表格开始-->
  <el-table
      :data="brandList"
      style="width: 100%"
      @selection-change="handleSelectionChange">
    <el-table-column type="selection" width="60"/>
    <!--若 type 为 id，则该字段会自动增长-->
    <el-table-column type="index" label="序号" width="60"/>

    <el-table-column label="logo" width="200">
      <template #default="scope">
        <el-image

            :src="scope.row.logo"
            fit="none"
        />
      </template>
    </el-table-column>
    <el-table-column property="name" label="品牌名" width="120"/>
    <el-table-column property="descript" label="介绍" width="300">
      <template #default="scope">
      <span
          class="clickable-text"
          @click="viewDescriptDetail(scope.row.name,scope.row.descript)"
      >
        {{ getDescript(scope.row.descript) }}
      </span>
      </template>
    </el-table-column>

    <el-table-column property="firstLetter" label="检索首字母" width="120"/>
    <el-table-column property="sort" label="排序"/>
    <el-table-column property="showStatus" label="显示状态" width="240">
      <template #default="scope">
        <el-switch
            :model-value="scope.row.showStatus === 1"
            @change="val => handleShowStatusChange(scope.row.brandId, val)"
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
import {defineComponent} from "vue";
import {doGet, doPut} from "../../http/HttpRequest.js";
import {messageAlert, messageTip} from "../../util/util.js";

export default defineComponent({
  name: "BrandView",
  data() {
    return {
      // 定义 List 对象
      brandList: [{
        brandId: "",
        name: "",
        logo: "",
        descript: "",
        showStatus: 1,
        firstLetter: "",
        sort: 0
      }],
      myPageSize: 0,
      myTotal: 0,
    }
  },

  methods: {
    handleShowStatusChange(brandId, flag) {
      let formData = new FormData();
      formData.append("brandId", brandId)
      formData.append("flag", flag)
      doPut("/api/product/brand/brandStatus", formData).then((resp) => {
        if (resp.data.code === 200) {
          let messageStr = "已改为[" + (flag===true ? "显示" : "不显示") + "]状态！"
          messageTip(messageStr, "success");
          this.reload();
        } else {
          messageTip("修改失败！未知错误！", "error");
        }
      });
    },
    viewDescriptDetail(name, descript) {
      messageAlert(name, descript)
    },
    // 获取成员名称，超长截断
    getDescript(descript) {
      const maxLength = 30; // 超过20个字符就截断
      if (descript.length > maxLength) {
        return descript.slice(0, maxLength) + '...';
      }
      return descript;
    },
    toPage(current) {
      this.getData(current)
    },
    // 勾选或者取消勾选时触发该函数
    handleSelectionChange() {
      // 完成批量删除模块功能时再写这个方法
    },
    // 查询品牌列表数据
    getData(current) {
      doGet("/api/product/brand/brand", {
        // 当前页
        current: current
      }).then(resp => {
        if (resp.data.code === 200) {
          console.log(resp)
          this.brandList = resp.data.data.list;
          this.myTotal = resp.data.data.total;
          this.myPageSize = resp.data.data.pageSize;
        }
      })
    },
  },
  mounted() {
    this.getData(1);
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