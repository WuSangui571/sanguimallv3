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

  <div>
    <el-upload
        class="upload-demo"
        :http-request="customUpload"
        action="#"
        :show-file-list="false"
        :limit="1"
        :on-exceed="() => messageTip('一次只能上传一个文件', 'warning')"
    >
      <el-button type="primary">点击上传图片</el-button>
      <template #tip>
        <div class="el-upload__tip">
          jpg/png files with a size less than 10MB.
        </div>
      </template>
    </el-upload>
    <div v-if="uploadedImageUrl" style="margin-top: 16px; display: flex; align-items: center; gap: 12px;">
      <span style="color: #67c23a; font-weight: 500;">已上传：</span>
      <el-image :src="uploadedImageUrl" style="width: 80px; height: 80px;" fit="cover"/>
      <el-button size="small" type="text" @click="copyUrl">复制路径</el-button>
      <el-button size="small" type="danger" @click="clearUploaded">清除</el-button>
    </div>
  </div>
</template>

<script>
import {defineComponent} from "vue";
import {doGet, doPut} from "../../http/HttpRequest.js";
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
      // 修改3：新增变量，保存上传成功的完整 URL
      uploadedImageUrl: '',  // 例如: https://xxx.com/test06/abc123.jpg
    }
  },

  methods: {
    messageTip,
    // 自定义上传：核心！直传 OSS
    // 自定义上传：核心！直传 OSS
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

      return doGet('/api/thirdParty/oss/getPolicy', {})
          .then(resp => {
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

            // 修改5：把 ossFileName 传到下一个 then
            return fetch(uploadUrl, {method: 'POST', body: formData}).then(response => ({
              response,
              data,
              ossFileName  // 关键！传下去
            }));
          })
          .then(({response, data, ossFileName}) => {
            if (response.ok) {
              // 修改6：拼接完整 URL 并保存到变量
              const finalUrl = `${data.baseUrl}/${data.dir}${ossFileName}`;
              this.uploadedImageUrl = finalUrl;  // 保存！

              messageTip('上传成功！', 'success');
              params.onSuccess({url: finalUrl}, params.file);
            } else {
              return response.text().then(text => {
                throw new Error(text);
              });
            }
          })
          .catch(err => {
            messageTip('上传失败: ' + err.message, 'error');
            params.onError(err, params.file);
          });
    },
    // 修改7：新增方法：复制路径
    copyUrl() {
      navigator.clipboard.writeText(this.uploadedImageUrl).then(() => {
        messageTip('路径已复制到剪贴板', 'success');
      });
    },

    // 修改8：新增方法：清除已上传
    clearUploaded() {
      this.uploadedImageUrl = '';
      messageTip('已清除', 'info');
    },

    // ... 你的其他 methods ...


    handleShowStatusChange(brandId, flag) {
      let formData = new FormData();
      formData.append("brandId", brandId)
      formData.append("flag", flag)
      doPut("/api/product/brand/brandStatus", formData).then((resp) => {
        if (resp.data.code === 200) {
          let messageStr = "已改为[" + (flag === true ? "显示" : "不显示") + "]状态！"
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