<template>
  <div class="page-container">
    <el-card class="content-card" shadow="hover">
      <div class="section-title">分类维护</div>
      <div v-loading="loading" style="max-width: 400px;">
        <el-tree
            style="max-width: 400px"
            :data="dataSource"
            show-checkbox
            node-key="id"
            default-expand-all
            :expand-on-click-node="false"
            draggable
            :allow-drop="allowDrop"
            @node-drop="handleDrop"
        >
          <template #default="{ node, data }">
            <div class="custom-tree-node">
              <span>{{ node.label }}</span>
              <div>
                <el-button type="primary" link @click="append(data)" v-if="data.level !== 3">
                  添加
                </el-button>
                <el-button type="info" link @click="edit(node,data)">
                  编辑
                </el-button>
                <el-button
                    style="margin-left: 4px"
                    type="danger"
                    link
                    @click="remove(node, data)"
                >
                  删除
                </el-button>
              </div>
            </div>
          </template>
        </el-tree>
      </div>
    </el-card>
  </div>
<el-dialog v-model="askNewLabelTableVisible" title="添加新节点" width="500">
    <el-form :model="askNewLabelFrom">
      <el-form-item label="新节点名字">
        <el-input v-model="newLabel" autocomplete="off"/>
      </el-form-item>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="askNewLabelTableVisible = false">取消</el-button>
        <el-button type="primary" @click="askNewLabelTableVisible = false">
          确认
        </el-button>
      </div>
    </template>
  </el-dialog>
  <el-dialog v-model="editLabelTableVisible" title="编辑该节点" width="500">
    <el-form :model="editLabelFrom">
      <el-form-item label="节点名字改为">
        <el-input v-model="editLabel" autocomplete="off"/>
      </el-form-item>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="editLabelTableVisible = false">取消</el-button>
        <el-button type="primary" @click="submitEdit(this.id)">
          确认
        </el-button>
      </div>
    </template>
  </el-dialog>
<!--  <el-backtop :bottom="100">-->
<!--    <div-->
<!--        style="-->
<!--        height: 100%;-->
<!--        width: 100%;-->
<!--        background-color: var(&#45;&#45;el-bg-color-overlay);-->
<!--        box-shadow: var(&#45;&#45;el-box-shadow-lighter);-->
<!--        text-align: center;-->
<!--        line-height: 40px;-->
<!--        color: #1989fa;-->
<!--      "-->
<!--    >-->
<!--      UP-->
<!--    </div>-->
<!--  </el-backtop>-->
</template>

<script>
import {defineComponent} from 'vue'
import {doDelete, doGet, doPost, doPut} from "../../http/HttpRequest.js";
import {messageConfirm, messageTip, waitFor} from "../../util/util.js";

export default defineComponent({
  name: "DashboardView",
  data() {
    return {
      // dataSource: {
      //   id: 0,
      //   label: "",
      //   level: 0,
      //   children: {
      //     id: 0,
      //     label: "",
      //     level: 0,
      //     children: {
      //       id: 0,
      //       label: "",
      //       level: 0,
      //     }
      //   }
      // },
      dataSource: [],   // ✅ 改成数组
      loading: false,   // ✅ 加一个 loading 状态
      id: 0,
      askNewLabelTableVisible: false,
      askNewLabelFrom: {},
      newLabel: "",

      editLabelTableVisible: false,
      editLabelFrom: {},
      editLabel: "",
      node: {},
      data: {},
      maxLevel: 0,
    }
  },
  mounted() {
    this.getData();
  },
  inject: ['reload'],
  methods: {
    handleDrop(draggingNode, dropNode, dropType, ev) {
      //console.log('拖动节点为：' + draggingNode.label + "，该节点被放置于：" + dropNode.label
      //    + "的" + dropType)
      let url = "/api/product/category/sequence/edit";
      let formData = new FormData();
      formData.append("draggingNodeId", draggingNode.data.id);
      formData.append("afterDropNodeId", dropNode.data.id);
      // console.log(draggingNode.data.id)
      // console.log(dropNode.data.id)
      doPut(url, formData).then((resp) => {
        console.log(resp)
        // if (resp.data.code === 200) {
        //
        // }
      })
    },
    allowDrop(draggingNode, dropNode, type) {
      // 只允许同级交换
      return dropNode.level === draggingNode.level && type === 'next';
    },

    submitEdit(catId) {
      let url = "/api/product/category/edit";
      let formData = new FormData();
      formData.append("catId", catId);
      formData.append("newNodeLabel", this.editLabel);
      doPut(url, formData).then((resp) => {
        console.log(resp)
        if (resp.data.code === 200) {
          this.editLabelTableVisible = false;
          const parent = this.node.parent;
          const children = parent?.data.children || parent?.data;
          const index = children.findIndex((d) => d.id === this.data.id);
          if (index !== -1) {
            children[index].label = this.editLabel; // 更新节点
          }
          this.dataSource = [...this.dataSource]; // 更新 dataSource
          messageTip("修改成功！", "success");
        } else {
          messageTip("修改失败！", "error");
        }
      })
    },
    edit(node, data) {
      this.editLabel = data.label;
      this.editLabelTableVisible = true;
      this.node = node;
      this.id = data.id;
      this.data = data;
    },
    async append(data) {
      if (data.level === 3) {
        messageTip("第三级的分类不可再添加了", "error")
        return;
      }
      this.askNewLabelTableVisible = true;
      // 等待关闭，提交新label 即会关闭
      await waitFor(() => this.askNewLabelTableVisible === false)
      if (this.newLabel === "") {
        return;
      }
      //alert("append:parent label=" + data.label + "parent id=" + data.id + "new node label=" + this.newLabel)
      let formData = new FormData();
      formData.append('parentId', data.id);
      formData.append('newNodeLabel', this.newLabel);
      doPost("/api/product/category/add", formData).then((resp) => {
        console.log(resp);
        if (resp.data.code === 200) {
          const newChild = {
            id: resp.data.data.id,
            label: resp.data.data.label,
            children: [],
            level: resp.data.data.level
          };
          if (!data.children) {
            data.children = [];
          }
          //console.log(data.children)
          data.children.push(newChild);
          // 更新 dataSource
          this.dataSource = [...this.dataSource];
          this.newLabel = "";
          messageTip("添加成功！", "success")
        } else {
          messageTip("添加失败！", "error");
        }
      });
    },
    remove(node, data) {
      messageConfirm("确定删除 " + data.label + " 吗？", "警告").then(() => {
        let url = "/api/product/category/del/" + data.id
        doDelete(url, {}).then((resp) => {
          if (resp.data.code === 200) {
            const parent = node.parent;
            const children = parent?.data.children || parent?.data;
            const index = children.findIndex((d) => d.id === data.id);
            if (index !== -1) {
              children.splice(index, 1); // 删除节点
            }
            this.dataSource = [...this.dataSource]; // 更新 dataSource
            messageTip("删除成功！", "success")
            //this.reload();
          } else {
            messageTip("删除失败！", "error")
          }
        })

      }).catch(() => {
        // 用户点击取消就会触发 catch 里
        messageTip("已取消删除！", "error")
      })
    },
    getData() {
      this.loading = true;
      doGet("/api/product/category/list/tree", {}).then(resp => {
        if (resp.data.code === 200) {
          //console.log(resp.data.data)
          this.loading = false;
          this.dataSource = resp.data.data;
          //console.log(this.dataSource)
        }else {
          this.loading = false;
        }
      })
    },
  },
})
</script>

<style scoped>
.custom-tree-node {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-size: 14px;
  padding-right: 8px;
}
</style>
