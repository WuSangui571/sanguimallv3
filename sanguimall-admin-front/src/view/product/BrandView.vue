<template>
  <!--两个按钮-->
  <el-button type="primary" @click="add">添加品牌</el-button>
  <el-button type="danger">批量删除</el-button>
  <!--表格开始-->
  <el-table
      :data="brandList"
      style="width: 100%"
      @selection-change="handleSelectionChange">
    <el-table-column type="selection" width="60"/>
    <!--若 type 为 id，则该字段会自动增长-->
    <el-table-column type="index" label="序号" width="60"/>

    <el-table-column label="LOGO" width="240">
      <template #default="scope">
        <el-image

            :src="scope.row.logo"
            fit="none"
        />
      </template>
    </el-table-column>
    <el-table-column property="name" label="品牌名" width="120"/>
    <el-table-column property="showStatus" label="显示状态" width="120">
      <template #default="scope">
        <el-switch
            :model-value="scope.row.showStatus === 1"
            @change="val => handleShowStatusChange(scope.row.brandId, val,scope.row.name)"
            class="ml-2"
            style="--el-switch-on-color: #13ce66; --el-switch-off-color: #ff4949"
        />
      </template>
    </el-table-column>
    <el-table-column property="firstLetter" label="检索首字母" width="100"/>
    <el-table-column property="sort" label="排序" width="90"/>
    <el-table-column property="descript" label="介绍" width="400">
      <template #default="scope">
      <span
          class="clickable-text"
          @click="viewDescriptDetail(scope.row.name,scope.row.descript)"
      >
        {{ getDescript(scope.row.descript) }}
      </span>
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
  <!--测试的上传按钮-->
  <!--  <div>-->
  <!--    <el-upload-->
  <!--        class="upload-demo"-->
  <!--        :http-request="customUpload"-->
  <!--        action="#"-->
  <!--        :show-file-list="false"-->
  <!--        :limit="1"-->
  <!--        :on-exceed="() => messageTip('一次只能上传一个文件', 'warning')"-->
  <!--    >-->
  <!--      <el-button type="primary">点击上传图片</el-button>-->
  <!--      <template #tip>-->
  <!--        <div class="el-upload__tip">-->
  <!--          仅至此 jpg/png 格式，且文件大小小于 10MB-->
  <!--        </div>-->
  <!--      </template>-->
  <!--    </el-upload>-->

  <!--  </div>-->
  <!--这是新增品牌的弹窗-->
  <el-dialog v-model="addBrandWindows" title="添加品牌" width="600" draggable>
    <el-form :model="addBrand" label-width="110px" :rules="addBrandRules" ref="addBrandRefForm">
      <el-form-item label="品牌名" prop="name">
        <el-input v-model="addBrand.name"/>
      </el-form-item>

      <el-form-item label="logo" prop="logoVo.savedLogo">
        <!--        <el-input v-model="addBrand.logoVo.signedLogo" />-->
        <el-upload
            ref="logoUpload"
            class="upload-demo"
            :http-request="customUpload"
            action="#"
            :show-file-list="false"
            :limit="1"
            :on-exceed="() => messageTip('一次只能上传一个文件', 'warning')"
            v-if="!uploadedImageUrl"
        >
          <el-button type="primary">点击上传图片</el-button>
          <template #tip>
            <div class="el-upload__tip" v-if="!uploadedImageUrl">
              仅支持 jpg/png 格式，且图片大小小于 10MB
            </div>
          </template>
        </el-upload>
        <!--上传完成之后的预览 div-->
        <div v-if="uploadedImageUrl" style="margin-top: 16px; display: flex; align-items: center; gap: 12px;">
          <!--          <span style="color: #67c23a; font-weight: 500;">已上传：</span>-->
          <el-image :src="signedImageUrl" style="width: 200px;" fit="fill"/>
          <!--          <el-button size="small" type="text" @click="copyUrl">复制路径</el-button>-->
          <el-button size="small" type="danger" @click="clearUploaded">清除</el-button>
        </div>
      </el-form-item>

      <el-form-item label="显示状态" prop="showStatus">
        <el-switch
            v-model="addBrand.showStatus"
            :active-value="1"
            :inactive-value="0"
            class="ml-2"
            style="--el-switch-on-color: #13ce66; --el-switch-off-color: #ff4949"
        />
      </el-form-item>

      <el-form-item label="检索首字母" prop="firstLetter">
        <el-input v-model="addBrand.firstLetter"/>
      </el-form-item>

      <el-form-item label="排序" prop="sort">
        <el-input v-model="addBrand.sort"/>
      </el-form-item>

      <el-form-item label="介绍" prop="descript">
        <el-input v-model="addBrand.descript" type="textarea"/>
      </el-form-item>

    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="addBrandWindows = false">取消</el-button>
        <el-button type="primary" @click="addBrandSubmit">
          添加
        </el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script>
import {defineComponent} from "vue";
import {doGet, doPost, doPut} from "../../http/HttpRequest.js";
import {getUUID, messageAlert, messageTip} from "../../util/util.js";

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
      // 保存上传成功的 URL，例如: test06/abc123.jpg
      uploadedImageUrl: '',
      // 进过签名验证的完整 URL，该路径可直接预览
      signedImageUrl: '',
      addBrandWindows: false,
      addBrand: {
        id: 0,
        name: "",
        logoVo: {
          savedLogo: "",
          signedLogo: ""
        },
        showStatus: 1,
        firstLetter: "",
        sort: 0,
        descript: "",
      },
      addBrandRules: {
        name: [
          {required: true, message: '请输入品牌名！', trigger: 'blur'},
          {max: 50, message: '品牌名在 50字符 之内！', trigger: 'blur'},
        ],
        'logoVo.savedLogo': [
          {required: true, message: '请上传品牌logo！', trigger: 'blur'}
        ],

        firstLetter: [
          {required: true, message: '请输入检索首字母！', trigger: 'blur'},
          {
            pattern: /^[A-Z]$/,
            message: '必须是单个大写英文字母！',
            trigger: 'blur'
          },
        ],
        sort: [
          {required: true, message: '请输入排序！', trigger: 'blur'},
          {pattern: /^[1-9]\d{0,2}$/, message: '排序必须是大于0，小于1000的整数！', trigger: 'blur'}
        ],
        descript: [
          {required: true, message: '请输入品牌介绍！', trigger: 'blur'},
          {max: 500, message: '品牌介绍在 500字符 之内！', trigger: 'blur'},
        ],
      },
    }
  },

  methods: {
    // 提交新增品牌
    addBrandSubmit() {
      this.addBrandWindows = false
      // ...
    },
    // 新增品牌
    add() {
      this.addBrandWindows = true
    },
    messageTip,
    customUpload(params) {
      const file = params.file;

      // 校验
      if (!['image/jpeg', 'image/jpg', 'image/png'].includes(file.type)) {
        messageTip('只能上传 jpg/png 文件!', 'error');
        return Promise.reject();
      }
      if (file.size / 1024 > 10000) {
        messageTip('文件不能超过 10MB!', 'error');
        return Promise.reject();
      }
      doGet("/api/thirdParty/oss/getPolicy", {
        //dir: "product/brand/logo/"
        dir: "test/"
      }).then(resp => {
        if (resp.data.code === 200) {
          const data = resp.data.data;
          const uploadUrl = `//${data.host.split('://')[1]}`;

          // 生成唯一文件名
          const uuid = getUUID();
          const fileExt = file.name.split('.').pop();
          const ossFileName = `${uuid}.${fileExt}`;  // 例如: abc123.jpg

          const formData = new FormData();
          formData.append('key', data.dir + ossFileName);
          formData.append('OSSAccessKeyId', data.accessKeyId);
          formData.append('policy', data.policy);
          formData.append('signature', data.signature);
          formData.append('success_action_status', '200');
          formData.append('file', file);

          doPost(uploadUrl, formData).then((response) => {
            // console.log("ali 返回内容：")
            // console.log(response);
            if (response.status === 200) {
              // 拼接完整 URL
              const finalUrl = `${data.baseUrl}/${data.dir}${ossFileName}`;
              // 并保存到变量
              this.uploadedImageUrl = data.dir + ossFileName;
              params.onSuccess({url: this.uploadedImageUrl}, params.file);

              this.getSignedImageUrl(data.dir + ossFileName);

              messageTip("上传成功！", "success");
            } else {
              messageTip("上传失败！", "error");
            }
          })
        } else {
          messageTip("用户验证失败！", "error");
        }
      })
    },

    // 修改7：新增方法：复制路径
    copyUrl() {
      navigator.clipboard.writeText(this.signedImageUrl).then(() => {
        messageTip('路径已复制到剪贴板', 'success');
      });
    },

    // 修改8：新增方法：清除已上传
    clearUploaded() {
      // 如果 ref 存在，就清空 el-upload 内部 fileList
      const uploadComp = this.$refs.logoUpload;
      if (uploadComp && typeof uploadComp.clearFiles === 'function') {
        uploadComp.clearFiles();
      }
      // 业务变量也清空
      this.uploadedImageUrl = '';
      this.signedImageUrl = '';
      // 如果你想连带清空 addBrand.logoVo 的字段：
      if (this.addBrand && this.addBrand.logoVo) {
        this.addBrand.logoVo.savedLogo = '';
        this.addBrand.logoVo.signedLogo = '';
      }
      messageTip('已清除', 'info');
    },

    // ... 你的其他 methods ...
    // 工具函数：获取签名 URL
    getSignedImageUrl(url) {
      doGet('/api/thirdParty/oss/getSignedUrl', {
        uploadedImageUrl: url
      }).then(resp => {
        if (resp.data.code === 200) {
          this.signedImageUrl = resp.data.data;
        }
      })
    },

    handleShowStatusChange(brandId, flag, name) {
      let formData = new FormData();
      formData.append("brandId", brandId)
      formData.append("flag", flag)
      doPut("/api/product/brand/brandStatus", formData).then((resp) => {
        if (resp.data.code === 200) {
          if (flag) {
            let messageStr = "[" + name + "] 已改为 [显示] 状态！"
            messageTip(messageStr, "success");
          } else {
            let messageStr = "[" + name + "] 已改为 [不显示] 状态！"
            messageTip(messageStr, "error");
          }

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
      const maxLength = 55; // 超过20个字符就截断
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
  }
  ,
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

.upload-demo {
  margin-top: 20px;
}
</style>