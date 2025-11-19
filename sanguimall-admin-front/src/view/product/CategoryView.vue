<template>
  <div class="page-container">
    <el-card class="content-card" shadow="hover">
      <div class="section-title">分类维护</div>
      <div class="tree-wrapper" v-loading="loading">
        <div class="tree-toolbar">
          <span class="tree-hint">拖拽同级可调整顺序，点击操作管理节点</span>
        </div>
        <el-tree
            class="category-tree"
            :data="dataSource"
            highlight-current
            node-key="id"
            default-expand-all
            :indent="20"
            :expand-on-click-node="false"
            draggable
            :allow-drop="allowDrop"
            @node-drop="handleDrop"
        >
          <template #default="{ node, data }">
            <div class="custom-tree-node">
              <div class="node-main">
                <span class="node-label">{{ node.label }}</span>
                <span class="node-tag">Lv{{ data.level }}</span>
              </div>
              <div class="node-actions">
                <el-button type="primary" link @click="append(data)" v-if="data.level !== 3">
                  添加
                </el-button>
                <el-button type="info" link @click="edit(node,data)">
                  编辑
                </el-button>
                <el-button
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
      <el-form-item label="新节点名称">
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
  <el-backtop
      target=".el-main"
      :right="32"
      :bottom="48"
      :visibility-height="200"
      class="backtop-box">
    <div class="backtop-inner">回到顶部</div>
  </el-backtop>
</template>

<script>
import {defineComponent} from 'vue'
import {doDelete, doGet, doPost, doPut} from "../../http/HttpRequest.js";
import {messageConfirm, messageTip, waitFor} from "../../util/util.js";

export default defineComponent({
  name: "DashboardView",
  data() {
    return {
      dataSource: [],   // 改成数组
      loading: true,
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
      let url = "/api/product/category/sequence/edit";
      let formData = new FormData();
      formData.append("draggingNodeId", draggingNode.data.id);
      formData.append("afterDropNodeId", dropNode.data.id);
      doPut(url, formData);
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
      await waitFor(() => this.askNewLabelTableVisible === false)
      if (this.newLabel === "") {
        return;
      }
      let formData = new FormData();
      formData.append('parentId', data.id);
      formData.append('newNodeLabel', this.newLabel);
      doPost("/api/product/category/add", formData).then((resp) => {
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
          data.children.push(newChild);
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
          } else {
            messageTip("删除失败！", "error")
          }
        })

      }).catch(() => {
        messageTip("已取消删除！", "error")
      })
    },
    getData() {
      this.loading = true;
      doGet("/api/product/category/list/tree", {}).then(resp => {
        if (resp.data.code === 200) {
          this.dataSource = resp.data.data;
        } else {
          messageTip("分类数据加载失败！", "error");
        }
      }).catch(() => {
        messageTip("分类数据加载异常，请稍后重试。", "error");
      }).finally(() => {
        this.loading = false;
      });
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

.tree-wrapper {
  background: var(--bg-card);
  border: 1px solid var(--border-color);
  border-radius: var(--radius-md);
  padding: 12px;
  box-shadow: var(--shadow-soft);
}

.tree-toolbar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 10px;
  color: var(--text-secondary);
  font-size: 13px;
}

.category-tree {
  padding: 8px 12px;
  border-radius: var(--radius-md);
  background: var(--bg-card);
  border: 1px solid var(--border-color);
  box-shadow: inset 0 0 0 1px rgba(79, 139, 255, 0.05);
  transition: background 0.3s ease, border-color 0.3s ease;
}

:global([data-theme="dark"]) .category-tree {
  background: rgba(17, 24, 39, 0.88);
  border-color: rgba(255, 255, 255, 0.08);
  box-shadow: inset 0 0 0 1px rgba(255, 255, 255, 0.03);
}

.custom-tree-node {
  padding: 6px 8px;
  border-radius: var(--radius-sm);
}

.custom-tree-node:hover {
  background: rgba(79, 139, 255, 0.08);
}

.node-main {
  display: flex;
  align-items: center;
  gap: 8px;
}

.node-label {
  font-weight: 600;
}

.node-tag {
  display: inline-block;
  padding: 2px 6px;
  border-radius: 10px;
  background: var(--el-color-primary-light-9);
  color: var(--el-color-primary);
  font-size: 12px;
}

.node-actions .el-button + .el-button {
  margin-left: 6px;
}

.backtop-box {
  width: 90px;
  height: 36px;
}

.backtop-inner {
  width: 100%;
  height: 100%;
  border-radius: var(--radius-sm);
  background: var(--el-color-primary);
  color: #fff;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 13px;
  box-shadow: var(--shadow-soft);
}
</style>
