<template>
  <!--两个按钮-->
  <el-button type="primary" @click="add">添加规格参数</el-button>
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
    <el-table-column property="valueSelect" label="可选值列表" width="180">
      <template #default="{ row }">
        <span
            class="clickable-text"
            @click="viewTagDetail(row.attrName,row.valueSelect)">
          <!-- 展示前两个（或不足两个）标签 -->
          <el-tag
              v-for="(tag, index) in getDisplayTags(row.valueSelect)"
              :key="index"
              class="mx-1"
              size="small"
              effect="plain"
              :disable-transitions="true"
          >
            {{ tag }}
          </el-tag>
          <!-- 如果还有更多，就显示三个点 -->
          <span v-if="hasMoreTags(row.valueSelect)">...</span>
        </span>
      </template>
    </el-table-column>
    <el-table-column property="attrType" label="属性类型" width="100">
      <template #default="{ row }">
        <el-tag
            :type="getAttrTypeTagType(row.attrType)"
            size="small"
            effect="plain"
            :disable-transitions="true"
        >
          {{ getAttrTypeValue(row.attrType) }}
        </el-tag>
      </template>
    </el-table-column>

    <el-table-column property="categoryVo.label" label="所属分类" width="220">
      <template #default="{ row }">
        <template v-if="getCategoryPathList(row.categoryVo.id).length">
          <el-breadcrumb separator="/">
            <el-breadcrumb-item
                v-for="(item, index) in getCategoryPathList(row.categoryVo.id)"
                :key="index"
            >
              {{ item }}
            </el-breadcrumb-item>
          </el-breadcrumb>
        </template>
        <span v-else>加载中...</span>
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

  <!-- 查看完整标签的弹窗 -->
  <el-dialog
      v-model="tagDialogVisible"
      :title="tagDialogTitle"
      width="400px"
  >
    <div v-if="tagDialogTags.length">
      <el-tag
          v-for="(tag, index) in tagDialogTags"
          :key="index"
          class="mx-1"
          size="default"
          effect="plain"
      >
        {{ tag }}
      </el-tag>
    </div>
    <div v-else>
      暂无可选值
    </div>

    <template #footer>
    <span class="dialog-footer">
      <el-button @click="tagDialogVisible = false">关 闭</el-button>
    </span>
    </template>
  </el-dialog>


  <!--这是新增规格参数的弹窗-->
  <el-dialog v-model="addAttrWindows" title="添加规格参数" width="600" draggable>
    <el-form :model="addAttr" label-width="110px" :rules="addAttrRules" ref="addAttrRefForm">
      <el-form-item label="所属分类" prop="selectedThreeOptionsVo.name">
        <el-select
            v-model="selectedOneOptionsVo.id"
            placeholder="一级分类"
            filterable
            style="width: 120px"
            @change="oneOptionsChange">
          <el-option
              v-for="item in oneOptionsData"
              :key="item.id"
              :label="item.name"
              :value="item.id"/>
        </el-select>
        <el-select
            v-model="selectedTwoOptionsVo.id"
            placeholder="二级分类"
            filterable
            style="width: 120px"
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
            placeholder="三级分类"
            filterable
            style="width: 120px"
            v-if="seeThreeOptionsFlag"
            @change="threeOptionsChange">
          <el-option
              v-for="item in threeOptionsData"
              :key="item.id"
              :label="item.name"
              :value="item.id"/>
        </el-select>
        <el-button type="primary" v-if="seeTwoOptionsFlag" @click="reset">重置</el-button>
      </el-form-item>

      <el-form-item label="属性名" prop="attrName">
        <el-input v-model="addAttr.attrName" placeholder="请输入属性名"/>

      </el-form-item>

      <el-form-item label="能否检索" prop="searchType">
        <el-switch
            v-model="addAttr.searchType"
            :active-value="1"
            :inactive-value="0"
            class="ml-2"
            style="--el-switch-on-color: #13ce66; --el-switch-off-color: #ff4949"
        />
      </el-form-item>

      <el-form-item label="值类型" prop="valueType">
        &nbsp (单值) &nbsp
        <el-switch
            v-model="addAttr.valueType"
            :active-value="1"
            :inactive-value="0"
            class="ml-2"
            style="--el-switch-on-color: #003D79; --el-switch-off-color: #D2E9FF"
        />
        &nbsp (多值) &nbsp
      </el-form-item>

      <el-form-item label="属性图标" prop="icon">
        <el-input v-model="addAttr.icon" placeholder="请输入属性图标"/>
      </el-form-item>

      <el-form-item label="可选值列表" prop="valueSelect">
        <el-input-tag
            v-model="addAttr.valueSelect"
            placeholder="可输入多个，以「回车键」分隔"
            aria-label="Please click the Enter key after input"
        />
      </el-form-item>

      <el-form-item label="属性类型" prop="attrTypeVo.id">
        <el-select
            v-model="addAttr.attrTypeVo.id"
            placeholder="请选择属性类型"
            clearable
            filterable>
          <!--注意下面，key 代表的是传给后端的值，label 代表的是展示给前端的值，value 表示的是下拉框改变时，绑定到 v-model 的值-->
          <el-option
              v-for="item in addAttrTypeOption"
              :key="item.id"
              :label="item.name"
              :value="item.id"/>
        </el-select>
      </el-form-item>

      <el-form-item label="启用状态" prop="enable">
        <el-switch
            v-model="addAttr.enable"
            :active-value="1"
            :inactive-value="0"
            class="ml-2"
            style="--el-switch-on-color: #13ce66; --el-switch-off-color: #ff4949"
        />
      </el-form-item>

      <el-form-item label="能否快速展示" prop="showDesc">
        <el-switch
            v-model="addAttr.showDesc"
            :active-value="1"
            :inactive-value="0"
            class="ml-2"
            style="--el-switch-on-color: #13ce66; --el-switch-off-color: #ff4949"
        />
      </el-form-item>

    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="addAttrWindows = false">取消</el-button>
        <el-button type="primary" @click="addAttrSubmit">
          添加
        </el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script>
import {doGet, doPost, doPut} from "../../../http/HttpRequest.js";
import {messageTip} from "../../../util/util.js";

export default {
  name: "ProductAttrView",
  data() {
    return {
      // 定义 List 对象
      attrList: [],
      myPageSize: 0,
      myTotal: 0,
      // 新增：用来存 categoryId -> 分类路径
      // 比如 { 3: "手机/手机通讯/手机", 5: "家用电器/..." }
      categoryPathMap: {},

      // 👉 新增：标签弹窗相关
      tagDialogVisible: false,
      tagDialogTitle: '',
      tagDialogTags: [],

      addAttrWindows: false,
      addAttr: {
        attrId: 0,
        attrName: "",
        searchType: 0,
        valueType: 1,
        valueSelect: "",
        icon: "",
        attrTypeVo: {
          id: "",
          name: "",
        },
        enable: 1,
        categoryId: 0,
        showDesc: 0,
      },
      addAttrTypeOption: [],

      seeTwoOptionsFlag: false,
      seeThreeOptionsFlag: false,
      selectedOneOptionsVo: {
        id: "",
        name: "",
      },
      oneOptionsData: [{
        id: "",
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
      selectThreeOptionsOver: false,
      addAttrRules: {
        attrName: [
          {required: true, message: '属性名不能为空！', trigger: 'blur'},
        ],
        'attrTypeVo.id': [
          {required: true, message: '属性类型不能为空！', trigger: 'blur'},
        ],
        'selectedThreeOptionsVo.name': [
          {required: true, message: '务必选择所属分类！', trigger: 'blur'},
        ],
        // icon: [
        //   {required: true, message: '图标不能为空！', trigger: 'blur'},
        //   {min: 6, max: 16, message: '密码的长度在 6-16 之间！', trigger: 'blur'},
        // ],
      },
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
      return "未知";
    },
    // 新增：不同类型使用不同颜色的 tag
    getAttrTypeTagType(type) {
      // Element Plus 内置的几种 type：success / info / warning / danger
      if (type === 0) {
        return "success";   // 绿色：销售
      } else if (type === 1) {
        return "info";      // 蓝色：基本
      } else if (type === 2) {
        return "warning";   // 橙色：销售+基本
      }
      return "";            // 默认样式
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

          // 在这里格式化为 “手机类 -> 手机通讯 -> 手机”
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

    // 👉 新增：给 Breadcrumb 用，返回数组
    getCategoryPathList(categoryId) {
      if (!categoryId || categoryId === 0) {
        return [];
      }

      // 确保已经触发异步加载
      this.loadCategoryDetailPath(categoryId);

      const text = this.categoryPathMap[categoryId];
      if (!text) return [];

      // 现在 text 形如："手机类 -> 手机通讯 -> 手机"
      return text
          .split("->")
          .map(v => v.trim())
          .filter(v => v);
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
            let messageStr = "[" + attrName + "] 已改为 [能够快速展示] 状态！"
            messageTip(messageStr, "success");
          } else {
            let messageStr = "[" + attrName + "] 已改为 [不能快速展示] 状态！"
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

    getValueType(valueType) {
      if (valueType === 1) {
        return "多值"
      } else {
        return "单值"
      }
    },

    // 把 "黑色;白色;蓝色" 这种字符串拆成数组
    parseValueSelect(value) {
      if (!value) return []
      return value
          .split(';')           // 用分号切分
          .map(v => v.trim())   // 去掉前后空格
          .filter(v => v)       // 过滤掉空字符串
    },

    // 输出“用于展示”的标签列表（最多两个）
    getDisplayTags(value) {
      const list = this.parseValueSelect(value)
      if (list.length <= 2) {
        return list           // 不超过两个就都展示
      }
      return list.slice(0, 2) // 超过两个只展示前两个
    },

    // 判断是否需要显示 "..."
    hasMoreTags(value) {
      const list = this.parseValueSelect(value)
      return list.length > 2
    },

    viewTagDetail(name, tagValue) {
      // 设置标题，比如“颜色 - 全部可选值”
      this.tagDialogTitle = `${name} - 全部可选值列表`

      // 使用已有的函数解析成标签数组（不改原数据）
      this.tagDialogTags = this.parseValueSelect(tagValue)

      // 打开弹窗
      this.tagDialogVisible = true
    },
    // 提交新增规格参数
    addAttrSubmit() {
      this.$refs.addAttrRefForm.validate((isValid) => {
        if (isValid) {
          // console.log("passed");
          let formData = new FormData();
          // 以键值对的形式写入数据
          formData.append('attrNamee', this.addAttr.attrName);
          //console.log("attrName=" + this.addAttr.attrName)
          formData.append('searchType', this.addAttr.searchType);
          //console.log("searchType=" + this.addAttr.searchType)
          formData.append('valueType', this.addAttr.valueType);
          //console.log("valueType=" + this.addAttr.valueType)
          formData.append('valueSelect', this.addAttr.valueSelect);
          //console.log("valueSelect=" + this.addAttr.valueSelect)
          formData.append('attrType', this.addAttr.attrTypeVo.id);
          //console.log("attrTypeVo.id=" + this.addAttr.attrTypeVo.id)
          formData.append('enable', this.addAttr.enable);
          //console.log("enable=" + this.addAttr.enable)
          formData.append('categoryId', this.selectedThreeOptionsVo.id);
          console.log("categoryId=" + this.selectedThreeOptionsVo.id)
          formData.append('showDesc', this.addAttr.showDesc);
          //console.log("showDesc=" + this.addAttr.showDesc)

          //console.log(formData);
          // doPost("/api/user", formData).then((resp) => {
          //   if (resp.data.code === 200){
          //     messageTip("添加用户成功！","success");
          //     this.addUserWindows = false;
          //   }else{
          //     messageTip("添加用户失败！","error");
          //   }
          // })
        }
      })
    },
    // 新增规格参数
    add() {
      this.addAttrWindows = true
    },

    getOneOptionData() {
      doGet("/api/product/category/one", {}).then(resp => {
        if (resp.data.code === 200) {
          this.oneOptionsData = resp.data.data;
        }
      })
    },
    oneOptionsChange() {
      if (this.seeTwoOptionsFlag === true) {
        this.selectedTwoOptionsVo = {
          id: "",
          name: "",
        };
        this.twoOptionsData = [{
          id: "",
          name: "",
        }];
        this.selectedThreeOptionsVo = {
          id: "",
          name: "",
        };
        this.threeOptionsData = [{
          id: "",
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
        //console.log("二级菜单数据");
        //console.log(resp.data.data);
        if (resp.data.code === 200) {
          this.twoOptionsData = resp.data.data;
        }
      })
    },
    twoOptionsChange() {
      if (this.seeThreeOptionsFlag === true) {
        this.selectedThreeOptionsVo = {
          id: "",
          name: "",
        };
        this.threeOptionsData = [{
          id: "",
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
      this.selectThreeOptionsOver = true;

    },
    reset() {
      this.seeTwoOptionsFlag = false;
      this.seeThreeOptionsFlag = false;
      this.selectThreeOptionsOver = false;
      this.selectedOneOptionsVo = {
        id: "",
        name: "",
      };
      this.selectedTwoOptionsVo = {
        id: "",
        name: "",
      };
      this.twoOptionsData = [{
        id: "",
        name: "",
      }];
      this.selectedThreeOptionsVo = {
        id: "",
        name: "",
      };
      this.threeOptionsData = [{
        id: "",
        name: "",
      }];

    },
    loadAddAttrTypeOption() {
      doGet("/api/product/attr/attrTypeOption", {}).then(resp => {
        if (resp.data.code === 200) {
          this.addAttrTypeOption = resp.data.data;
        }
      })
    }

  },
  mounted() {
    this.getData(1);
    this.getOneOptionData();
    this.loadAddAttrTypeOption();
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