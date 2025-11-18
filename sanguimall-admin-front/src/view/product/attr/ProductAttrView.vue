<template>
  <div class="page-container">
    <div class="toolbar">
      <el-button type="primary" plain @click="add">添加规格参数</el-button>
      <el-button type="danger" plain @click="batchDel">批量删除</el-button>
      <div class="mySearch">
        <el-form :model="searchAttr" :rules="searchAttrRules" ref="searchAttrRefForm">
          <el-form-item prop="selectValue">
            <el-input
                v-model="searchAttr.selectValue"
                style="max-width: 600px"
                placeholder="请输入具体的模糊查询"
                class="input-with-select"
                @keydown.enter.prevent
                @keyup.enter="onKeyupEnter"
                @compositionstart="onCompStart"
                @compositionend="onCompEnd"
            >
              <template #append>
                <el-button :icon="Search" @click="submitSearch"/>
              </template>
            </el-input>
          </el-form-item>
        </el-form>
      </div>
      <el-button type="success" plain @click="resetSearch">重置</el-button>
    </div>
    <el-card class="content-card" shadow="hover">
      <div class="section-title">规格参数列表</div>
      <el-table
          :data="attrList"
          border
          stripe
          style="width: 100%"
          @selection-change="handleSelectionChange"
          row-key="attrId">
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
        <template v-if="row.categoryVo && getCategoryPathList(row.categoryVo.id).length">
          <el-breadcrumb separator="/">
            <el-breadcrumb-item
                v-for="(item, index) in getCategoryPathList(row.categoryVo.id)"
                :key="index"
            >
              {{ item }}
            </el-breadcrumb-item>
          </el-breadcrumb>
        </template>
        <span v-else-if="row.categoryVo">加载中...</span>
        <span v-else>暂无分类</span>
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
      <template #default="scope">
        <el-button type="warning" @click="edit(scope.row.attrId)">编辑</el-button>
        <el-button type="danger" @click="del(scope.row.attrId,scope.row.attrName)">删除</el-button>
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
  </div>

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
  <el-dialog
      v-model="addAttrWindows"
      :title="addAttr.attrId > 0 ? '编辑规格参数' : '添加规格参数'"
      width="600"
      draggable>
    <el-form :model="addAttr" label-width="110px" :rules="addAttrRules" ref="addAttrRefForm">
      <el-form-item label="所属分类" prop="categoryVo.id">
        <el-select
            v-model="selectedOneOptionsVo.id"
            placeholder="一级分类"
            filterable
            style="width: 120px"
            @change="oneOptionsChange"
            :validate-event="false">
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
            :validate-event="false"
            @change="twoOptionsChange">
          <el-option
              v-for="item in twoOptionsData"
              :key="item.id"
              :label="item.name"
              :value="item.id"/>
        </el-select>
<!--      <el-form-item label="所属分类" prop="selectedThreeOptionsVo.id">-->
        <el-select
            v-model="addAttr.categoryVo.id"
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



      <el-form-item label="能否快速展示" prop="showDesc">
        <el-switch
            v-model="addAttr.showDesc"
            :active-value="1"
            :inactive-value="0"
            class="ml-2"
            style="--el-switch-on-color: #13ce66; --el-switch-off-color: #ff4949"
        />
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

    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="addAttrWindows = false">取消</el-button>
        <el-button type="primary" @click="addAttrSubmit">
          {{ addAttr.attrId > 0 ? '保存' : '添加' }}
        </el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script>
import {doDelete, doGet, doPost, doPut} from "../../../http/HttpRequest.js";
import {messageConfirm, messageTip} from "../../../util/util.js";
import {Search} from "@element-plus/icons-vue";

export default {
  name: "ProductAttrView",
  computed: {
    Search() {
      return Search;
    }
  },
  data() {
    return {
      // 定义 List 对象
      attrList: [],
      currentPage: 1,
      myPageSize: 0,
      myTotal: 0,
      searchAttr: {
        selectValue: "",
      },
      searchAttrRules: {
        selectValue: [
          {min: 1, max: 50, message: '关键字长度应在 1 到 50 个字符之间！', trigger: 'blur'},
          {pattern: /^[\u4e00-\u9fa5A-Za-z0-9_ /+]+$/, message: '只允许输入中文、英文、数字、下划线、空格、斜杠或加号！', trigger: 'blur'}
        ]
      },
      isSearch: false,
      isComposing: false,
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
        valueSelect: [],
        icon: "",
        attrTypeVo: {
          id: "",
          name: "",
        },
        enable: 1,
        categoryVo: {
          id:'',
          name:"",
        },
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
      // selectedThreeOptionsVo: {
      //   id: "",
      //   name: "",
      // },
      threeOptionsData: [{
        id: 0,
        name: "",
      }],
      selectThreeOptionsOver: false,
      selectedAttrIds: [],
      selectedAttrNames: [],
      addAttrRules: {
        attrName: [
          {required: true, message: '属性名不能为空！', trigger: 'blur'},
        ],
        'attrTypeVo.id': [
          {required: true, message: '属性类型不能为空！', trigger: 'blur'},
        ],
        'categoryVo.id': [
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
    handleSelectionChange(selectionDataArray) {
      this.selectedAttrIds = []
      this.selectedAttrNames = []
      selectionDataArray.forEach(data => {
        this.selectedAttrIds.push(data.attrId)
        this.selectedAttrNames.push(data.attrName)
      })
    },
    // 查询用户列表数据
    async getData(current) {
      this.currentPage = current || 1;
      this.attrList = [{
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
        categoryVo: {
          id:'',
          name:"",
        },
        showDesc: 0,
      }];
      if (this.isSearch) {
        doGet("/api/product/attr/search", {
          current: this.currentPage,
          selectValue: this.searchAttr.selectValue,
        }).then(resp => {
          if (resp.data.code === 200) {
            const list = resp.data.data.list || [];
            if (list.length === 0) {
              messageTip("未找到匹配数据，已为你恢复全部列表。", "warning");
              this.isSearch = false;
              // 恢复全量数据
              this.getData(1);
              return;
            }
            this.attrList = list;
            this.myTotal = resp.data.data.total;
            this.myPageSize = resp.data.data.pageSize;
            messageTip("查询成功！", "success");
          } else {
            messageTip("查询失败！", "error");
          }
        });
      } else {
        doGet("/api/product/attr/attrs", {
          // 当前页
          current: this.currentPage
        }).then(resp => {
          if (resp.data.code === 200) {
            console.log(resp)
            this.attrList = resp.data.data.list;
            this.myTotal = resp.data.data.total;
            this.myPageSize = resp.data.data.pageSize;
          }
        })
      }
    },
    submitSearch() {
      const keyword = (this.searchAttr.selectValue || "").trim();
      if (keyword === "") {
        messageTip("请输入查询条件！", "error");
        return;
      }
      this.searchAttr.selectValue = keyword;
      this.$refs.searchAttrRefForm.validate((isValid) => {
        if (isValid) {
          this.isSearch = true;
          this.getData(1);
        }
      })
    },
    resetSearch() {
      this.isSearch = false;
      if (this.$refs.searchAttrRefForm) {
        this.$refs.searchAttrRefForm.resetFields();
      }
      this.searchAttr.selectValue = "";
      this.getData(1);
    },
    onCompStart() {
      this.isComposing = true;
    },
    onCompEnd() {
      this.isComposing = false;
    },
    onKeyupEnter() {
      if (this.isComposing) return;
      this.submitSearch();
    },
    toPage(current) {
      this.currentPage = current;
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
          this.getData(this.currentPage);
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
          this.getData(this.currentPage);
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
          this.getData(this.currentPage);
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
        if (Array.isArray(value)) {
          return value.map(v => (v || "").trim()).filter(v => v)
        }
      return value
          .split(";")           // 用分号切开
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
    batchDel() {
      if (this.selectedAttrIds.length === 0) {
        messageTip("请勾选要批量删除的规格参数！", "error")
        return;
      }
      messageConfirm("确认批量删除 " + this.selectedAttrNames + " 吗？", "温馨提示").then(() => {
        let ids = this.selectedAttrIds.join(",");
        doDelete("/api/product/attr/attrs", {ids: ids}).then((resp) => {
          if (resp.data.code === 200) {
            messageTip("已批量删除 " + this.selectedAttrNames, "success")
            this.getData(this.currentPage);
          } else {
            messageTip("批量删除失败！原因：" + resp.data.msg, "error")
          }
        })
      }).catch(() => {
        messageTip("已取消批量删除！", "warning")
      })
    },
    del(attrId, attrName) {
      messageConfirm("确认删除 " + attrName + " 吗？", "温馨提示").then(() => {
        let url = "/api/product/attr/attr/" + attrId;
        doDelete(url, {}).then((resp) => {
          if (resp.data.code === 200) {
            messageTip("已删除 " + attrName, "success")
            this.getData(this.currentPage);
          } else {
            messageTip("删除失败！原因：" + resp.data.msg, "error")
          }
        })
      }).catch(() => {
        messageTip("已取消删除！", "warning")
      })
    },
    edit(id) {
      this.resetAddAttrForm();
      this.addAttrWindows = true;
      this.loadEditData(id);
    },
    loadEditData(id) {
      doGet(`/api/product/attr/attr/${id}`, {}).then((resp) => {
        if (resp.data.code === 200) {
          const data = resp.data.data;
          this.addAttr.attrId = data.attrId;
          this.addAttr.attrName = data.attrName;
          this.addAttr.searchType = data.searchType;
          this.addAttr.valueType = data.valueType;
          this.addAttr.valueSelect = this.parseValueSelect(data.valueSelect);
          this.addAttr.icon = data.icon;
          this.addAttr.attrTypeVo.id = data.attrType;
          this.addAttr.enable = data.enable;
          this.addAttr.categoryVo.id = data.threeCategoryId || data.catelogId;
          this.addAttr.showDesc = data.showDesc;

          this.selectedOneOptionsVo.id = data.oneCategoryId || "";
          this.selectedTwoOptionsVo.id = data.twoCategoryId || "";
          this.seeTwoOptionsFlag = !!this.selectedOneOptionsVo.id;
          this.seeThreeOptionsFlag = !!this.selectedTwoOptionsVo.id;

          if (this.seeTwoOptionsFlag) {
            this.getTwoOptionsData().then(() => {
              if (this.seeThreeOptionsFlag) {
                this.getThreeOptionsData().then(() => {
                  this.selectThreeOptionsOver = true;
                });
              }
            });
          }
        }
      })
    },
    // 提交新增规格参数
    addAttrSubmit() {
      this.$refs.addAttrRefForm.validate((isValid) => {
        if (isValid) {
          // console.log("passed");
          let formData = new FormData();
          // 以键值对的形式写入数据
          formData.append('attrName', this.addAttr.attrName);
          //console.log("attrName=" + this.addAttr.attrName)
          formData.append('searchType', this.addAttr.searchType);
          //console.log("searchType=" + this.addAttr.searchType)
          formData.append('valueType', this.addAttr.valueType);
          //console.log("valueType=" + this.addAttr.valueType)
          formData.append('valueSelect', Array.isArray(this.addAttr.valueSelect) ? this.addAttr.valueSelect.join(";") : this.addAttr.valueSelect);
          formData.append('icon', this.addAttr.icon);
          formData.append('attrType', this.addAttr.attrTypeVo.id);
          //console.log("attrTypeVo.id=" + this.addAttr.attrTypeVo.id)
          formData.append('enable', this.addAttr.enable);
          console.log("enable=" + this.addAttr.enable)
          formData.append('catelogId', this.addAttr.categoryVo.id);
          //console.log("catelogId=" + this.addAttr.categoryVo.id)
          formData.append('showDesc', this.addAttr.showDesc);
          //console.log("showDesc=" + this.addAttr.showDesc)

          console.log(formData);
          if (this.addAttr.attrId > 0) {
            formData.append('id', this.addAttr.attrId);
            doPut("/api/product/attr/attr", formData).then((resp) => {
              if (resp.data.code === 200){
                messageTip("编辑规格参数成功！","success");
                this.addAttrWindows = false;
                this.getData(this.currentPage);

              }else{
                messageTip("编辑规格参数失败！","error");
              }
            })
          } else {
            doPost("/api/product/attr/attr", formData).then((resp) => {
              if (resp.data.code === 200){
                messageTip("添加规格参数成功！","success");
                this.resetAddAttrForm();
                this.addAttrWindows = false;
                this.getData(this.currentPage);

              }else{
                messageTip("添加规格参数失败！","error");
              }
            })
          }
        }
      })
    },
    // 重置新增规格参数表单数据
    resetAddAttrForm() {
      if (this.$refs.addAttrRefForm) {
        this.$refs.addAttrRefForm.resetFields();
      }
      this.addAttr = {
        attrId: 0,
        attrName: "",
        searchType: 0,
        valueType: 1,
        valueSelect: [],
        icon: "",
        attrTypeVo: {
          id: "",
          name: "",
        },
        enable: 1,
        categoryVo: {
          id:'',
          name:"",
        },
        showDesc: 0,
      };
      this.seeTwoOptionsFlag = false;
      this.seeThreeOptionsFlag = false;
      this.selectThreeOptionsOver = false;
      this.selectedOneOptionsVo = { id: "", name: "" };
      this.selectedTwoOptionsVo = { id: "", name: "" };
      this.twoOptionsData = [{ id: 0, name: "" }];
      this.threeOptionsData = [{ id: 0, name: "" }];
    },
    // 新增规格参数
    add() {
      this.resetAddAttrForm();
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
        this.addAttr.categoryVo = {
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
      if (!this.selectedOneOptionsVo.id) {
        return Promise.resolve();
      }
      return doGet("/api/product/category/two", {
        oneOptionsId: this.selectedOneOptionsVo.id
      }).then(resp => {
        //console.log("二级菜单数据");
        //console.log(resp.data.data);
        if (resp.data.code === 200) {
          this.twoOptionsData = resp.data.data;
        }
        return resp;
      })
    },
    twoOptionsChange() {
      if (this.seeThreeOptionsFlag === true) {
        this.addAttr.categoryVo = {
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
      if (!this.selectedTwoOptionsVo.id) {
        return Promise.resolve();
      }
      return doGet("/api/product/category/three", {
        twoOptionsId: this.selectedTwoOptionsVo.id
      }).then(resp => {
        if (resp.data.code === 200) {
          this.threeOptionsData = resp.data.data;
        }
        return resp;
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
      this.addAttr.categoryVo = {
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
.page-container {
  min-height: calc(100vh - 120px);
}

.toolbar {
  display: flex;
  align-items: center;
  flex-wrap: wrap;
  gap: 8px;
  justify-content: flex-start;
  background: var(--bg-card);
  border: 1px solid var(--border-color);
  border-radius: var(--radius-md);
  padding: 12px 12px;
  box-shadow: var(--shadow-soft);
  margin-bottom: 12px;
}

.mySearch {
  margin: 0 8px;
}

.mySearch .el-form,
.mySearch .el-form-item {
  margin-bottom: 0;
}

.content-card {
  margin-bottom: 12px;
}

.el-dialog {
  border-radius: var(--radius-md);
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 8px;
}
</style>
